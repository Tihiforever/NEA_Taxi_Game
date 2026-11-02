package io.github.tihiforever;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class JobManager {

    private static ArrayList<Job> jobs;

    public JobManager(Node pickup, Node dropoff, Graph graph, Pathfinder pathfinder, int amount) {
        jobs = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            jobs.add(new Job(pickup, dropoff, graph, pathfinder));
        }
    }

    public void update(float dt, Taxi taxi, City city) {
        for (Job job : jobs) {
            job.update(dt, taxi, city);
        }
    }

    public static ArrayList<Job> getJobs() {return jobs;}
    public void setNpcs(ArrayList<Job> jobs) {this.jobs = jobs;}
}

