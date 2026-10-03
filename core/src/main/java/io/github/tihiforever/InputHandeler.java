package io.github.tihiforever;

import com.badlogic.gdx.InputAdapter;

public class InputHandeler extends InputAdapter {

    private String playerName = "";

    private boolean mouseClicked = false;
    private int mouseX;
    private int mouseY;

    private int lastKeyPressed = -1;

    @Override
    public boolean keyTyped(char character) {
        if (playerName.length() < 3 && Character.isLetter(character)) {
            playerName += Character.toUpperCase(character);
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        lastKeyPressed = keycode;

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = screenY;

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseX = screenX;
        mouseY = screenY;
        mouseClicked = true;

        return true;
    }

    public String getNameDisplay() {
        String display = "";

        for (int i = 0; i < playerName.length(); i++) {
            display += playerName.charAt(i) + " ";
        }

        if (playerName.length() < 3) {
            display += "_";
        }

        return display;
    }

    public boolean isMouseClicked() {return mouseClicked;}
    public int getMouseX() {return mouseX;}
    public int getMouseY() {return mouseY;}
    public void resetMouseClick() {mouseClicked = false;}
    public String getPlayerName(){return playerName;}
    public int getLastKeyPressed() {return lastKeyPressed;}
    public void clearLastKeyPressed() {lastKeyPressed = -1;}
}
