package io.github.tihiforever;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Job {
    // job info
    private Node pickup;
    private Node dropoff;

    // distance stuff
    private float strDist;
    private float roadDist;
    private float actDist;

    // customer and driving stats
    private float patience;
    private float timeTaken;
    private int numCollisions;
    private float avrgSpeed;
    private float optimalRouteDeviation;

    // payment
    private float basePay;
    private float bonus;
    private float finalPay;

    // jounrny tracking
    private float previousX;
    private float previousY;
    private boolean trackingPosition;
    private boolean completed;

    // constants (still being adjusted)
    private final float patienceStart = 100f;
    private final float patienceLossTime = 0.5f;
    private final float patienceLossCollisions = 15f;

    private final float baseFare = 20f;
    private final float farePerRoadCell = 2f;

    private final float targetSpeed = 300f;

    private final float patienceBonus = 0.2f;
    private final float timeBonus = 0.2f;
    private final float speedBonus = 0.1f;
    private final float collisionPenalty = 0.1f;
    private final float routePenalty = 0.5f;

    public Job(Node pickup, Node dropoff, Graph graph, Pathfinder pathfinder, City city){
        this.pickup = pickup;
        this.dropoff = dropoff;

        patience = patienceStart;
        strDist = Vector2.dst(pickup.getX(), pickup.getY(), dropoff.getX(), dropoff.getY());

        ArrayList<Node> optimalPath = pathfinder.findPath(pickup, dropoff);

        float distance = 0f;

        for (int i = 0; i < optimalPath.size(); i++) {
            Node a = optimalPath.get(i);
            Node b = optimalPath.get(i+1);

           distance += graph.getDistance(a, b);
        }

        roadDist = Math.round(distance);

        basePay = baseFare + roadDist * farePerRoadCell;
        actDist = 0f;
        timeTaken = 0f;
        numCollisions = 0;
        avrgSpeed = 0f;
        optimalRouteDeviation = 0f;
        bonus = 0f;
        finalPay = 0f;
    }

    public void update(float delta, Taxi taxi, City city){
        if(completed) return;

        float currentX = taxi.getX() + taxi.getWidth() / 2f;
        float currentY = taxi.getY() + taxi.getHeight() / 2f;

        if(!trackingPosition){
            previousX = currentX;
            previousY = currentY;
            trackingPosition = true;
        } else {
            actDist += Vector2.dst(previousX, previousY, currentX, currentY);
            previousX = currentX;
            previousY = currentY;
        }

        timeTaken += delta;
        patience = Math.max(0f, patience - patienceLossTime * delta);
    }

    public void recordCollision(){
        if (completed) return;

        numCollisions++;
        patience = Math.max(0f, patience - patienceLossCollisions);
    }

    public void completedJob(){
        if (completed) return;

        completed = true;

        if (timeTaken > 0f) avrgSpeed = actDist/timeTaken;
        if (roadDist > 0) optimalRouteDeviation = Math.max(0f, (actDist - roadDist) / roadDist);

        float patienceScore = patience / patienceStart;
        float expectedTime = roadDist / targetSpeed;

        if (timeTaken > 0f && expectedTime > 0f) timeTaken = clamp(expectedTime / timeTaken, 0f, 1f);

        float speedScore = clamp(avrgSpeed / targetSpeed, 0f, 1f);

        float bonusMultiplier = patienceScore * patienceBonus + timeTaken * timeBonus + speedScore * speedBonus - numCollisions * collisionPenalty - optimalRouteDeviation * routePenalty;

        bonus = basePay * bonusMultiplier;

        bonus = Math.max(-basePay, bonus);
        finalPay = Math.max(0f, basePay + bonus);
    }

    private float clamp(float value, float min, float max){
        return Math.max(min, Math.min(max, value));
    }

    public Node getPickup() {return pickup;}
    public Node getDropoff() {return dropoff;}
    public float getPatience() {return patience;}
    public float getStrDist() {return strDist;}
    public float getRoadDist() {return roadDist;}
    public float getActDist() {return actDist;}
    public float getBasePay() {return basePay;}
    public float getTimeTaken() {return timeTaken;}
    public int getNumCollisions() {return numCollisions;}
    public float getAvrgSpeed() {return avrgSpeed;}
    public float getOptimalRouteDeviation() {return optimalRouteDeviation;}
    public float getBonus() {return bonus;}
    public float getFinalPay() {return finalPay;}
    public boolean isCompleted() {return completed;}
}
