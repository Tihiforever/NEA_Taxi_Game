package io.github.tihiforever;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class NPCManager {

    private static ArrayList<NPC> npcs;

    public NPCManager(Graph graph, Pathfinder pathfinder, int amount) {
        npcs = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            npcs.add(new NPC(graph, pathfinder));
        }
    }

    public void update(float dt) {
        for (NPC npc : npcs) {
            npc.update(dt);
        }
    }

    public void draw(ShapeRenderer sr) {
        for (NPC npc : npcs) {
            npc.draw(sr);
        }
    }

    public static ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public void setNpcs(ArrayList<NPC> npcs) {
        this.npcs = npcs;
    }
}
