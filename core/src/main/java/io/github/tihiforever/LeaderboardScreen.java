package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class LeaderboardScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    private PriorityQueue playerData;

    public LeaderboardScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        playerData = readFromFile();

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);
    }

    @Override
    public void render(float delta){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Clear the screen
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.getBatch().begin();

        main.getFont().getData().setScale(6f);
        main.getFont().setColor(Color.WHITE);
        main.getFont().draw(main.getBatch(), "LEADERBOARDS", (screenWidth / 2f) - 350f, screenHeight - 50f);
        main.getFont().getData().setScale(3.5f);
        main.getFont().draw(main.getBatch(), "TOP 10", (screenWidth / 2f) - 100f, screenHeight - 130f);
        main.getFont().getData().setScale(3f);

        float spacing = 40f;
        for (int i = 0; i < playerData.length() && i < 10; i++) {
            float y = screenHeight - 180f - (i * spacing);

            main.getFont().draw(main.getBatch(), (i + 1) + ".", screenWidth/8, y);
            main.getFont().draw(main.getBatch(), playerData.getName(i), screenWidth/4, y);
            main.getFont().draw(main.getBatch(), "" + playerData.getScore(i), screenWidth - 300f, y);
        }
        main.getBatch().end();


        if(inputHandeler.isMouseClicked()){
            screenManager.setScreen(Screen_Type.MAIN_MENU);
            inputHandeler.resetMouseClick();
        }
    }

    private PriorityQueue readFromFile(){
        PriorityQueue playerData = new PriorityQueue();

        try {
            FileReader file = new FileReader("leaderboard.txt");
            BufferedReader reader = new BufferedReader(file);

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                playerData.push(data[0], Integer.parseInt(data[1]));
            }

            reader.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerData;
    }

}
