
package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;

public class JobScreen extends ScreenAdapter {
    private enum SortType {
        PICKUP, JOURNEY, TOTAL_DISTANCE, PATIENCE, FARE
    }

    private final ScreenManager screenManager;
    private final Main main;
    private final JobManager jobManager;
    private final Taxi taxi;
    private final PlayerStats playerStats;
    private final InputHandeler inputHandeler;

    private SortType sortType = SortType.PICKUP;

    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Button> sortButtons = new ArrayList<>();
    private ArrayList<Job> displayJobs = new ArrayList<>();
    private ArrayList<Float> pickupDistances = new ArrayList<>();
    private Button activeJobButton;

    private float buttonWidth = 900f;
    private float buttonHeight = 65f;
    private float spacing = 10f;

    public JobScreen(ScreenManager screenManager, Main main, JobManager jobManager, Taxi taxi, PlayerStats playerStats) {
        this.screenManager = screenManager;
        this.main = main;
        this.jobManager = jobManager;
        this.taxi = taxi;
        this.playerStats = playerStats;

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);

        for (SortType type : SortType.values()) {
            sortButtons.add(new Button(0, 0, 150, 45, type.name(), Color.DARK_GRAY, Color.LIGHT_GRAY));
        }

        activeJobButton = new Button(0, 0, 0, 0, "", Color.ORANGE, Color.GOLD);

