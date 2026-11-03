package io.github.tihiforever;

import java.util.ArrayList;
import java.util.Random;

public class JobManager {
    private ArrayList<Job> jobs;
    private Job actvJob;
    private Random random = new Random();

    public JobManager(Graph graph, Pathfinder pathfinder, int amount) {
        jobs = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            addJob(graph, pathfinder);
        }
    }

    private void addJob(Graph graph, Pathfinder pathfinder) {
        ArrayList<Node> availableNodes = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            if (!node.isHasJob()) {
                availableNodes.add(node);
            }
        }

        Node pickup = availableNodes.remove(random.nextInt(availableNodes.size()));
        Node dropoff = availableNodes.get(random.nextInt(availableNodes.size()));

        jobs.add(new Job(pickup, dropoff, graph, pathfinder));
    }

    public boolean acceptJob(int index) {
        if (actvJob != null || index < 0 || index >= jobs.size()) return false;

        actvJob = jobs.get(index);
        return true;
    }

    public void update(float dt, Taxi taxi, City city) {
        if (actvJob == null) return;

        actvJob.update(dt, taxi, city);

        if (actvJob.isCompleted()) {
            jobs.remove(actvJob);
            actvJob = null;
        }
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public Job getActvJob() {
        return actvJob;
    }
}
