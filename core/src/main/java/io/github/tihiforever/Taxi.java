package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

public class Taxi extends Car{
    private final InputHandeler inputHandeler;

    public Taxi(float x, float y, InputHandeler inputHandeler) {
        super(x, y);
        rotation = 90f;
        this.inputHandeler = inputHandeler;
    }

    public void update(){
        float dt = Gdx.graphics.getDeltaTime();
        savePreviousPosition();

        boolean forward = inputHandeler.isKeyPressed(Input.Keys.W) || inputHandeler.isKeyPressed(Input.Keys.UP);
        boolean backward = inputHandeler.isKeyPressed(Input.Keys.S) || inputHandeler.isKeyPressed(Input.Keys.DOWN);
        boolean left = inputHandeler.isKeyPressed(Input.Keys.A) || inputHandeler.isKeyPressed(Input.Keys.LEFT);
        boolean right = inputHandeler.isKeyPressed(Input.Keys.D) || inputHandeler.isKeyPressed(Input.Keys.RIGHT);


        if (collisionTimer > 0){
            collisionTimer -= dt;
        }

        float steeringMultiplier = 1f - (Math.abs(speed) / maxSpeed) * 0.6f;
        previousX = x;
        previousY = y;
        previousRotation = rotation;

        if (collisionTimer <= 0){

            if (backward){
                if (speed > 0){
                   speed -= acceleration * dt * breakMultiplier;
                   if (speed < 0) speed = 0;
                   reverseTimer = 0;

                } else {
                    reverseTimer += dt;
                    if (reverseTimer >= delay) speed -= acceleration * dt;

                }
            } else if (forward) {
                reverseTimer = 0;

                if (speed < 0){
                    speed += acceleration * dt * breakMultiplier;
                    if (speed > 0) speed = 0;

                } else {
                    speed += acceleration * dt;

                }
            } else {
                reverseTimer = 0;

                if (speed > 0) {
                    speed -= friction * dt;
                    if (speed < 0) speed = 0;

                } else if (speed < 0) {
                    speed += friction * dt;
                    if (speed > 0) speed = 0;

                }
            }

            updateHitbox();
        }

        speed = MathUtils.clamp(speed, -maxSpeed / 2f, maxSpeed);

        x += MathUtils.cosDeg(rotation) * speed * dt;
        y += MathUtils.sinDeg(rotation) * speed * dt;

        if (Math.abs(speed) > 10) {
            if(left) rotation += turnSpeed * steeringMultiplier * dt;
            if(right) rotation -= turnSpeed * steeringMultiplier * dt;
        }

    }
}
