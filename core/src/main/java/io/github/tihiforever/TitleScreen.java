package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class TitleScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private Screen

    public TitleScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sr.begin

        if(Gdx.input.isTouched()){
            screenManager.setScreen(Screen_Type.MAIN_MENU);
        }
    }

}
