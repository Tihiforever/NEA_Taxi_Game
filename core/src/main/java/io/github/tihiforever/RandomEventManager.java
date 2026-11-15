
package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

public class RandomEventManager {

    private Graph graph;
    private Random random = new Random();

    private Node blockedNode;

    private float eventTimer;
    private float blockedTimer;
    private String eventType;

    private String[] eventTypes = {"ROADWORKS", "TRAFFIC ACCIDENT", "ROAD CLOSURE"};

    public RandomEventManager(Graph graph) {
        this.graph = graph;
        eventTimer = random.nextFloat() * 60 + 30;
    }

    public void update(float delta, Node taxiNode) {
        if (blockedNode != null) {
            blockedTimer -= delta;

            if (blockedTimer <= 0) {
                blockedNode.setBlocked(false);
                blockedNode = null;
                eventType = null;

                eventTimer = random.nextFloat() * 60 + 30;
            }

            return;
        }

        eventTimer -= delta;

        if (eventTimer <= 0) startEvent(taxiNode);
    }

    private void startEvent(Node taxiNode) {
        ArrayList<Node> availableNodes = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            if (!node.isBlocked() && !node.isHasJob() && node != taxiNode) availableNodes.add(node);
        }

        if (availableNodes.isEmpty()) {
            eventTimer = 5;
            return;
        }

        blockedNode = availableNodes.get(random.nextInt(availableNodes.size()));
        blockedTimer = random.nextFloat() * 90 + 30;

        eventType = eventTypes[random.nextInt(eventTypes.length)];

        blockedNode.setBlocked(true);
    }

    public void draw(ShapeRenderer sr) {
        if (blockedNode == null) return;

        if (blockedTimer > 90) sr.setColor(Color.RED);
        else if (blockedTimer > 30) sr.setColor(Color.ORANGE);
        else sr.setColor(Color.YELLOW);

        sr.circle(blockedNode.getX(), blockedNode.getY(), 32);

        sr.setColor(Color.BLACK);
        sr.circle(blockedNode.getX(), blockedNode.getY(), 12);
    }

    public String getEventText() {
        if (blockedNode == null) return "";
        return eventType + "\nLocation: (" + blockedNode.getRow() + ", " + blockedNode.getCol() + ")" + "\nTime remaining: " + (int) Math.ceil(blockedTimer) + " minutes";
    }
}
