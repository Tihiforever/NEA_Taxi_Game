package io.github.tihiforever;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {
    private ScreenManager screenManager;
    private ShapeRenderer sr;
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        screenManager = new ScreenManager(this);
        screenManager.setScreen(Screen_Type.TITLE);
    }

    @Override
    public void render() {
        screenManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        batch.dispose();
        sr.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width, height);
    }

    @Override
    public void pause() {
        screenManager.pause();
    }

    @Override
    public void resume() {
        screenManager.resume();
    }
}
