package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class MainMenu extends ScreenAdapter {
    private final ScreenManager screenManager;
    private Main main;

    private Button gameButton, settingsButton, leaderboardButton, exitButton;
    private float screenWidth = Gdx.graphics.getWidth();
    private float screenHeight = Gdx.graphics.getHeight();;

    private float buttonWidth = 260f;
    private float buttonHeight = 45f;
    private float spacing = 25f;

    private float buttonX = (screenWidth - buttonWidth) / 2f;;
    private float buttonY = (screenHeight / 2f) + (spacing * 1.5f);

    public MainMenu(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        gameButton = new Button(buttonX, buttonY, buttonWidth, buttonHeight, "PLAY GAME", Color.DARK_GRAY, Color.LIGHT_GRAY, Screen_Type.GAME_SCREEN, screenManager);
        leaderboardButton = new Button(buttonX, buttonY - (spacing + buttonHeight), buttonWidth, buttonHeight, "LEADERBOARDS", Color.DARK_GRAY, Color.LIGHT_GRAY, Screen_Type.LEADERBOARD, screenManager);
        settingsButton = new Button(buttonX, buttonY - 2*(spacing + buttonHeight), buttonWidth, buttonHeight, "SETTINGS", Color.DARK_GRAY, Color.LIGHT_GRAY, Screen_Type.SETTINGS, screenManager);
        exitButton = new Button(buttonX, buttonY - 3*(spacing + buttonHeight), buttonWidth, buttonHeight, "EXIT GAME", Color.DARK_GRAY, Color.LIGHT_GRAY, null, screenManager);
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = Gdx.input.getX();
        float mouseY = screenHeight - Gdx.input.getY();
        boolean isClicked = Gdx.input.justTouched();

        gameButton.update(mouseX, mouseY, isClicked);
        gameButton.draw(main.getSr(), main.getBatch(), main.getFont());

        leaderboardButton.update(mouseX, mouseY, isClicked);
        leaderboardButton.draw(main.getSr(), main.getBatch(), main.getFont());

        settingsButton.update(mouseX, mouseY, isClicked);
        settingsButton.draw(main.getSr(), main.getBatch(), main.getFont());

        exitButton.update(mouseX, mouseY, isClicked);
        exitButton.draw(main.getSr(), main.getBatch(), main.getFont());

        main.getBatch().begin();

        main.getFont().getData().setScale(6f);
        main.getFont().setColor(Color.WHITE);
        main.getFont().draw(main.getBatch(), "Main Menu", screenWidth / 2f - 210f, screenHeight / 2f + 220f);
        main.getFont().getData().setScale(1f);

        main.getBatch().end();

    }

}
