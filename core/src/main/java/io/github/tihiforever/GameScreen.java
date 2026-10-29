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
    private City city;

    // Cameras for the game world and HUD
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    //screen stuff
    private final ScreenManager screenManager;
    private final Main main;

    private int TILE_SIZE = 64;

    public GameScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;
        taxi = new Taxi(0,0);
        city = new City();
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

        draw_background();
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

    private void draw_background() {
        // work out which tiles are visible based on camera position
        int screenW = Gdx.graphics.getWidth();
        int screenH = Gdx.graphics.getHeight();

        // find the top-left world coordinate the camera can see
        float camLeft = camera.position.x - screenW / 2f;
        float camBottom = camera.position.y - screenH / 2f;

        // convert to tile indices, go one tile extra each side so no gaps appear at edges
        int startCol = (int) Math.floor(camLeft / TILE_SIZE) - 1;
        int startRow = (int) Math.floor(camBottom / TILE_SIZE) - 1;

        // how many tiles fit across and up the screen
        int tilesX = (int) Math.ceil((float) screenW / TILE_SIZE) + 2;
        int tilesY = (int) Math.ceil((float) screenH / TILE_SIZE) + 2;

        for (int col = startCol; col < startCol + tilesX; col++) {
            for (int row = startRow; row < startRow + tilesY; row++) {
                // checkerboard using modulo - handles negative indices correctly
                int check = Math.floorMod(col + row, 2);
                if (check == 0) {
                    main.getSr().setColor(0.25f, 0.25f, 0.25f, 1);
                } else {
                    main.getSr().setColor(0.3f, 0.3f, 0.3f, 1);
                }
                main.getSr().rect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
