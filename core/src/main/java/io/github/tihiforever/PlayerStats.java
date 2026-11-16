package io.github.tihiforever;

public class PlayerStats {
    private float money;
    private float totalMoney;
    private float totalReputation;
    private int totalJobsCompleted;
    private float totalDistanceDriven;
    private int numAchivements = 0;
    private boolean[] achievements = new boolean[10];
    private int nightJobsCompleted;
    private int cleanJobsCompleted;

    public void addMoney(float amount) {
        money += amount;
        if (amount > 0) totalMoney += amount;
    }

    public void addJobCompleted(float patienceScore, float distance, int numCollsisons, float time) {
        totalReputation += patienceScore;
        totalJobsCompleted++;
        totalDistanceDriven += distance;
        checkAchivements(numCollsisons, time);
    }

    public void checkAchivements(int numCollisions, float time){
        if (totalJobsCompleted >= 1 && !achievements[0]) {
            achievements[0] = true;
            numAchivements++;
            addMoney(50);
        }
    }

    public float getMoney() {return money;}
    public float getReputation() {
        if (totalJobsCompleted == 0) return 0;
        return (totalReputation / totalJobsCompleted) * 5;
    }
    public int getTotalJobsCompleted() {return totalJobsCompleted;}
    public float getTotalDistanceDriven() {return totalDistanceDriven;}
    public float getTotalMoney() {return totalMoney;}
    public int getNumAchivements() {return numAchivements;}
}
