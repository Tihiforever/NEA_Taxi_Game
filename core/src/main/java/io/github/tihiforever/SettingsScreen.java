package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class SettingsScreen extends ScreenAdapter {
    private final ScreenManager screenManager;
    private final Main main;
    private final InputHandeler inputHandeler;

    private Button easyButton, normalButton, hardButton, smallResButton, medResButton, bigResButton;

    private float buttonWidth = 180f;
    private float buttonHeight = 45f;
    private float spacing = 45f;

    public SettingsScreen(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);

        easyButton = new Button(0, 0, buttonWidth, buttonHeight, "EASY", Color.DARK_GRAY, Color.LIGHT_GRAY);
        normalButton = new Button(0, 0, buttonWidth, buttonHeight, "NORMAL", Color.DARK_GRAY, Color.LIGHT_GRAY);
        hardButton = new Button(0, 0, buttonWidth, buttonHeight, "HARD", Color.DARK_GRAY, Color.LIGHT_GRAY);
        smallResButton = new Button(0, 0, buttonWidth, buttonHeight, "800 x 600", Color.DARK_GRAY, Color.LIGHT_GRAY);
        medResButton = new Button(0, 0, buttonWidth, buttonHeight, "1280 x 720", Color.DARK_GRAY, Color.LIGHT_GRAY);
        bigResButton = new Button(0, 0, buttonWidth, buttonHeight, "1820 x 980", Color.DARK_GRAY, Color.LIGHT_GRAY);
    }

    @Override
    public void render(float delta){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float settingsWidth = 900f;
        float columnWidth = settingsWidth / 3f;

        float settingsStartX = (screenWidth - settingsWidth) / 2f;

        easyButton.setX(settingsStartX + columnWidth / 2f - buttonWidth / 2f);
        easyButton.setY((screenHeight / 2f) + (spacing * 1.5f));

        normalButton.setX(settingsStartX + columnWidth / 2f - buttonWidth / 2f);
        normalButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - (spacing + buttonHeight));

        hardButton.setX(settingsStartX + columnWidth / 2f - buttonWidth / 2f);
        hardButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - 2 * (spacing + buttonHeight));

        smallResButton.setX(settingsStartX + columnWidth + columnWidth / 2f - buttonWidth / 2f);
        smallResButton.setY((screenHeight / 2f) + (spacing * 1.5f));

        medResButton.setX(settingsStartX + columnWidth + columnWidth / 2f - buttonWidth / 2f);
        medResButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - (spacing + buttonHeight));

        bigResButton.setX(settingsStartX + columnWidth + columnWidth / 2f - buttonWidth / 2f);
        bigResButton.setY(((screenHeight / 2f) + (spacing * 1.5f)) - 2 * (spacing + buttonHeight));

        // Clear the screen
        Gdx.gl.glClearColor(0.25f, 0.25f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mouseX = inputHandeler.getMouseX();
        float mouseY = screenHeight - inputHandeler.getMouseY();

        easyButton.update(mouseX, mouseY);
        normalButton.update(mouseX, mouseY);
        hardButton.update(mouseX, mouseY);
        smallResButton.update(mouseX, mouseY);
        medResButton.update(mouseX, mouseY);
        bigResButton.update(mouseX, mouseY);

        if (inputHandeler.isMouseClicked()) {
            if (easyButton.isClicked(mouseX, mouseY, true)) {
                main.setDifficulty(Difficulty.EASY);
            } else if (normalButton.isClicked(mouseX, mouseY, true)) {
                main.setDifficulty(Difficulty.NORMAL);
            } else if (hardButton.isClicked(mouseX, mouseY, true)) {
                main.setDifficulty(Difficulty.HARD);
            } else if (smallResButton.isClicked(mouseX, mouseY, true)) {
                Gdx.graphics.setWindowedMode(800, 600);
            } else if (medResButton.isClicked(mouseX, mouseY, true)) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else if (bigResButton.isClicked(mouseX, mouseY, true)) {
                Gdx.graphics.setWindowedMode(1820, 980);
            }
            inputHandeler.resetMouseClick();
        }

        easyButton.draw(main.getSr(), main.getBatch(), main.getFont());
        normalButton.draw(main.getSr(), main.getBatch(), main.getFont());
        hardButton.draw(main.getSr(), main.getBatch(), main.getFont());
        smallResButton.draw(main.getSr(), main.getBatch(), main.getFont());
        medResButton.draw(main.getSr(), main.getBatch(), main.getFont());
        bigResButton.draw(main.getSr(), main.getBatch(), main.getFont());

        main.getBatch().begin();

        main.getFont().getData().setScale(6f);
        main.getFont().setColor(Color.WHITE);
        main.getFont().draw(main.getBatch(), "SETTINGS", (screenWidth / 2f) - 200f, screenHeight - 50f);
        main.getFont().getData().setScale(2.5f);
        main.getFont().draw(main.getBatch(), "DIFFICULTY", settingsStartX + columnWidth / 2f - 100f, (screenHeight / 2f) + 170f);
        main.getFont().draw(main.getBatch(), "RESOLUTION", settingsStartX + columnWidth + columnWidth / 2f - 100f, (screenHeight / 2f) + 170f);
        main.getFont().draw(main.getBatch(), "KEY BINDS", settingsStartX + 2 * columnWidth + columnWidth / 2f - 100f, (screenHeight / 2f) + 170f);
        main.getFont().getData().setScale(1.5f);
        main.getFont().draw(main.getBatch(), "Current: " + main.getDifficulty(), settingsStartX + columnWidth / 2f - 85f, hardButton.getY() - 40f);
        main.getFont().draw(main.getBatch(), "Current: " + (int)screenWidth + " x " + (int)screenHeight, settingsStartX + columnWidth + columnWidth / 2f - 100f, bigResButton.getY() - 40f);

        main.getBatch().end();

        System.out.println(main.getDifficulty());
        System.out.println(screenWidth);
        System.out.println(screenHeight);

        if (inputHandeler.getLastKeyPressed() == Input.Keys.ENTER) {
            screenManager.setScreen(Screen_Type.MAIN_MENU);
            inputHandeler.clearLastKeyPressed();
        }
    }
}
