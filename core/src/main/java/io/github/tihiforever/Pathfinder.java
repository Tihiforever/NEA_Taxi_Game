
package io.github.tihiforever;

import java.util.ArrayList;

public class Pathfinder {
    private Graph graph;

    private ArrayList<Node> open = new ArrayList<>();
    private ArrayList<Node> currentPath = new ArrayList<>();

    private Node start;
    private Node end;

    private boolean searchFinished;
    private boolean pathFound;


    public Pathfinder(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Node> findPath(Node start, Node end) {
        ArrayList<Node> path = new ArrayList<Node>();

        if (start == null || end == null) return path;

        if (start == end) {
            path.add(start);
            return path;
        }

        for (Node node : graph.getNodes()) {
            node.setParent(null);
            node.setgCost(Float.MAX_VALUE);
            node.sethCost(0);
            node.setfCost(Float.MAX_VALUE);
            node.setOpen(false);
            node.setClosed(false);
        }

        ArrayList<Node> open = new ArrayList<Node>();

        start.setgCost(0);
        start.sethCost(heuristic(start, end));
        start.setfCost(start.gethCost());
        start.setOpen(true);

        open.add(start);

        boolean foundPath = false;

        while (!open.isEmpty()) {
            Node current = open.get(0);

            for (Node node : open) {
                if (node.getfCost() < current.getfCost()) current = node;
            }

            if (current == end) {
                foundPath = true;
                break;
            }

            open.remove(current);
            current.setOpen(false);
            current.setClosed(true);

            for (Node neighbour : graph.getNeighbours(current)) {
                if (neighbour.isClosed()) continue;

                float distance = graph.getDistance(current, neighbour);
                float tempG = current.getgCost() + distance;

                if (tempG < neighbour.getgCost()) {
                    neighbour.setParent(current);
                    neighbour.setgCost(tempG);
                    neighbour.sethCost(heuristic(neighbour, end));
                    neighbour.setfCost(neighbour.getgCost() + neighbour.gethCost());

                    if (!neighbour.isOpen()) {
                        open.add(neighbour);
                        neighbour.setOpen(true);
                    }
                }
            }
        }

        if (!foundPath) return path;

        Node current = end;

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        int left = 0;
        int right = path.size() - 1;

        while (left < right) {
            Node temp = path.get(left);
            path.set(left, path.get(right));
            path.set(right, temp);

            left++;
            right--;
        }

        return path;
    }

    private float heuristic(Node a, Node b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }


    public void startSearch(Node start, Node end) {
        this.start = start;
        this.end = end;

        open.clear();
        currentPath.clear();

        searchFinished = false;
        pathFound = false;

        for (Node node : graph.getNodes()) {
            node.setParent(null);
            node.setgCost(Float.MAX_VALUE);
            node.sethCost(0);
            node.setfCost(Float.MAX_VALUE);
            node.setOpen(false);
            node.setClosed(false);
        }

        if (start == null || end == null) {
            searchFinished = true;
            return;
        }

        start.setgCost(0);
        start.sethCost(heuristic(start, end));
        start.setfCost(start.gethCost());
        start.setOpen(true);

        open.add(start);
    }

    public void stepSearch() {
        if (searchFinished) return;

        if (open.isEmpty()) {
            searchFinished = true;
            return;
        }

        Node current = open.get(0);

        for (Node node : open) {
            if (node.getfCost() < current.getfCost()) {
                current = node;
            }
        }

        open.remove(current);
        current.setOpen(false);
        current.setClosed(true);

        if (current == end) {
            pathFound = true;
            searchFinished = true;
            buildCurrentPath();
            return;
        }

        for (Node neighbour : graph.getNeighbours(current)) {
            if (neighbour.isClosed()) continue;

            float distance = graph.getDistance(current, neighbour);

            if (distance == Float.MAX_VALUE) continue;

            float newG = current.getgCost() + distance;

            if (newG < neighbour.getgCost()) {
                neighbour.setParent(current);
                neighbour.setgCost(newG);
                neighbour.sethCost(heuristic(neighbour, end));
                neighbour.setfCost(newG + neighbour.gethCost());

                if (!neighbour.isOpen()) {
                    open.add(neighbour);
                    neighbour.setOpen(true);
                }
            }
        }
    }

    private void buildCurrentPath() {
        currentPath.clear();

        Node current = end;

        while (current != null) {
            currentPath.add(current);
            current = current.getParent();
        }

        java.util.Collections.reverse(currentPath);
    }

    public ArrayList<Node> getCurrentPath(){return currentPath;}
    public boolean isSearchFinished(){return searchFinished;}
    public boolean isPathFound(){return pathFound;}

}
