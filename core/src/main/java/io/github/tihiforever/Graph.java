package io.github.tihiforever;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Graph {

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private City city;

    public Graph(City city) {
        this.city = city;
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();

        generateNodes();
        generateEdges();
    }

    // Generates nodes at intersections and road ends
    public void generateNodes() {
        nodes.clear();
        Cell[][] grid = city.getGrid();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (!city.isRoad(row, col)) continue;

                int connections = 0;

                boolean north = city.isRoad(row - 1, col);
                boolean south = city.isRoad(row + 1, col);
                boolean east = city.isRoad(row, col + 1);
                boolean west = city.isRoad(row, col - 1);

                boolean vertical = north && south;
                boolean horizontal = east && west;

                if (north) connections++;
                if (south) connections++;
                if (east) connections++;
                if (west) connections++;

                if(connections == 2 && (vertical || horizontal)) continue;
                if (connections == 1) System.out.println("deadend at" + row + " " + col);

                float x = col * city.getCellSize() + city.getCellSize() / 2f;
                float y = row * city.getCellSize() + city.getCellSize() / 2f;

                nodes.add(new Node(row, col, x, y));
            }
        }
    }

    public void generateEdges() {
        edges.clear();
        Cell[][] grid = city.getGrid();

        for (Node node : nodes) {
            int row = node.getRow();
            int col = node.getCol();

            for (int c = col + 1; c < grid.length; c++) {
                if (city.isBuilding(row, c)) break;

                Node end = getNodeAt(row, c);

                if (end != null) {
                    float distance = c - col;
                    edges.add(new Edge(node, end, distance));
                    break;
                }
            }

            for (int r = row + 1; r < grid.length; r++) {
                if (city.isBuilding(r, col)) break;

                Node end = getNodeAt(r, col);

                if (end != null) {
                    float distance = r - row;
                    edges.add(new Edge(node, end, distance));
                    break;
                }
            }
        }
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(new Color(0.30f, 0.33f, 0.38f, 1f));

        for (Edge edge : edges) {
            sr.line(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());
        }

        sr.setColor(new Color(0.45f, 0.48f, 0.52f, 1f));

        for (Node node : nodes) {
            sr.circle(node.getX(), node.getY(), 6);
        }
    }

    public Node getNodeAt(int row, int col) {
        for (Node node : nodes) {
            if (node.getRow() == row && node.getCol() == col) return node;
        }

        return null;
    }

    public ArrayList<Node> getNeighbours(Node node) {
        ArrayList<Node> neighbours = new ArrayList<Node>();

        for (Edge edge : edges) {
            if (edge.getStart() == node) neighbours.add(edge.getEnd());
            if (edge.getEnd() == node) neighbours.add(edge.getStart());
        }

        return neighbours;
    }

    public float getDistance(Node a, Node b) {
        for (Edge edge : edges) {
            if (edge.getStart() == a && edge.getEnd() == b) return edge.getWeight();
            if (edge.getStart() == b && edge.getEnd() == a) return edge.getWeight();
        }

        return Float.MAX_VALUE;
    }

    public Node getClosestNode(float x, float y) {
        Node closest = null;
        float shortestDistance = Float.MAX_VALUE;

        for (Node node : nodes) {
            float distance = Vector2.dst(x, y, node.getX(), node.getY());

            if (distance < shortestDistance) {
                shortestDistance = distance;
                closest = node;
            }
        }

        return closest;
    }

    public ArrayList<Node> getNodes() {return nodes;}
    public ArrayList<Edge> getEdges() {return edges;}
}
