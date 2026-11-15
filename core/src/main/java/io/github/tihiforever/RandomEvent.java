
package io.github.tihiforever;

public class RandomEvent {

    private Node node;
    private String eventType;
    private float remainingTime;

    public RandomEvent(Node node, String eventType, float duration) {
        this.node = node;
        this.eventType = eventType;
        this.remainingTime = duration;
    }

    public void update(float delta) {
        remainingTime -= delta;
    }

    public Node getNode() {return node;}
    public String getEventType() {return eventType;}
    public float getRemainingTime() {return remainingTime;}
}
