package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TitleScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    // Taxi position and movement variables
    private float taxiX;
    private float taxiY;
    private float taxiSpeed;

    // Scale variable
    private float scale;

    public TitleScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);
    }

    @Override
    public void show() {
        // Initial taxi starting values
        taxiX = -100f;   // Start off-screen on the left
        taxiY = 50f;     // Height above bottom of screen
        taxiSpeed = 400f; // Pixels per second
    }

    @Override
    public void render(float delta) {
        // width height can change when screen size changes
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        //update taxi
        taxiX += taxiSpeed * delta;

        //update text scale
        scale = 6f;

        // reset the taxi
        if (taxiX > screenWidth + 100f) {
            taxiX = -100f;
        }

        Gdx.gl.glClearColor(0.15f, 0.55f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.getSr().begin(ShapeRenderer.ShapeType.Filled);

        // Road line
        main.getSr().setColor(Color.DARK_GRAY);
        main.getSr().rect(0f, 25f, screenWidth, 50f);

        // Yellow Body
        main.getSr().setColor(Color.YELLOW);
        main.getSr().rect(taxiX - 35f, taxiY - 15f, 70f, 30f);


        // Black Wheels
        main.getSr().setColor(Color.BLACK);
        main.getSr().rect(taxiX - 28f, taxiY + 15f, 12f, 5f);  // Top Left
        main.getSr().rect(taxiX + 16f, taxiY + 15f, 12f, 5f);   // Top Right
        main.getSr().rect(taxiX - 28f, taxiY - 20f, 12f, 5f);  // Bottom Left
        main.getSr().rect(taxiX + 16f, taxiY - 20f, 12f, 5f);   // Bottom Right

        main.getSr().end();

        main.getBatch().begin();

        // Main title
        main.getFont().getData().setScale(scale);
        main.getFont().setColor(Color.YELLOW);
        main.getFont().draw(main.getBatch(), "Taxi Game", screenWidth / 2f - 240f, screenHeight / 2f + 120f);

        // subtitle
        main.getFont().getData().setScale(scale/2);
        main.getFont().setColor(Color.ORANGE);
        main.getFont().draw(main.getBatch(), "in sim city", screenWidth / 2f - 60f, screenHeight / 2f + 30f);

        //start
        main.getFont().getData().setScale(scale/3);
        main.getFont().setColor(Color.WHITE);
        main.getFont().draw(main.getBatch(), "TOUCH TO START", screenWidth / 2f - 100f, screenHeight / 2f - 90f);

        main.getBatch().end();

        if(inputHandeler.isMouseClicked()){
            screenManager.setScreen(Screen_Type.MAIN_MENU);
            inputHandeler.resetMouseClick();
        }
    }
}
