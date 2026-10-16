package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen extends ScreenAdapter {

    // Main game objects
    private Taxi taxi;

    // Cameras for the game world and HUD
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    //screen stuff
    private final ScreenManager screenManager;
    private final Main main;

    public GameScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;
        taxi = new Taxi(0,0);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        main.getBatch().getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);

        main.getSr().getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);

        handleInput();
        updateEntities(delta);
        updateCamera(delta);

        drawWorld();
        drawHud(delta);
    }

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) screenManager.setScreen(Screen_Type.GAME_OVER);
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)) screenManager.setScreen(Screen_Type.JOB_SCREEN);
    }

    private void updateEntities(float delta) {
        taxi.update();
    }

    private void updateCamera(float delta) {
        camera.position.x += (taxi.getX() + taxi.getWidth() / 2f - camera.position.x) * 0.12f;
        camera.position.y += (taxi.getY() + taxi.getHeight() / 2f - camera.position.y) * 0.12f;
        camera.update();
    }

    private void drawWorld() {
        main.getSr().setProjectionMatrix(camera.combined);
        main.getSr().begin(ShapeRenderer.ShapeType.Filled);

        taxi.draw(main.getSr(), Color.YELLOW);

        main.getSr().end();
    }

    private void drawHud(float delta) {
        main.getBatch().setProjectionMatrix(hudCamera.combined);

        main.getBatch().begin();
        main.getFont().getData().setScale(1);

        main.getFont().draw(main.getBatch(), "Taxi-Game" + getInfoText(), 20, Gdx.graphics.getHeight() - 20);

        main.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        hudCamera.setToOrtho(false, width, height);

        camera.update();
        hudCamera.update();
    }

    private String getInfoText(){
        if(taxi.getSpeed() > 0){
            return String.format("Speed: %.1f (forward)", taxi.getSpeed());
        } else if (taxi.getSpeed() < 0) {
            return String.format("Speed: %.1f (reverse)", taxi.getSpeed());
        } else {
            return "Speed: 0 (stopped)";
        }
    }
}
