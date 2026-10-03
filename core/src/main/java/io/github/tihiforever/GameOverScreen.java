package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import java.io.FileWriter;
import java.io.IOException;

public class GameOverScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    private final float screenWidth = Gdx.graphics.getWidth();
    private final float screenHeight = Gdx.graphics.getHeight();

    private int playerScore = 500;

    public GameOverScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.getBatch().begin();

        main.getFont().getData().setScale(6f);
        main.getFont().setColor(Color.WHITE);
        main.getFont().draw(main.getBatch(), "GAME OVER", (screenWidth / 2f) - 270f, screenHeight - 50f);
        main.getFont().getData().setScale(3f);
        main.getFont().draw(main.getBatch(), "ENTER YOUR NAME", (screenWidth/2f) - 215f, screenHeight - 180f);
        main.getFont().getData().setScale(8f);
        main.getFont().draw(main.getBatch(), inputHandeler.getNameDisplay(), (screenWidth /2f) - 150f, screenHeight - 300f);
        main.getBatch().end();


        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            writeToFile(inputHandeler.getPlayerName(), playerScore);
            screenManager.setScreen(Screen_Type.TITLE);
        }
    }

    private void writeToFile(String name, int score){
        try {
            FileWriter writer = new FileWriter("leaderboard.txt", true);

            writer.write(name + "," + score + "\n");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
