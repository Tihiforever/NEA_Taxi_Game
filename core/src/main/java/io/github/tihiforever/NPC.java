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
    private int currentWheypoint;

    public NPC(Graph graph, Pathfinder pathfinder) {
        super(0, 0);

        this.graph = graph;
        this.pathfinder = pathfinder;

        currentNode = graph.getNodes().get(random.nextInt(graph.getNodes().size()));

        x = currentNode.getX() - width / 2f;
        y = currentNode.getY() - height / 2f;
        updateHitbox();

        chooseDestination();

        if (path == null || path.size() < 2) return;

        Node next = path.get(1);

        float dx = next.getX() - currentNode.getX();
        float dy = next.getY() - currentNode.getY();

        rotation = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
        updateHitbox();
    }

    public void update(float delta) {
        if (path == null || currentWheypoint >= path.size()) return;

        savePreviousPosition();

        Node target = path.get(currentWheypoint);

        float dx = target.getX() - (x + width / 2f);
        float dy = target.getY() - (y + height / 2f);
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        rotation = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;

        float movement = 200f * delta;

        if (distance <= movement) {
            x = target.getX() - width / 2f;
            y = target.getY() - height / 2f;

            currentNode = target;
            currentWheypoint++;

            if (currentWheypoint >= path.size()) chooseDestination();
        } else {
            x += MathUtils.cosDeg(rotation) * movement;
            y += MathUtils.sinDeg(rotation) * movement;
        }

        updateHitbox();
    }

    private void chooseDestination() {
        do {
            destinationNode = graph.getNodes().get(random.nextInt(graph.getNodes().size()));
        } while (destinationNode == currentNode);

        path = pathfinder.findPath(currentNode, destinationNode);
        currentWheypoint = 1;
    }

    public void draw(ShapeRenderer sr) {
        super.draw(sr, Color.RED);
    }
}
