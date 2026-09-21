package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class SettingsScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private OrthographicCamera camera;

    public SettingsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
        
    @Override
    public void render(float delta){
        // Clear the screen
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            screenManager.resize(400,400);
            System.out.println("TEST MF");
        }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            screenManager.setScreen(Screen_Type.MAIN_MENU);
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

}
