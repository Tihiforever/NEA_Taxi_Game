
package io.github.tihiforever;

import java.util.ArrayList;
import java.util.Random;

public class JobManager {
    private ArrayList<Job> jobs;
    private Job actvJob;
    private Random random = new Random();

    private Graph graph;
    private Pathfinder pathfinder;
    private int baseAmount;

    public JobManager(Graph graph, Pathfinder pathfinder, int amount) {
        this.graph = graph;
        this.pathfinder = pathfinder;
        this.baseAmount = amount;
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

        jobs.add(new Job(pickup, dropoff, graph, pathfinder));
    }

    private void generateJobs(PlayerStats playerStats) {
        int targetAmount = baseAmount + (int)(playerStats.getReputation() * 2);

        int availableJobs = jobs.size();
        if (actvJob != null) availableJobs--;

        while (availableJobs < targetAmount) {
            int oldSize = jobs.size();
            addJob();

            if (jobs.size() == oldSize) break;

            availableJobs++;
        }
    }

    public boolean acceptJob(Job job) {
        if (actvJob != null || !jobs.contains(job)) return false;

        actvJob = job;
        return true;
    }

    public boolean acceptJob(int index) {
        if (actvJob != null || index < 0 || index >= jobs.size()) return false;

        actvJob = jobs.get(index);
        return true;
    }

    public void update(float dt, Taxi taxi, City city, PlayerStats playerStats) {
        if (actvJob != null) {
            actvJob.update(dt, taxi, city);

            if (actvJob.isCompleted()) {
                actvJob.getPickup().setHasJob(false);
                actvJob.getDropoff().setHasJob(false);
                jobs.remove(actvJob);
                actvJob = null;
            }
        }

        generateJobs(playerStats);
    }

    public ArrayList<Job> getJobs() {return jobs;}
    public Job getActvJob() {return actvJob;}
}
