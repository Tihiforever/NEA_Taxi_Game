package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class TitleScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private BitmapFont font;
    private Batch batch;

    public TitleScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.55f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isTouched()){
            screenManager.setScreen(Screen_Type.MAIN_MENU);
        }
    }
}
