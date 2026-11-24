package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.nio.file.Path;
import java.util.Random;
import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    private Taxi taxi;
    private City city;
    private Graph graph;
    private Graph NPCgraph;
    private Pathfinder pathfinder;
    private JobManager jobManager;
    private RandomEventManager randomEventManager;
    private MiniMap miniMap;
    private PlayerStats playerStats;
    private NPCManager npcManager;
    private UpgradeManager upgradeManager;
    private Pathfinder NPCpathfinder;

    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    private Node startNode;
    private Node endNode;
    private ArrayList<Node> currentPath = new ArrayList<>();

    private Difficulty difficulty;

    private float searchTimer = 0f;
    private float secondsPerStep = 0.15f;
    private boolean visualisationPaused = false;
    private boolean visualisationEnabled = false;

    private float gameTime = 8 * 60;
    private int hour = 8;
    private int minute = 0;
    private boolean rushhour = false;

    private boolean[] previousAchievements = new boolean[10];
    private int achievementNotification = -1;
    private float achievementTimer = 0f;

    private boolean degbugMode = false;
    private boolean showPlayerStats = false;
    private boolean showMiniMap = true;

    public GameScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;
        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);
        difficulty = Difficulty.NORMAL;

        city = new City();
        graph = new Graph(city);
        NPCgraph = new Graph(city);
        taxi = new Taxi(graph.getClosestNode(city.getWorldWidth() / 2f, city.getWorldHeight() / 2f).getX(),  graph.getClosestNode(city.getWorldWidth() / 2f, city.getWorldHeight() / 2f).getY(), inputHandeler);
        pathfinder = new Pathfinder(graph);
        NPCpathfinder = new Pathfinder(NPCgraph);
        upgradeManager = new UpgradeManager();
        playerStats = new PlayerStats();
        jobManager = new JobManager(graph, pathfinder, upgradeManager, 5, playerStats, difficulty);
        randomEventManager = new RandomEventManager(graph);
        miniMap = new MiniMap(city, taxi);
        npcManager = new NPCManager(graph, NPCpathfinder, graph.getNodes().size() / 3);

        startNode = graph.getClosestNode(taxi.getX(), taxi.getY());
        if (!graph.getNodes().isEmpty()) {
            endNode = graph.getNodes().get((int) (Math.random() * graph.getNodes().size()));
            pathfinder.startSearch(startNode, endNode);
        }

        playerStats.addMoney(100000);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(inputHandeler);
    }

    @Override
    public void render(float delta) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        if (!visualisationEnabled) {
            startNode = graph.getClosestNode(taxi.getX(), taxi.getY());
        }

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.getBatch().getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);
        main.getSr().getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);

        handleInput();
        updateEntities(delta);
        updateCamera();
        drawWorld();
        drawHud();
    }


    private void handleInput() {
        if (inputHandeler.getLastKeyPressed() == Input.Keys.Z) {
            screenManager.setScreen(Screen_Type.GAME_OVER);
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.X) {
            screenManager.setScreen(Screen_Type.SETTINGS);
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.U) {
            screenManager.setScreen(Screen_Type.UPGRADE_SCREEN);
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.R) {
            taxi.setX(graph.getClosestNode(taxi.getX(), taxi.getY()).getX() - taxi.getWidth());
            taxi.setY(graph.getClosestNode(taxi.getX(), taxi.getY()).getY() - taxi.getHeight());
            taxi.setSpeed(0);
            taxi.getHitbox().setPosition(taxi.getX(), taxi.getY());
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.J) {
            screenManager.setScreen(Screen_Type.JOB_SCREEN);
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.M) {
            showMiniMap = !showMiniMap;
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.H) {
            showPlayerStats = !showPlayerStats;
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.F) {
            if (jobManager.getActvJob() == null && !visualisationEnabled) {
                Job nearestJob = null;

                float shrtDist = Float.MAX_VALUE;

                float taxiX = taxi.getX() + taxi.getWidth() / 2f;
                float taxiY = taxi.getY() + taxi.getHeight() / 2f;

                for (Job job : jobManager.getJobs()) {
                    float dist = job.getPickupDistance(taxiX, taxiY);

                    if (dist < shrtDist) {
                        shrtDist = dist;
                        nearestJob = job;
                    }
                }

                if (nearestJob != null) jobManager.acceptJob(nearestJob);
            }

            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.TAB) {
            visualisationEnabled = !visualisationEnabled;
            inputHandeler.clearLastKeyPressed();

            if (visualisationEnabled && !graph.getNodes().isEmpty()) {
                endNode = graph.getNodes().get((int) (Math.random() * graph.getNodes().size()));

                searchTimer = 0f;
                pathfinder.startSearch(startNode, endNode);
            }
        }
        if (visualisationEnabled) {
            if (inputHandeler.getLastKeyPressed() == Input.Keys.SPACE) {
                visualisationPaused = !visualisationPaused;
                inputHandeler.clearLastKeyPressed();
            }
            if (inputHandeler.getLastKeyPressed() == Input.Keys.MINUS) {
                secondsPerStep = Math.min(2f, secondsPerStep + 0.1f);
                inputHandeler.clearLastKeyPressed();
            }
            if (inputHandeler.getLastKeyPressed() == Input.Keys.EQUALS) {
                secondsPerStep = Math.max(0.02f, secondsPerStep - 0.1f);
                inputHandeler.clearLastKeyPressed();
            }
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.P) {
            if (startNode != null && endNode != null) {
                searchTimer = 0f;
                visualisationPaused = false;

                pathfinder.startSearch(startNode, endNode);
            }

            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.T) {
            if (!graph.getNodes().isEmpty()) {
                startNode = graph.getClosestNode(taxi.getX(), taxi.getY());
                endNode = graph.getNodes().get((int) (Math.random() * graph.getNodes().size()));

                searchTimer = 0f;
                visualisationPaused = false;
                pathfinder.startSearch(startNode, endNode);
            }

            inputHandeler.clearLastKeyPressed();
        }
    }


    private void updateEntities(float delta) {
        gameTime += delta;

        if (gameTime >= 1440) gameTime -= 1440;

        hour = (int) gameTime / 60;
        minute = (int) gameTime % 60;

        rushhour = (hour >= 7 && hour < 9) || (hour >= 16 && hour < 19);
        jobManager.setRushHour(rushhour);

        taxi.update();
        npcManager.update(delta);

        Job actvJob = jobManager.getActvJob();

        if (actvJob != null) {
            taxi.pickup(graph, actvJob);
            taxi.dropoff(graph, actvJob, gameTime);

            if(actvJob.isCompleted()){
                playerStats.addMoney(actvJob.getFinalPay());
                playerStats.addJobCompleted(actvJob.getPatience(), actvJob.getActDist(), actvJob.getNumBuildingCollisions(), gameTime, upgradeManager);
                actvJob = jobManager.getExtraJob();
            }
        }

        float taxiBounceSpeed;

        if (taxi.getSpeed() < 0) taxiBounceSpeed = Math.max(100f, -taxi.getSpeed() * 0.5f);
        else taxiBounceSpeed = -Math.max(100f, taxi.getSpeed() * 0.5f);

        if (city.collidesWithBuilding(taxi.getHitbox())) {
            taxi.resolveCollision(taxiBounceSpeed);

            if (actvJob != null) {
                actvJob.recordCollision(false);
            }
        }

        jobManager.update(delta, taxi, city, playerStats);
        randomEventManager.update(delta, graph.getClosestNode(taxi.getX(), taxi.getY()));

        if (visualisationEnabled && !visualisationPaused && !pathfinder.isSearchFinished()) {
            searchTimer += delta;

            if (searchTimer >= secondsPerStep) {
                searchTimer = 0f;
                pathfinder.stepSearch();
            }
        }

        achievementTimer -= delta;

        boolean[] achievements = playerStats.getAchievements();

        if (achievementTimer <= 0) {
            achievementNotification = -1;

            for (int i = 0; i < achievements.length; i++) {
                if (achievements[i] && !previousAchievements[i]) {
                    achievementNotification = i;
                    achievementTimer = 4f;
                    previousAchievements[i] = true;
                    break;
                }
            }
        }
    }

    private void updateCamera() {
        camera.position.x += (taxi.getX() + taxi.getWidth() / 2f - camera.position.x) * 0.12f;
        camera.position.y += (taxi.getY() + taxi.getHeight() / 2f - camera.position.y) * 0.12f;
        camera.update();
    }

    private void drawWorld() {
        if (!visualisationEnabled) currentPath.clear();

        main.getSr().setProjectionMatrix(camera.combined);
        main.getSr().begin(ShapeRenderer.ShapeType.Filled);

        city.draw(main.getSr());

        if(degbugMode || visualisationEnabled) graph.draw(main.getSr());
        randomEventManager.draw(main.getSr());

        Job actvJob = jobManager.getActvJob();

        if (visualisationEnabled) {
            currentPath = pathfinder.getCurrentPath();
            drawPathfindingVisualisation();
        } else if (actvJob != null) {
            if (actvJob.isPickedUp()) {
                currentPath = pathfinder.findPath(startNode, actvJob.getDropoff());
            } else {
                currentPath = pathfinder.findPath(startNode, actvJob.getPickup());
            }

            main.getSr().setColor(Color.GREEN);

            for (int i = 0; i < currentPath.size() - 1; i++) {
                Node a = currentPath.get(i);
                Node b = currentPath.get(i + 1);

                main.getSr().rectLine(a.getX(), a.getY(), b.getX(), b.getY(), 8);
            }
        }

        if (!visualisationEnabled) npcManager.draw(main.getSr());

        taxi.draw(main.getSr(), Color.YELLOW, upgradeManager);
        if (degbugMode) taxi.drawHitbox(main.getSr());

        main.getSr().setColor(Color.PINK);

        if (actvJob != null) {
            if (!actvJob.isPickedUp()) {
                main.getSr().circle(actvJob.getPickup().getX(), actvJob.getPickup().getY(), 15);
            } else {
                main.getSr().circle(actvJob.getDropoff().getX(), actvJob.getDropoff().getY(), 15);
            }
        }

        main.getSr().end();
    }

    private void drawHud() {
        ShapeRenderer sr = main.getSr();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.setProjectionMatrix(hudCamera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(0, 0, 0, 0.7f);
        if (jobManager.getActvJob() != null && showPlayerStats) {
            sr.rect(15, Gdx.graphics.getHeight() - 220, 270, 205);
        } else if (jobManager.getActvJob() == null && showPlayerStats) {
            sr.rect(15, Gdx.graphics.getHeight() - 210, 270, 195);
        } else if (jobManager.getActvJob() != null){
            sr.rect(15, Gdx.graphics.getHeight() - 115, 270, 100);
        } else {
            sr.rect(15, Gdx.graphics.getHeight() - 95, 270, 80);
        }

        if (showMiniMap) miniMap.draw(sr, jobManager.getActvJob(), currentPath, graph, pathfinder, visualisationEnabled, endNode, startNode);

        float eventWidth = 270;
        float eventHeight = 100;
        float eventX = Gdx.graphics.getWidth() - eventWidth - 15;
        float eventY = Gdx.graphics.getHeight() - eventHeight - 15;

        if (!randomEventManager.getEventText().isEmpty()) {
            sr.setColor(0, 0, 0, 0.8f);
            sr.rect(eventX, eventY, eventWidth, eventHeight);
        }

        if (achievementNotification != -1 && achievementTimer > 0) {
            float boxWidth = 500f;
            float boxHeight = 180f;

            sr.setColor(0, 0, 0, 0.9f);
            sr.rect((Gdx.graphics.getWidth() - boxWidth) / 2f, (Gdx.graphics.getHeight() - boxHeight) / 2f, boxWidth, boxHeight);
        }

        sr.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        main.getBatch().setProjectionMatrix(hudCamera.combined);
        main.getBatch().begin();

        main.getFont().getData().setScale(1f);
        main.getFont().draw(main.getBatch(), getInfoText(), 30, Gdx.graphics.getHeight() - 25);

        if (!randomEventManager.getEventText().isEmpty()) main.getFont().draw(main.getBatch(), randomEventManager.getEventText(), eventX + 15f, Gdx.graphics.getHeight() - 25f);

        if (achievementNotification != -1 && achievementTimer > 0) {
            String achievementText = "";

            if (achievementNotification == 0) achievementText = "First Steps - £50\n(Complete 1 job)";
            else if (achievementNotification == 1) achievementText = "Endless Driver - £10,000\n(Drive 1,000,000 units)";
            else if (achievementNotification == 2) achievementText = "Getting Somewhere - £200\n(Earn £200)";
            else if (achievementNotification == 3) achievementText = "Taxi Veteran - £1,000\n(Complete 100 jobs)";
            else if (achievementNotification == 4) achievementText = "Upgrade Monster - £5,000\n(Purchase all upgrades)";
            else if (achievementNotification == 5) achievementText = "Night Owl - £200\n(Complete 10 jobs between 20:00-06:00)";
            else if (achievementNotification == 6) achievementText = "Taxi Tycoon - £1000\n(Earn £10,000)";
            else if (achievementNotification == 7) achievementText = "Perfectionist - 500£\n(Reach at least 4.5 reputation)";
            else if (achievementNotification == 8) achievementText = "Busy Bee - £100\n(Complete 25 jobs)";
            else if (achievementNotification == 9) achievementText = "Clean Getaway - £800\n(Complete 10 collision-free jobs)";

            main.getFont().getData().setScale(2f);
            main.getFont().draw(main.getBatch(), "ACHIEVEMENT UNLOCKED", Gdx.graphics.getWidth() / 2f - 200f, Gdx.graphics.getHeight() / 2f + 45f);

            main.getFont().getData().setScale(1.3f);
            main.getFont().draw(main.getBatch(), achievementText, Gdx.graphics.getWidth() / 2f - 200f, Gdx.graphics.getHeight() / 2f + 5f);
        }

        main.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        if (camera == null || hudCamera == null) return;
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);
        camera.update();
        hudCamera.update();
    }

    private String getInfoText() {
        String info = "";

        info += String.format("Time: %02d:%02d", hour, minute);
        if (rushhour) info += "  (RUSH HOUR!)";
        info += "\n";

        info += String.format("Speed: %.1f", taxi.getSpeed());

        if (taxi.getSpeed() > 0) info += " (Forward)";
        if (taxi.getSpeed() < 0) info += " (Reverse)";
        if (taxi.getSpeed() == 0) info += " (Stopped)";

        Job job = jobManager.getActvJob();

        if (job == null) info += "\nAvailable Jobs: " + jobManager.getJobs().size();
        info += "\nObjective: ";

        if (job != null ) {

            if (job.isPickedUp()) info += "Deliver Customer";
            else info += "Collect Passenger";


            info += "\nFare: £" + String.format("%.2f", job.getBasePay());
            info += "\nPatience: " + String.format("%.0f", job.getPatience());
        } else {
            info += "Find A Job";
        }

        if (showPlayerStats) {
            info += "\n\nPLAYER STATS";
            info += "\nMoney: £" + String.format("%.2f", playerStats.getMoney());
            info += "\nReputation: " + String.format("%.1f", playerStats.getReputation()) + "/5 Stars";
            info += "\nJobs Completed: " + playerStats.getTotalJobsCompleted();
            info += "\nDistance Driven: " + String.format("%.0f", playerStats.getTotalDistanceDriven());
        }

        return info;
    }

    private void drawPathfindingVisualisation() {
        ShapeRenderer sr = main.getSr();

        for (Edge edge : graph.getEdges()) {
            Node a = edge.getStart();
            Node b = edge.getEnd();

            Node child = null;

            if (a.getParent() == b) {
                child = a;
            } else if (b.getParent() == a) {
                child = b;
            }

            if (child == null) continue;
            if (child.isClosed()) sr.setColor(Color.ORANGE);

            else if (child.isOpen()) {
                sr.setColor(Color.SKY);
            } else {
                continue;
            }

            sr.rectLine(a.getX(), a.getY(), b.getX(), b.getY(), 10);
        }

        if (pathfinder.isPathFound()) {
            ArrayList<Node> path = pathfinder.getCurrentPath();

            sr.setColor(Color.BLACK);
            for (int i = 0; i < path.size() - 1; i++) {
                Node a = path.get(i);
                Node b = path.get(i + 1);

                sr.rectLine(a.getX(), a.getY(), b.getX(), b.getY(), 22);
            }

            sr.setColor(Color.CYAN);
            for (int i = 0; i < path.size() - 1; i++) {
                Node a = path.get(i);
                Node b = path.get(i + 1);

                sr.rectLine(a.getX(), a.getY(), b.getX(), b.getY(), 13);
            }
        }

        for (Node node : graph.getNodes()) {
            if (node.isClosed()) {
                sr.setColor(Color.ORANGE);
                sr.circle(node.getX(), node.getY(), 13);
            } else if (node.isOpen()) {
                sr.setColor(Color.SKY);
                sr.circle(node.getX(), node.getY(), 13);
            }
        }

        Node current = pathfinder.getCurrentNode();

        if (current != null && current != startNode && current != endNode) {
            sr.setColor(Color.WHITE);
            sr.circle(current.getX(), current.getY(), 22);

            sr.setColor(Color.MAGENTA);
            sr.circle(current.getX(), current.getY(), 15);
        }

        if (startNode != null) {
            sr.setColor(Color.BLACK);
            sr.circle(startNode.getX(), startNode.getY(), 23);

            sr.setColor(Color.LIME);
            sr.circle(startNode.getX(), startNode.getY(), 17);
        }

        if (endNode != null) {
            sr.setColor(Color.BLACK);
            sr.circle(endNode.getX(), endNode.getY(), 23);

            sr.setColor(Color.WHITE);
            sr.circle(endNode.getX(), endNode.getY(), 17);
        }
    }

    public JobManager getJobManager() {return jobManager;}
    public City getCity() {return city;}
    public Graph getGraph() {return graph;}
    public Pathfinder getPathfinder() {return pathfinder;}
    public PlayerStats getPlayerStats() {return playerStats;}
    public Taxi getTaxi() {return taxi;}
    public UpgradeManager getUpgradeManager() {return upgradeManager;}
    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}
    public Difficulty getDifficulty() {return difficulty;}
}