        refreshJobs();
    }

    private void refreshJobs() {
        displayJobs.clear();
        pickupDistances.clear();

        displayJobs.addAll(jobManager.getJobs());

        if (jobManager.getActvJob() != null) displayJobs.remove(jobManager.getActvJob());

        for (Job job : displayJobs) {
            pickupDistances.add(job.getPickupDistance(taxi.getX(), taxi.getY()));
        }

        sortDisplayJobs();
    }

    private float getSortValue(int index) {
        Job job = displayJobs.get(index);

        if (sortType == SortType.PICKUP) return pickupDistances.get(index);
        if (sortType == SortType.JOURNEY) return job.getOptimalDist();
        if (sortType == SortType.TOTAL_DISTANCE) return pickupDistances.get(index) + job.getOptimalDist();
        if (sortType == SortType.PATIENCE) return job.getPatience();

        return job.getBasePay();
    }

    private void sortDisplayJobs() {
        for (int i = 0; i < displayJobs.size(); i++) {
            for (int j = i + 1; j < displayJobs.size(); j++) {

                float a = getSortValue(i);
                float b = getSortValue(j);

                boolean swap = false;

                if (sortType == SortType.PICKUP || sortType == SortType.JOURNEY || sortType == SortType.TOTAL_DISTANCE) {
                    if (b < a) swap = true;
                } else {
                    if (b > a) swap = true;
                }

                if (swap) {
                    Job tempJob = displayJobs.get(i);
                    displayJobs.set(i, displayJobs.get(j));
                    displayJobs.set(j, tempJob);

                    float tempDistance = pickupDistances.get(i);
                    pickupDistances.set(i, pickupDistances.get(j));
                    pickupDistances.set(j, tempDistance);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.65f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = inputHandeler.getMouseX();
        float mouseY = screenHeight - inputHandeler.getMouseY();

        float buttonWidth = Math.min(900f, screenWidth - 40f);

        Job actvJob = jobManager.getActvJob();

        float sortY = screenHeight - 190f;

        if (actvJob != null) {
            sortY = screenHeight - 285f;

            activeJobButton.setX((screenWidth - buttonWidth) / 2f);
            activeJobButton.setY(sortY + 65f);
            activeJobButton.setWidth(buttonWidth);
            activeJobButton.setHeight(buttonHeight);

            activeJobButton.update(mouseX, mouseY);
            activeJobButton.draw(main.getSr(), main.getBatch(), main.getFont());
        }

        float sortWidth = (buttonWidth - spacing * 4f) / 5f;
        float sortHeight = 45f;
        float sortStartX = (screenWidth - buttonWidth) / 2f;

        for (int i = 0; i < sortButtons.size(); i++) {
            Button button = sortButtons.get(i);

            button.setX(sortStartX + i * (sortWidth + spacing));
            button.setY(sortY);
            button.setWidth(sortWidth);
            button.setHeight(sortHeight);

            button.update(mouseX, mouseY);
            button.draw(main.getSr(), main.getBatch(), main.getFont());
        }

        float listTop = sortY - 15f;

        buttonWidth = Math.min(900f, screenWidth - 40f);

        int visibleJobs = (int)((listTop - 20f) / (buttonHeight + spacing));
        visibleJobs = Math.min(visibleJobs, displayJobs.size());

        while (buttons.size() < visibleJobs) {
            buttons.add(new Button(0, 0, buttonWidth, buttonHeight, "", Color.DARK_GRAY, Color.LIGHT_GRAY));
        }

        while (buttons.size() > visibleJobs) {
            buttons.remove(buttons.size() - 1);
        }

        for (int i = 0; i < visibleJobs; i++) {
            Button button = buttons.get(i);

            button.setX((screenWidth - buttonWidth) / 2f);
            button.setY(listTop - (i + 1) * (buttonHeight + spacing));
            button.setWidth(buttonWidth);
            button.setHeight(buttonHeight);

            button.update(mouseX, mouseY);
            button.draw(main.getSr(), main.getBatch(), main.getFont());
        }

        if (inputHandeler.isMouseClicked()) {
            boolean sortClicked = false;

            for (int i = 0; i < sortButtons.size(); i++) {
                if (sortButtons.get(i).isClicked(mouseX, mouseY, true)) {
                    sortType = SortType.values()[i];
                    sortDisplayJobs();
                    sortClicked = true;
                    break;
                }
            }

            if (!sortClicked) {
                for (int i = 0; i < visibleJobs; i++) {
                    if (buttons.get(i).isClicked(mouseX, mouseY, true)) {
                        int jobIndex = jobManager.getJobs().indexOf(displayJobs.get(i));

                        if (jobIndex != -1 && jobManager.acceptJob(jobIndex)) {
                            inputHandeler.resetMouseClick();
                            screenManager.setScreen(Screen_Type.GAME_SCREEN);
                            return;
                        }
                    }
                }
            }

            inputHandeler.resetMouseClick();
        }

        main.getBatch().begin();

        main.getFont().setColor(Color.WHITE);

        main.getFont().getData().setScale(6f);
        main.getFont().draw(main.getBatch(), "JOBS", screenWidth / 2f - 100f, screenHeight - 50f);

        main.getFont().getData().setScale(1.5f);
        main.getFont().draw(main.getBatch(), "REPUTATION: " + String.format("%.1f", playerStats.getReputation()) + "/5", screenWidth - 250f, screenHeight - 65f);

        main.getFont().getData().setScale(1.3f);

        if (actvJob != null) {
            main.getFont().setColor(Color.BLACK);

            String status = "PICKUP REQUIRED";

            if (actvJob.isPickedUp()) status = "PASSENGER ONBOARD";

            main.getFont().draw(main.getBatch(), "ACTIVE JOB " + actvJob.getJobNum() + " - " + status, activeJobButton.getX() + 10f, activeJobButton.getY() + 58f);
            main.getFont().draw(main.getBatch(), "JOURNEY: " + String.format("%.0f", actvJob.getOptimalDist()) + "     PATIENCE: " + String.format("%.0f", actvJob.getPatience()) + "     FARE: £" + String.format("%.2f", actvJob.getBasePay()), activeJobButton.getX() + 10f, activeJobButton.getY() + 30f);

            main.getFont().setColor(Color.WHITE);
        }

        for (int i = 0; i < visibleJobs; i++) {
            Job job = displayJobs.get(i);
            Button button = buttons.get(i);

            float pickupDistance = pickupDistances.get(i);
            float journeyDistance = job.getOptimalDist();
            float totalDistance = pickupDistance + journeyDistance;

            String firstLine = "Job " + job.getJobNum() + "              PICKUP: " + String.format("%.0f", pickupDistance) + "     JOURNEY: " + String.format("%.0f", journeyDistance);
            String secondLine = "                       TOTAL DISTANCE: " + String.format("%.0f", totalDistance) + "     PATIENCE: " + String.format("%.0f", job.getPatience()) + "     FARE: £" + String.format("%.2f", job.getBasePay());

            main.getFont().draw(main.getBatch(), firstLine, button.getX() + 10f, button.getY() + 58f);
            main.getFont().draw(main.getBatch(), secondLine, button.getX() + 10f, button.getY() + 30f);
        }

        main.getFont().getData().setScale(1f);
        main.getBatch().end();

        if (inputHandeler.getLastKeyPressed() == Input.Keys.ENTER) {
            screenManager.setScreen(Screen_Type.GAME_SCREEN);
            inputHandeler.clearLastKeyPressed();
        }
    }
}
