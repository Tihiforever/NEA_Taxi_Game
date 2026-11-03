package io.github.tihiforever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class MiniMap {
    private final City city;
    private Taxi taxi;

    private float size;
    private float margin = 20f;

    public MiniMap(City city, Taxi taxi){
        this.city = city;
        this.taxi = taxi;
    }

    public void draw(ShapeRenderer sr, Job job, ArrayList<Node> path, Graph graph, Pathfinder pathfinder, boolean visulisationEnabled, Node endNode, Node startNode){
        size = Gdx.graphics.getHeight() / 3f;

        Cell[][] cells = city.getGrid();

        float cellSize = size / cells[0].length;

        float startX = Gdx.graphics.getWidth() - size - margin;
        float startY = margin;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                sr.setColor(getColor(cells[i][j].getType()));
                sr.rect(startX + j * cellSize, startY + i * cellSize, cellSize, cellSize);

            }
        }

        if (!visulisationEnabled ) {
            sr.setColor(Color.GREEN);

            for (int i = 0; i < path.size() - 1; i++) {
                Node a = path.get(i);
                Node b = path.get(i + 1);

                float scaleX = size / city.getWorldWidth();
                float scaleY = size / city.getWorldHeight();

                sr.rectLine(startX + a.getX() * scaleX, startY + a.getY() * scaleY, startX + b.getX() * scaleX, startY + b.getY() * scaleY, cellSize * 0.5f);
            }

            if (job != null) {
                Node destination;

                if (job.isPickedUp()) destination = job.getDropoff();
                else destination = job.getPickup();

                int row = (int) (destination.getY() / city.getCellSize());
                int col = (int) (destination.getX() / city.getCellSize());

                sr.setColor(Color.PINK);
                sr.rect(startX + col * cellSize, startY + row * cellSize, cellSize, cellSize);

            }
        } else {
            for (Edge edge : graph.getEdges()) {
                Node a = edge.getStart();
                Node b = edge.getEnd();

                Node child = null;

                if (a.getParent() == b) child = a;
                else if (b.getParent() == a) child = b;

                if (child == null) continue;

                if (child.isClosed()) sr.setColor(Color.ORANGE);
                else if (child.isOpen()) sr.setColor(Color.SKY);
                else continue;

                sr.rectLine(startX + a.getX() / city.getWorldWidth() * size, startY + a.getY() / city.getWorldHeight() * size, startX + b.getX() / city.getWorldWidth() * size, startY + b.getY() / city.getWorldHeight() * size, cellSize * 0.5f);
            }


            for (Node node : graph.getNodes()) {
                if (!node.isClosed() && !node.isOpen()) continue;

                int row = (int) (node.getY() / city.getCellSize());
                int col = (int) (node.getX() / city.getCellSize());

                if (node.isClosed()) sr.setColor(Color.ORANGE);
                else sr.setColor(Color.SKY);

                sr.rect(startX + col * cellSize, startY + row * cellSize, cellSize, cellSize);
            }

            if (pathfinder.isPathFound()) {
                ArrayList<Node> foundPath = pathfinder.getCurrentPath();

                sr.setColor(Color.CYAN);

                for (int i = 0; i < foundPath.size() - 1; i++) {
                    Node a = foundPath.get(i);
                    Node b = foundPath.get(i + 1);

                    sr.rectLine(startX + a.getX() / city.getWorldWidth() * size, startY + a.getY() / city.getWorldHeight() * size, startX + b.getX() / city.getWorldWidth() * size, startY + b.getY() / city.getWorldHeight() * size, cellSize * 0.6f);
                }
            }

            Node current = pathfinder.getCurrentNode();

            if (current != null && current != startNode && current != endNode) {
                int row = (int) (current.getY() / city.getCellSize());
                int col = (int) (current.getX() / city.getCellSize());

                sr.setColor(Color.MAGENTA);
                sr.rect(startX + col * cellSize, startY + row * cellSize, cellSize, cellSize);
            }

            if (startNode != null) {
                int row = (int) (startNode.getY() / city.getCellSize());
                int col = (int) (startNode.getX() / city.getCellSize());

                sr.setColor(Color.LIME);
                sr.rect(startX + col * cellSize, startY + row * cellSize, cellSize, cellSize);
            }

            if (endNode != null) {
                int row = (int) (endNode.getY() / city.getCellSize());
                int col = (int) (endNode.getX() / city.getCellSize());

                sr.setColor(Color.WHITE);
                sr.rect(startX + col * cellSize, startY + row * cellSize, cellSize, cellSize);
            }
        }

        int taxiRow = (int) ((taxi.getY() + taxi.getHeight() / 2f) / city.getCellSize());
        int taxiCol = (int) ((taxi.getX() + taxi.getWidth() / 2f) / city.getCellSize());

        sr.setColor(Color.YELLOW);
        sr.rect(startX + taxiCol * cellSize, startY + taxiRow * cellSize, cellSize, cellSize);
    }

    private Color getColor(tileTypes zone){
        if (zone == tileTypes.RESIDENTIAL) return Color.GREEN;
        if (zone == tileTypes.COMMERCIAL) return Color.BLUE;
        if (zone == tileTypes.INDUSTRIAL) return Color.RED;
        if (zone == tileTypes.ROAD) return Color.DARK_GRAY;
        return Color.BLACK;
    }
}
