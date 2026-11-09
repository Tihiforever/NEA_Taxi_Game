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

        if (collisionTimer > 0) collisionTimer -= dt;

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

        rotation = MathUtils.atan2(ty, tx) * MathUtils.radiansToDegrees;

        if (collisionTimer <= 0f){
            speed = 300f;

            for (NPC npc : NPCManager.getNpcs()) {
                if (npc == this) continue;

                float dx = npc.x - x;
                float dy = npc.y - y;

                float ahead = dx * MathUtils.cosDeg(rotation) + dy * MathUtils.sinDeg(rotation);
                float side = Math.abs(-dx * MathUtils.sinDeg(rotation) + dy * MathUtils.sinDeg(rotation));

                if (ahead > 0 && ahead < 110 && side < 30) {
                    speed = Math.max(0, Math.min(speed, (ahead - 35) * 4));
                }
            }
        }

        x += MathUtils.cosDeg(rotation) * speed * dt;
        y += MathUtils.sinDeg(rotation) * speed * dt;

        updateHitbox();
    }

    public void draw(ShapeRenderer sr) {super.draw(sr, Color.FIREBRICK);}
}
