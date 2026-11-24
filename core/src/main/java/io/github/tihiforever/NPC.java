package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Random;

public class NPC extends Car {
    private Taxi taxi;
    private Graph graph;
    private Pathfinder pathfinder;
    private PlayerStats playerStats;
    private Random random = new Random();

    private Node currentNode;
    private Node destinationNode;

    private ArrayList<Node> path;
    private int currentWaypoint;

    private boolean isPoPo;
    private boolean hasBeenSeen;

    public NPC(Graph graph, Pathfinder pathfinder, Taxi taxi, PlayerStats playerStats) {
        super(0, 0);

        this.graph = graph;
        this.pathfinder = pathfinder;
        this.taxi = taxi;
        this.playerStats = playerStats;
        speed = 300f;

        currentNode = graph.getNodes().get(random.nextInt(graph.getNodes().size()));

        x = currentNode.getX();
        y = currentNode.getY();


        chooseDestination();
        updateHitbox();

        if (random.nextFloat() < 0.05f) isPoPo = true;
        else isPoPo = false;

        hasBeenSeen = false;
    }

    private void chooseDestination() {
        do {
            destinationNode = graph.getNodes().get(random.nextInt(graph.getNodes().size()));
        } while (destinationNode == currentNode);

        path = pathfinder.findPath(currentNode, destinationNode);
        currentWaypoint = 0;
    }

    public void update(float dt) {
        if (path == null || path.isEmpty()) return;

        savePreviousPosition();

        if (collisionTimer > 0) {
            collisionTimer -= dt;

            x += MathUtils.cosDeg(rotation) * speed * dt;
            y += MathUtils.sinDeg(rotation) * speed * dt;

            updateHitbox();
            return;
        }

        Node target = path.get(currentWaypoint);

        float tx = target.getX() - x;
        float ty = target.getY() - y;

        float distance = (float)Math.sqrt(tx * tx + ty * ty);

        if (distance <= 5) {
            currentWaypoint++;

            if (currentWaypoint >= path.size()) {
                currentNode = destinationNode;
                x = currentNode.getX();
                y = currentNode.getY();
                chooseDestination();
                return;
            }

            target = path.get(currentWaypoint);
            tx = target.getX() - x;
            ty = target.getY() - y;
        }

        if (isPoPo) {
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < 1000; j++) {
                    if (x + width / 2f + i >= taxi.getX()){
                        if (y + height / 2f + j >= taxi.getY()) {
                            if (taxi.getSpeed() > 700) {
                                playerStats.addMoney((int) taxi.getSpeed() - 700);
                            }
                        }
                    }
                }
            }
        }

        rotation = MathUtils.atan2(ty, tx) * MathUtils.radiansToDegrees;

        x += MathUtils.cosDeg(rotation) * speed * dt;
        y += MathUtils.sinDeg(rotation) * speed * dt;

        updateHitbox();
    }

    public void draw(ShapeRenderer sr) {
        if (isPoPo && !hasBeenSeen) super.draw(sr, Color.BLUE);
        else super.draw(sr, Color.FIREBRICK);
    }
}
