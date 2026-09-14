package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class GameOverScreen extends ScreenAdapter {
    private final ScreenManager screenManager;

    public GameOverScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Title

        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
            screenManager.setScreen(Screen_Type.TITLE);
        }
    }

}
