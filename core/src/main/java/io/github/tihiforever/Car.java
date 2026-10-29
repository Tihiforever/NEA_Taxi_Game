package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Car {

    // standard stuff
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float rotation;
    protected float speed;
    protected Polygon hitbox;

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

    public Car(float x, float y) {
        this.x = x;
        this.y = y;
        width = 46;
        height = 82;

        hitbox = new Polygon(new float[]{5, 6, width - 11, 6, width - 11, height - 16, 5, height - 1});
        hitbox.setOrigin(width / 2f, height / 2f);
    }

    public void draw(ShapeRenderer sr, Color colour) {
        sr.setColor(colour);

        sr.getTransformMatrix().translate(x + width / 2f, y + height / 2f, 0).rotate(0, 0, 1, rotation - 90).translate(-width / 2f, -height / 2f, 0);
        sr.updateMatrices();

        sr.rect(0, 0, width, height);

        sr.identity();
    }

    public void bounce(float newSpeed) {
        speed = newSpeed;
        collisionTimer = delay;
    }

    protected void updateHitbox() {
        hitbox.setPosition(x, y);
        hitbox.setRotation(rotation);
    }

    public boolean collidesWith(Car other) {
        return Intersector.overlapConvexPolygons(hitbox, other.hitbox);
    }

    public void savePreviousPosition() {
        previousX = x;
        previousY = y;
        previousRotation = rotation;
    }

    public void resolveCollision() {
        x = previousX;
        y = previousY;
        rotation = previousRotation;

        updateHitbox();
        bounce(-speed * 0.2f);
    }

    public float getCollisionTimer() {
        return collisionTimer;
    }

    public float getX() {return x;}
    public void setX(float x) {this.x = x;}

    public float getWidth() {return width;}
    public void setWidth(float width) {this.width = width;}

    public float getY() {return y;}
    public void setY(float y) {this.y = y;}

    public float getHeight() {return height;}
    public void setHeight(float height) {this.height = height;}

    public float getRotation() {return rotation;}
    public void setRotation(float rotation) {this.rotation = rotation;}

    public float getSpeed() {return speed;}
    public void setSpeed(float speed) {this.speed = speed;}
}
