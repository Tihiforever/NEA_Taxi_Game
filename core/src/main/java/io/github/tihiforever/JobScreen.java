package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;

public class JobScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final JobManager jobManager;
    private Main main;

    private ArrayList<Button> buttons = new ArrayList<>();

    public JobScreen(ScreenManager screenManager, Main main, JobManager jobManager) {
        this.screenManager = screenManager;
        this.main = main;
        this.jobManager = jobManager;

        for (int i = 0; i < jobManager.getJobs().size(); i++) {
            buttons.add(new Button(100, 600 - i * 60, 300, 45, "Job " + (i + 1), Color.GRAY,Color.DARK_GRAY));
        }
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.65f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.update(mouseX, mouseY);
            button.draw(main.getSr(), main.getBatch(), main.getFont());

            if (button.isClicked(mouseX, mouseY,
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
                if (jobManager.acceptJob(i)) {
                    screenManager.setScreen(Screen_Type.GAME_SCREEN);
                }
            }
        }
    }

}
