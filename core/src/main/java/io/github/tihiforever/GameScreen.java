package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;

    public GameScreen(ScreenManager screenManager, Main main){
        this.screenManager = screenManager;
        this.main = main;
    }

    @Override
    public void show(){

    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render game

        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            screenManager.setScreen(Screen_Type.GAME_OVER);
        }
    }

    @Override
    public void dispose(){

    }

}
