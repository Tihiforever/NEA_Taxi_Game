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

        for (int row = 0; row < city.getMapSize(); row++) {
            for (int col = 0; col < city.getMapSize(); col++) {

                if (city.isBuilding(row, col)) {
                    continue;
                }

                int connections = 0;

                boolean north = false;
                boolean south = false;
                boolean east = false;
                boolean west = false;

                if (row > 0 && !city.isBuilding(row - 1, col)) {
                    north = true;
                    connections++;
                }
                if (row < city.getMapSize() - 1 && !city.isBuilding(row + 1, col)) {
                    south = true;
                    connections++;
                }
                if (col < city.getMapSize() - 1 && !city.isBuilding(row, col + 1)) {
                    east = true;
                    connections++;
                }
                if (col > 0 && !city.isBuilding(row, col - 1)) {
                    west = true;
                    connections++;
                }
                if (connections == 2) {
                    if ((north && south) || (east && west)) {
                        continue;
                    }
                }

                float x = col * city.getCellSize() * city.getScaleSize();
                float y = row * city.getCellSize() * city.getScaleSize();

                nodes.add(new Node(row, col, x, y));
            }
        }
    }

    public void generateEdges() {
        edges.clear();

        for (Node node : nodes) {
            int row = node.getRow();
            int col = node.getCol();

            for (int c = col + 1; c < city.getMapSize(); c++) {
                if (city.isBuilding(row, c)) break;

                Node end = getNodeAt(row, c);

                if (end != null) {
                    float distance = c - col;
                    edges.add(new Edge(node, end, distance));
                    break;
                }
            }

            for (int r = row + 1; r < city.getMapSize(); r++) {
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

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GOLD);

        for (Edge edge : edges) {
            shapeRenderer.line(edge.getStart().getX(), edge.getStart().getY(), edge.getEnd().getX(), edge.getEnd().getY());
        }

        shapeRenderer.setColor(Color.YELLOW);

        for (Node node : nodes) {
            shapeRenderer.circle(node.getX(), node.getY(), 10);
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
