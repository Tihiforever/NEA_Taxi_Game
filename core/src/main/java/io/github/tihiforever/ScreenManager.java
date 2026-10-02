package io.github.tihiforever;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager{
    private Screen currentScreen;
    private Main main;

    public ScreenManager(Main main) {
        this.main = main;
    }

    public void setScreen(Screen_Type screenType) {
        // dispose of the current screen safley if it exists
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
            System.out.println("Screen disposed");
        }

        // initalise the requestd screen based on your enum
        switch (screenType) {
            case TITLE:
                currentScreen = new TitleScreen(this, main);
                break;
            case MAIN_MENU:
                currentScreen = new MainMenu(this, main);
                break;
            case JOB_SCREEN:
                currentScreen = new JobScreen(this, main);
                break;
            case GAME_SCREEN:
                currentScreen = new GameScreen(this, main);
                break;
            case SETTINGS:
                currentScreen = new SettingsScreen(this, main);
                break;
            case GAME_OVER:
                currentScreen = new GameOverScreen(this, main);
                break;
            case LEADERBOARD:
                currentScreen = new LeaderboardScreen(this, main);
                break;
        }

        // trigger the new show method on the current screen
        if (currentScreen != null) {
            currentScreen.show();
        }

    }
    public void render(float delta) {
        if (currentScreen != null) currentScreen.render(delta);
    }

    public void resize(int width, int height) {
        if (currentScreen != null) currentScreen.resize(width, height);
    }

    public void pause() {
        if (currentScreen != null) currentScreen.pause();
    }

    public void resume() {
        if (currentScreen != null) currentScreen.resume();
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }
    }


}
