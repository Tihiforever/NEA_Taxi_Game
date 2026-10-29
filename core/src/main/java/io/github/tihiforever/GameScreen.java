package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    private Taxi taxi;
    private City city;
    private Graph graph;
    private Pathfinder pathfinder;
    private Job job;

    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    private Node startNode;
    private Node endNode;

    private float searchTimer = 0f;
    private float secondsPerStep = 0.15f;
    private boolean visualisationPaused = false;
    private boolean visualisationEnabled = false;

    private boolean degbugMode = true;

    public GameScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;
        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);

        city = new City();
        graph = new Graph(city);
        taxi = new Taxi(graph.getClosestNode(city.getWorldWidth() / 2f, city.getWorldHeight() / 2f).getX(),  graph.getClosestNode(city.getWorldWidth() / 2f, city.getWorldHeight() / 2f).getY(), inputHandeler);
        pathfinder = new Pathfinder(graph);

        startNode = graph.getClosestNode(taxi.getX(), taxi.getY());
        if (!graph.getNodes().isEmpty()) {
            endNode = graph.getNodes().get((int) (Math.random() * graph.getNodes().size()));
            pathfinder.startSearch(startNode, endNode);
        }
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        startNode = graph.getClosestNode(taxi.getX(), taxi.getY());

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.getBatch().getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);
        main.getSr().getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);

        handleInput();
        updateEntities(delta);
        updateCamera(delta);
        drawWorld();
        drawHud();
    }


    private void handleInput() {
        if (inputHandeler.getLastKeyPressed() == Input.Keys.Z) {
            screenManager.setScreen(Screen_Type.GAME_OVER);
            inputHandeler.clearLastKeyPressed();
        }
        if (inputHandeler.getLastKeyPressed() == Input.Keys.J) {
            screenManager.setScreen(Screen_Type.JOB_SCREEN);
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
            if (!graph.getNodes().isEmpty()) {
                endNode = graph.getNodes().get((int) (Math.random() * graph.getNodes().size()));

                searchTimer = 0f;
                pathfinder.startSearch(startNode, endNode);
            }

            inputHandeler.clearLastKeyPressed();
        }
    }


    private void updateEntities(float delta) {
        taxi.update();
        taxi.resolveCollision();

        if (visualisationEnabled && !visualisationPaused && !pathfinder.isSearchFinished()) {
            searchTimer += delta;

            if (searchTimer >= secondsPerStep) {
                searchTimer = 0f;
                pathfinder.stepSearch();
            }
        }
    }

    private void updateCamera(float delta) {
        camera.position.x += (taxi.getX() + taxi.getWidth() / 2f - camera.position.x) * 0.12f;
        camera.position.y += (taxi.getY() + taxi.getHeight() / 2f - camera.position.y) * 0.12f;
        camera.update();
    }

    private void drawWorld() {
        main.getSr().setProjectionMatrix(camera.combined);
        main.getSr().begin(ShapeRenderer.ShapeType.Filled);
        city.draw(main.getSr());
        if(degbugMode){
        graph.draw(main.getSr());
        }
        if (visualisationEnabled) {
            drawPathfindingVisualisation();
        } else {
            ArrayList<Node> path = pathfinder.findPath(startNode, endNode);

            main.getSr().setColor(Color.GREEN);

            for(int i=0;i<path.size()-1;i++) {
                Node a = path.get(i);
                Node b = path.get(i+1);

                main.getSr().rectLine(a.getX(), a.getY(), b.getX(), b.getY(), 8);
            }

            main.getSr().setColor(Color.RED);
            main.getSr().circle(startNode.getX(), startNode.getY(), 12);

            main.getSr().setColor(Color.BLUE);
            main.getSr().circle(endNode.getX(), endNode.getY(), 12);
        }
        taxi.draw(main.getSr(), Color.YELLOW);
        main.getSr().end();
    }

    private void drawHud() {
        main.getBatch().setProjectionMatrix(hudCamera.combined);
        main.getBatch().begin();
        main.getFont().getData().setScale(1);
        main.getFont().draw(main.getBatch(), getInfoText(), 20, Gdx.graphics.getHeight() - 20);
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
        if (taxi.getSpeed() > 0) return String.format("Speed: %.1f (forward)", taxi.getSpeed());
        if (taxi.getSpeed() < 0) return String.format("Speed: %.1f (reverse)", taxi.getSpeed());
        return "Speed: 0 (stopped)";
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

            sr.setColor(Color.RED);
            sr.circle(endNode.getX(), endNode.getY(), 17);
        }
    }
}
