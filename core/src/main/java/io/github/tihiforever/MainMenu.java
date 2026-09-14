package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class MainMenu extends ScreenAdapter {
    private final ScreenManager screenManager;

    public MainMenu(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Buttons here

        if(!Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)){
            screenManager.setScreen(Screen_Type.GAME_SCREEN);
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.S)) {
            screenManager.setScreen(Screen_Type.SETTINGS);
        }
    }

}
