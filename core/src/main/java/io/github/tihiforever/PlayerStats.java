package io.github.tihiforever;

public class PlayerStats {
    private float money;
    private float totalReputation;
    private int totalJobsCompleted;
    private float totalDistanceDriven;

    public void addMoney(float amount) {
        money += amount;
    }

    public void addJobCompleted(float patienceScore, float distance) {
        totalReputation += patienceScore;
        totalJobsCompleted++;
        totalDistanceDriven += distance;
    }

    public float getMoney() {return money;}
    public float getReputation() {
        if (totalJobsCompleted == 0) return 0;
        return (totalReputation / totalJobsCompleted) * 5;
    }
    public int getTotalJobsCompleted() {return totalJobsCompleted;}
    public float getTotalDistanceDriven() {return totalDistanceDriven;}
}
