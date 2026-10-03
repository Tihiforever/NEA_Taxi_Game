package io.github.tihiforever;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

public class Button {
    private float width;
    private float height;
    private float x;
    private float y;
    private boolean isHovered;
    private String text;
    private Color hoverColor;
    private Color baseColor;

    public Button(float x, float y, float width, float height, String text, Color hoverColor, Color basecolor) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.text = text;
        this.baseColor = basecolor;
        this.hoverColor = hoverColor;
        isHovered = false;
    }

    public void update(float mouseX, float mouseY){
        if(((mouseX >= x) && (mouseX <= (x+width))) && ((mouseY >= y) && (mouseY <= (y+height)))){
            isHovered = true;
        } else {
            isHovered = false;
        }
    }

    public void draw(ShapeRenderer sr, SpriteBatch batch, BitmapFont font){
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (isHovered){

            sr.setColor(hoverColor);
            sr.rect(x,y,width,height);
        } else {
            sr.setColor(baseColor);
            sr.rect(x,y,width,height);
        }
        sr.end();

        batch.begin();
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);
        font.draw(batch, text, x + 10f, y + (height / 2f) + 2f);
        batch.end();
    }

    public boolean isClicked(float mouseX, float mouseY, boolean isMouseClicked) {return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height && isMouseClicked;}

    public void setX(float x) {
        this.x = x;
    }
    public float getX() {return x;}

    public void setY(float y) {
        this.y = y;
    }
    public float getY() {return y;}
}
