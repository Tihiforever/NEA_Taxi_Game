package io.github.tihiforever;

import com.badlogic.gdx.InputAdapter;

public class InputHandeler extends InputAdapter {

    private String playerName = "";

    @Override
    public boolean keyTyped(char character) {
        if (playerName.length() < 3 && Character.isLetter(character)) {
            playerName += Character.toUpperCase(character);
        }

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

    public String getPlayerName(){return playerName;}
}
