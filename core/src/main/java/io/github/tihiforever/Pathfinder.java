
package io.github.tihiforever;

import java.util.ArrayList;

public class Pathfinder {

    private Graph graph;

    public Pathfinder(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Node> findPath(Node start, Node end) {
        ArrayList<Node> path = new ArrayList<Node>();

        if (start == null || end == null) return path;

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
}
