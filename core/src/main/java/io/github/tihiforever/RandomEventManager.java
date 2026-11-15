
package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class RandomEventManager {

    private Graph graph;
    private Random random = new Random();
    private ArrayList<RandomEvent> events = new ArrayList<>();

    private String[] eventTypes = {"ROADWORKS", "TRAFFIC ACCIDENT", "ROAD CLOSURE"};

    private float eventTimer;
    private float announcementTimer = 4f;
    private int currentEvent = 0;
    private Node taxiNode;

    public RandomEventManager(Graph graph) {
        this.graph = graph;
        eventTimer = random.nextFloat() * 15 + 5;
    }

    public void update(float delta, Node taxiNode) {
        this.taxiNode = taxiNode;

        for (int i = events.size() - 1; i >= 0; i--) {
            RandomEvent event = events.get(i);
            event.update(delta);

            if (event.getRemainingTime() <= 0) {
                event.getNode().setBlocked(false);
                events.remove(i);
            }
        }

        eventTimer -= delta;

        if (eventTimer <= 0) {
            startEvent(taxiNode);
            eventTimer = random.nextFloat() * 15 + 5;
        }

        if (!events.isEmpty()) {
            announcementTimer -= delta;

            if (announcementTimer <= 0) {
                currentEvent++;
                if (currentEvent >= events.size()) currentEvent = 0;
                announcementTimer = 4f;
            }
        } else {
            currentEvent = 0;
            announcementTimer = 4f;
        }
    }

    private void startEvent(Node taxiNode) {
        ArrayList<Node> availableNodes = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            if (!node.isBlocked() && !node.isHasJob() && node != taxiNode) {
                availableNodes.add(node);
            }
        }

        if (availableNodes.isEmpty()) return;

        Node node = availableNodes.get(random.nextInt(availableNodes.size()));

        float chance = random.nextFloat();
        float duration;

        if (chance < 0.5f) duration = random.nextFloat() * 90 + 30;
        else if (chance < 0.85f) duration = random.nextFloat() * 120 + 120;
        else duration = random.nextFloat() * 480 + 240;

        String eventType = eventTypes[random.nextInt(eventTypes.length)];

        node.setBlocked(true);
        events.add(new RandomEvent(node, eventType, duration));
    }

    public void draw(ShapeRenderer sr) {
        for (RandomEvent event : events) {
            float remainingTime = event.getRemainingTime();

            if (remainingTime > 120) sr.setColor(Color.RED);
            else if (remainingTime > 45) sr.setColor(Color.ORANGE);
            else sr.setColor(Color.YELLOW);

            sr.circle(event.getNode().getX(), event.getNode().getY(), 32);

            sr.setColor(Color.BLACK);
            sr.circle(event.getNode().getX(), event.getNode().getY(), 12);
        }
    }

    public String getEventText() {
        if (events.isEmpty()) return "";

        RandomEvent event = events.get(currentEvent);
        Node node = event.getNode();

        float distance = getDistance(taxiNode, node);

        String text = event.getEventType();
        text += "\nLocation: (" + node.getRow() + ", " + node.getCol() + ")";
        text += "\nTaxi position: (" + taxiNode.getRow() + ", " + taxiNode.getCol() + ")";

        if (distance != Float.MAX_VALUE) text += "\nDistance: " + Math.round(distance) + " units";
        else text += "\nDistance: Route unavailable";

        text += "\nTime remaining: " + (int) Math.ceil(event.getRemainingTime()) + " minutes";

        return text;
    }

    private float getDistance(Node start, Node target) {
        if (start == null || target == null) return Float.MAX_VALUE;
        return Vector2.dst(start.getX(), start.getY(), target.getX(), target.getY());
    }
}
