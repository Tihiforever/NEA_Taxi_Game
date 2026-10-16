package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;

public class Taxi extends Car{
    private final InputHandeler inputHandeler;

    public Taxi(float x, float y) {
        super(x, y);

        inputHandeler = new InputHandeler();
        Gdx.input.setInputProcessor(inputHandeler);
    }

    public void update(){
        float dt = Gdx.graphics.getDeltaTime();

        boolean forward = inputHandeler.keyDown(Input.Keys.W) || inputHandeler.keyDown(Input.Keys.UP);
        boolean backward = inputHandeler.keyDown(Input.Keys.S) || inputHandeler.keyDown(Input.Keys.DOWN);
        boolean left = inputHandeler.keyDown(Input.Keys.A) || inputHandeler.keyDown(Input.Keys.LEFT);
        boolean right = inputHandeler.keyDown(Input.Keys.D) || inputHandeler.keyDown(Input.Keys.RIGHT);


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
                    if (speed < 0) speed = 0;

                }
            }
        }

        speed = MathUtils.clamp(speed, -maxSpeed / 2f, maxSpeed);

        if (Math.abs(speed) > 10) {
            if(right) rotation += turnSpeed * steeringMultiplier * dt;
            if(left) rotation -= turnSpeed * steeringMultiplier * dt;
        }

    }
}
