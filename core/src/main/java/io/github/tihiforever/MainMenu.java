package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class MainMenu extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    private Button gameButton, settingsButton, leaderboardButton, exitButton;

    private float buttonWidth = 260f;
    private float buttonHeight = 45f;
    private float spacing = 25f;

    public MainMenu(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        gameButton = new Button(0, 0, buttonWidth, buttonHeight, "PLAY GAME", Color.DARK_GRAY, Color.LIGHT_GRAY);
        leaderboardButton = new Button(0, 0, buttonWidth, buttonHeight, "LEADERBOARDS", Color.DARK_GRAY, Color.LIGHT_GRAY);
        settingsButton = new Button(0, 0, buttonWidth, buttonHeight, "SETTINGS", Color.DARK_GRAY, Color.LIGHT_GRAY);
        exitButton = new Button(0, 0, buttonWidth, buttonHeight, "EXIT GAME", Color.DARK_GRAY, Color.LIGHT_GRAY);

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);
    }

    @Override
    public void render(float delta){
        // get width and hieght for varied screen sizes
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        gameButton.setX((screenWidth - buttonWidth) / 2f);
        gameButton.setY((screenHeight / 2f) + (spacing * 1.5f));

        leaderboardButton.setX((screenWidth - buttonWidth) / 2f);
        leaderboardButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - (spacing + buttonHeight));

        settingsButton.setX((screenWidth - buttonWidth) / 2f);
        settingsButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - 2*(spacing + buttonHeight));

        exitButton.setX((screenWidth - buttonWidth) / 2f);
        exitButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - 3*(spacing + buttonHeight));


        // clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = inputHandeler.getMouseX();
        float mouseY = screenHeight - inputHandeler.getMouseY();

        gameButton.update(mouseX, mouseY);
        leaderboardButton.update(mouseX, mouseY);
        settingsButton.update(mouseX, mouseY);
        exitButton.update(mouseX, mouseY);

        if (inputHandeler.isMouseClicked()) {
            if (gameButton.isClicked(mouseX, mouseY, true)) {
                screenManager.setScreen(Screen_Type.GAME_SCREEN);
            } else if (leaderboardButton.isClicked(mouseX, mouseY, true)) {
                screenManager.setScreen(Screen_Type.LEADERBOARD);
            } else if (settingsButton.isClicked(mouseX, mouseY, true)) {
                screenManager.setScreen(Screen_Type.SETTINGS);
            } else if (exitButton.isClicked(mouseX, mouseY, true)) {
                Gdx.app.exit();
            }
            inputHandeler.resetMouseClick();
        }
        gameButton.draw(main.getSr(), main.getBatch(), main.getFont());
        leaderboardButton.draw(main.getSr(), main.getBatch(), main.getFont());
        settingsButton.draw(main.getSr(), main.getBatch(), main.getFont());
        exitButton.draw(main.getSr(), main.getBatch(), main.getFont());

        main.getBatch().begin();

        main.getFont().getData().setScale(6f);
        main.getFont().setColor(Color.WHITE);
        main.getFont().draw(main.getBatch(), "Main Menu", screenWidth / 2f - 210f, screenHeight / 2f + 220f);
        main.getFont().getData().setScale(1f);

        main.getBatch().end();

    }

}
