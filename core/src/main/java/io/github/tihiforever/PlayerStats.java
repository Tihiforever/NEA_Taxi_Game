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

    public void addJobCompleted(float patienceScore, float distance, int numCollsisons, float time, UpgradeManager upgradeManager) {
        totalReputation += patienceScore;
        totalJobsCompleted++;
        totalDistanceDriven += distance;
        checkAchivements(numCollsisons, time, upgradeManager);
    }

    public void checkAchivements(int numCollisions, float time, UpgradeManager upgradeManager){
        if (totalJobsCompleted >= 1 && !achievements[0]) {
            achievements[0] = true;
            numAchivements++;
            addMoney(50);
        }
        if (totalDistanceDriven >= 1000000 && !achievements[1]) {
            achievements[1] = true;
            numAchivements++;
            addMoney(10000);
        }
        if(totalMoney >= 200 && !achievements[2]){
            achievements[2] = true;
            numAchivements++;
            addMoney(200);
        }
        if(totalJobsCompleted >= 100 && !achievements[3]){
            achievements[3] = true;
            numAchivements++;
            addMoney(1000);
        }
        if (upgradeManager.hasAllUpgrades() && !achievements[4]){
            achievements[4] = true;
            numAchivements++;
            addMoney(5000);
        }
        if (nightJobsCompleted >= 10 && !achievements[5]){
            achievements[5] = true;
            numAchivements++;
            addMoney(200);
        }
        if (totalMoney >= 10000 && !achievements[6]){
            achievements[6] = true;
            numAchivements++;
            addMoney(1000);
        }
        if (getReputation() >= 4.5f && !achievements[7]){
            achievements[7] = true;
            numAchivements++;
            addMoney(500);
        }
        if (totalJobsCompleted >= 25 && !achievements[8]){
            achievements[8] = true;
            numAchivements++;
            addMoney(100);
        }
        if (cleanJobsCompleted >= 10 && !achievements[9]){
            achievements[9] = true;
            numAchivements++;
            addMoney(800);
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

    public void setNightJobsCompleted(int nightJobsCompleted) {this.nightJobsCompleted = nightJobsCompleted;}
    public int getCleanJobsCompleted() {return cleanJobsCompleted;}
    public int getNightJobsCompleted() {return nightJobsCompleted;}
    public void setCleanJobsCompleted(int cleanJobsCompleted) {this.cleanJobsCompleted = cleanJobsCompleted;}
}
