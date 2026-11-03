package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Random;

public class NPC extends Car {

    private Graph graph;
    private Pathfinder pathfinder;
    private Random random = new Random();

    private Node currentNode;
    private Node destinationNode;

    private ArrayList<Node> path;
    private int currentWaypoint;

    public NPC(Graph graph, Pathfinder pathfinder) {
        super(0, 0);

        this.graph = graph;
        this.pathfinder = pathfinder;

        currentNode = graph.getNodes().get(random.nextInt(graph.getNodes().size()));

        x = currentNode.getX();
        y = currentNode.getY();

        chooseDestination();
        updateHitbox();
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

        if (collisionTimer > 0) {
            collisionTimer -= dt;
            return;
        }

        Node target = path.get(currentWaypoint);

        float dx = target.getX() - x;
        float dy = target.getY() - y;

        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if (collisionTimer > 0) collisionTimer -= dt;

        savePreviousPosition();

        if (distance < 10) {
            currentWaypoint++;

            if (currentWaypoint >= path.size()) {
                currentNode = destinationNode;
                x = currentNode.getX();
                y = currentNode.getY();
                chooseDestination();
                return;
            }

            target = path.get(currentWaypoint);
            dx = target.getX() - x;
            dy = target.getY() - y;
        }

        rotation = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;


        if (collisionTimer <= 0) speed = 200;

        x += MathUtils.cosDeg(rotation) * speed * dt;
        y += MathUtils.sinDeg(rotation) * speed * dt;

        updateHitbox();
    }

    public void draw(ShapeRenderer sr) {
        super.draw(sr, Color.RED);
    }
}
