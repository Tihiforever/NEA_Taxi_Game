package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class LeaderboardScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private Main main;

    public LeaderboardScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;
    }

    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(1f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            screenManager.setScreen(Screen_Type.MAIN_MENU);
        }
    }

}
