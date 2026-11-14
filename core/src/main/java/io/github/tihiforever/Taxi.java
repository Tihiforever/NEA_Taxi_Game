package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

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

        float steeringMultiplier = (1f - (Math.abs(speed) / maxSpeed) * 0.6f) * handlingMultiplier;
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
        }

        speed = MathUtils.clamp(speed, -maxSpeed / 2f, maxSpeed);

        x += MathUtils.cosDeg(rotation) * speed * dt;
        y += MathUtils.sinDeg(rotation) * speed * dt;

        if (Math.abs(speed) > 10) {
            if(left) rotation += turnSpeed * steeringMultiplier * dt;
            if(right) rotation -= turnSpeed * steeringMultiplier * dt;
        }

        updateHitbox();
    }

    public void pickup(Graph graph, Job job){
        if (job.isPickedUp() || job.isDroppedOff() || job.isCompleted()) return;
        if(graph.getClosestNode(x + width / 2f, y + height / 2f) == job.getPickup() && Vector2.dst(x + width / 2f, y + height / 2f, graph.getClosestNode(x + width / 2f, y + height / 2f).getX(), graph.getClosestNode(x + width / 2f, y + height / 2f).getY()) <= 60f){
            job.setPickedUp(true);
            graph.getClosestNode(x + width / 2f, y + height / 2f).setHasJob(false);
        }
    }

    public void dropoff(Graph graph, Job job){
        if (!job.isPickedUp() || job.isCompleted()) return;
        if(graph.getClosestNode(x + width / 2f, y + height / 2f) == job.getDropoff() && Vector2.dst(x + width / 2f, y + height / 2f, graph.getClosestNode(x + width / 2f, y + height / 2f).getX(), graph.getClosestNode(x + width / 2f, y + height / 2f).getY()) <= 60f){
            job.setPickedUp(false);
            job.setDroppedOff(true);
            graph.getClosestNode(x + width / 2f, y + height / 2f).setHasJob(false);
            job.completedJob();
        }
    }
}
