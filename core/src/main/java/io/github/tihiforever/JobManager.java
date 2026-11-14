
package io.github.tihiforever;

import java.util.ArrayList;
import java.util.Random;

public class JobManager {
    private ArrayList<Job> jobs;
    private Job actvJob;
    private Job extraJob;
    private Random random = new Random();

    private Graph graph;
    private Pathfinder pathfinder;
    private UpgradeManager upgradeManager;

    private int baseAmount;
    private int nextJobNumber = 1;

    public JobManager(Graph graph, Pathfinder pathfinder, UpgradeManager upgradeManager, int amount) {
        this.graph = graph;
        this.pathfinder = pathfinder;
        this.baseAmount = amount;
        this.upgradeManager = upgradeManager;
        jobs = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            addJob();
        }
    }

    private void addJob() {
        ArrayList<Node> availableNodes = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            if (!node.isHasJob()) {
                availableNodes.add(node);
            }
        }

        if (availableNodes.size() < 2) return;

        Node pickup = availableNodes.remove(random.nextInt(availableNodes.size()));
        Node dropoff = availableNodes.get(random.nextInt(availableNodes.size()));

        if (upgradeManager.hasUpgrade(Upgrades.DISPATCH2) && random.nextFloat() < 0.1f) {
            for (Node node : availableNodes) {
                if (pathfinder.findPath(pickup, node).size() > pathfinder.findPath(pickup, dropoff).size()) dropoff = node;
            }
        }

        float fareMultiplier = 1f;

        if (upgradeManager.hasUpgrade(Upgrades.FARES1)) fareMultiplier += 0.10f;
        if (upgradeManager.hasUpgrade(Upgrades.FARES2)) fareMultiplier += 0.10f;
        if (upgradeManager.hasUpgrade(Upgrades.FARES3)) fareMultiplier += 0.15f;

        Job job = new Job(pickup, dropoff, graph, pathfinder, fareMultiplier, upgradeManager);
        job.setJobNum(nextJobNumber);
        nextJobNumber++;
        jobs.add(job);

    }

    private void generateJobs(PlayerStats playerStats) {
        int targetAmount = baseAmount + (int)(playerStats.getReputation() * 2);

        if (upgradeManager.hasUpgrade(Upgrades.DISPATCH1)) targetAmount += 5;

        int availableJobs = jobs.size();
        if (actvJob != null) availableJobs--;
        if (extraJob != null) availableJobs--;

        while (availableJobs < targetAmount) {
            int oldSize = jobs.size();
            addJob();

            if (jobs.size() == oldSize) break;

            availableJobs++;
        }
    }

    public boolean acceptJob(Job job) {
        if (!jobs.contains(job)) return false;

        if (actvJob == null) {
            actvJob = job;
            return true;
        }

        if (upgradeManager.hasUpgrade(Upgrades.PASSENGERS1) && extraJob == null && job != actvJob) {
            extraJob = job;
            return true;
        }

        return false;
    }

    public boolean acceptJob(int index) {
        if (index < 0 || index >= jobs.size()) return false;
        return acceptJob(jobs.get(index));
    }

    public void update(float dt, Taxi taxi, City city, PlayerStats playerStats) {
        if (actvJob != null) {
            actvJob.update(dt, taxi, city);

            if (actvJob.isCompleted()) {
                actvJob.getPickup().setHasJob(false);
                actvJob.getDropoff().setHasJob(false);
                jobs.remove(actvJob);
                actvJob = extraJob;
                extraJob = null;
            }
        }
        if (extraJob != null) {
            extraJob.update(dt, taxi, city);

            if (extraJob.isCompleted()) {
                extraJob.getPickup().setHasJob(false);
                extraJob.getDropoff().setHasJob(false);
                jobs.remove(extraJob);
                extraJob = null;
            }
        }

        generateJobs(playerStats);
    }

    public ArrayList<Job> getJobs() {return jobs;}
    public Job getActvJob() {return actvJob;}
    public Job getExtraJob() {return extraJob;}
}
