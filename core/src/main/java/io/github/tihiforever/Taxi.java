package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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

    public void draw(ShapeRenderer sr, Color colour, UpgradeManager upgrades) {
        super.draw(sr, colour);

        sr.getTransformMatrix().translate(x + width / 2f, y + height / 2f, 0).rotate(0, 0, 1, rotation - 90).translate(-width / 2f, -height / 2f, 0);
        sr.updateMatrices();

        if (upgrades.hasUpgrade(Upgrades.SPEED1)) {
            sr.setColor(0.65f, 0.65f, 0.65f, 1f);
            sr.rect(13f, -10f, 9f, 13f);
        }

        if (upgrades.hasUpgrade(Upgrades.SPEED2)) {
            sr.setColor(Color.ORANGE);
            sr.triangle(5f, -7f, 31f, -7f, 18f, -18f);

            sr.setColor(Color.YELLOW);
            sr.triangle(11f, -8f, 25f, -8f, 18f, -14f);
        }

        if (upgrades.hasUpgrade(Upgrades.HANDLING1)) {
            sr.setColor(Color.BLACK);
            sr.rect(-8f, 12f, 8f, 21f);
            sr.rect(width, 12f, 8f, 21f);
            sr.rect(-8f, 55f, 8f, 21f);
            sr.rect(width, 55f, 8f, 21f);

            sr.setColor(Color.LIGHT_GRAY);
            sr.rect(-4f, 19f, 4f, 7f);
            sr.rect(width, 19f, 4f, 7f);
            sr.rect(-4f, 62f, 4f, 7f);
            sr.rect(width, 62f, 4f, 7f);
        }

        if (upgrades.hasUpgrade(Upgrades.BRAKES2)) {
            sr.setColor(Color.LIGHT_GRAY);
            sr.rect(2f, height - 3f, width - 4f, 5f);
            sr.rect(5f, height - 6f, 4f, 5f);
            sr.rect(width - 9f, height - 6f, 4f, 5f);
            sr.rect(0f, -2f, width, 4f);
            sr.rect(-2f, 0f, 4f, height);
            sr.rect(width - 2f, 0f, 4f, height);
        }

        if (upgrades.hasUpgrade(Upgrades.FARES1)) {
            sr.setColor(Color.BLACK);
            sr.rect(13f, 35f, 20f, 12f);

            sr.setColor(Color.YELLOW);
            sr.rect(15f, 37f, 16f, 8f);
        }

        if (upgrades.hasUpgrade(Upgrades.FARES2)) {
            sr.setColor(0.85f, 0.65f, 0.15f, 1f);
            sr.rect(5f, 18f, 2f, 42f);
            sr.rect(width - 7f, 18f, 2f, 42f);
        }

        if (upgrades.hasUpgrade(Upgrades.PASSENGERS1)) {
            sr.setColor(0.65f, 0.48f, 0.30f, 1f);
            sr.rect(7f, 13f, 13f, 8f);
            sr.rect(26f, 13f, 13f, 8f);
        }

        if (upgrades.hasUpgrade(Upgrades.DISPATCH1)) {
            sr.setColor(Color.BLACK);
            sr.rect(20f, 4f, 6f, 10f);
        }

        if (upgrades.hasUpgrade(Upgrades.DISPATCH2)) {
            sr.setColor(Color.CYAN);

            sr.rect(13f, 8f, 2f, 5f);
            sr.rect(9f, 5f, 2f, 8f);
            sr.rect(5f, 2f, 2f, 9f);

            sr.rect(31f, 8f, 2f, 5f);
            sr.rect(35f, 5f, 2f, 8f);
            sr.rect(39f, 2f, 2f, 9f);
        }

        sr.identity();
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
