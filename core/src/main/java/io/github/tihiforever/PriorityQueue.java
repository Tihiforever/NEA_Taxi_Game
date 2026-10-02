package io.github.tihiforever;

import java.util.ArrayList;

public class PriorityQueue {
    private static class PlayerData {
        String name;
        int score;

        PlayerData(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    private ArrayList<PlayerData> elements = new ArrayList<PlayerData>();
    private int size;

    public boolean isEmpty(){
        return size ==0;
    }

    public int length(){
        return size;
    }

    public void push(String name, int score){
        PlayerData newPlayerData = new PlayerData(name, score);

        int position =0;
        while (position < elements.size() && elements.get(position).score >= score){
            position++;
        }

        elements.add(position, newPlayerData);
        size++;
    }

    public PlayerData pop(){
        if(isEmpty()){
            return null;
        }

        PlayerData removedData = elements.remove(0);
        size--;

        return removedData;
    }

    public String getName(PlayerData dataEntry){return dataEntry.name;}

    public int getScore(PlayerData dataEntry){return dataEntry.score;}
}
