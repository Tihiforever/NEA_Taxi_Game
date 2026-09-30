package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Car {

    // standard stuff
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float rotation;
    protected float speed;

    // constants
    protected final float maxSpeed = 1400f;
    protected final float acceleration = 400f;
    protected final float friction = 300f;
    protected final float turnSpeed = 220f;
    protected final float breakMultiplier = 5f;

    // waiting times and delay
    protected float reverseTimer = 0f;
    protected float collisionTimer = 0f;
    protected final float delay = 0.25f;

    // collision stuff
    protected float previousX;
    protected float previousY;
    protected float previousRotation;

    public Car(float x, float y){
        this.x = x;
        this.y = y;
        width = 46;
        height = 82;
    }

    public void draw(ShapeRenderer sr, Color colour){
        sr.setColor(colour);

        sr.getTransformMatrix().translate(x + width/2f, y + height/2f, 0).rotate(0,0,1,rotation-90).translate(-width/2f,-height/2f,0);
        sr.updateMatrices();

        sr.rect(0, 0, width, height);

        sr.identity();
    }

    public void bounce(float newSpeed) {
        speed = newSpeed;
        collisionTimer = delay;
    }


}
