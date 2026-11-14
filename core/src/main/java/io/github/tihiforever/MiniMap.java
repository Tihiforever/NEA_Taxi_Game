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
    private int visibleCells;

    public MiniMap(City city, Taxi taxi){
        this.city = city;
        this.taxi = taxi;
    }


    public void draw(ShapeRenderer sr, Job job, ArrayList<Node> path, Graph graph, Pathfinder pathfinder, boolean visulisationEnabled, Node endNode, Node startNode) {
        Cell[][] cells = city.getGrid();

        size = Gdx.graphics.getHeight() / 3f;

        float cellSize = size / 30f;

        if (visulisationEnabled) cellSize = size / cells.length;

        float startX = Gdx.graphics.getWidth() - size - margin;
        float startY = margin;

        int taxiRow = (int) ((taxi.getY() + taxi.getHeight() / 2f) / city.getCellSize());
        int taxiCol = (int) ((taxi.getX() + taxi.getWidth() / 2f) / city.getCellSize());

        int startRow = taxiRow - 15;
        int startCol = taxiCol - 15;

        if (visulisationEnabled) {
            startRow = 0;
            startCol = 0;
        }

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (!visulisationEnabled && (i >= 30 || j >= 30)) continue;
                int row = startRow + i;
                int col = startCol + j;

                if (row < 0 || row >= cells.length || col < 0 || col >= cells[0].length) continue;

                sr.setColor(getColor(cells[row][col].getType()));
                sr.rect(startX + j * cellSize, startY + i * cellSize, cellSize, cellSize);
            }
        }

        if (!visulisationEnabled) {
            sr.setColor(Color.GREEN);

            for (int i = 0; i < path.size() - 1; i++) {
                Node a = path.get(i);
                Node b = path.get(i + 1);

                int rowA = (int) (a.getY() / city.getCellSize());
                int colA = (int) (a.getX() / city.getCellSize());
                int rowB = (int) (b.getY() / city.getCellSize());
                int colB = (int) (b.getX() / city.getCellSize());

                if (rowA < startRow || rowA >= startRow + 30 || colA < startCol || colA >= startCol + 30 || rowB < startRow || rowB >= startRow + 30 || colB < startCol || colB >= startCol + 30) continue;

                sr.rectLine(startX + (a.getX() / city.getCellSize() - startCol) * cellSize, startY + (a.getY() / city.getCellSize() - startRow) * cellSize, startX + (b.getX() / city.getCellSize() - startCol) * cellSize, startY + (b.getY() / city.getCellSize() - startRow) * cellSize, cellSize * 0.5f);
            }

            if (job != null) {
                Node destination;

                if (job.isPickedUp()) destination = job.getDropoff();
                else destination = job.getPickup();

                int row = (int) (destination.getY() / city.getCellSize());
                int col = (int) (destination.getX() / city.getCellSize());

                if (row >= startRow && row < startRow + 30 && col >= startCol && col < startCol + 30) {
                    sr.setColor(Color.PINK);
                    sr.rect(startX + (col - startCol) * cellSize, startY + (row - startRow) * cellSize, cellSize, cellSize);
                }
            }

        } else {
            for (Edge edge : graph.getEdges()) {
                Node a = edge.getStart();
                Node b = edge.getEnd();

                Node child = null;

                if (a.getParent() == b) child = a;
                else if (b.getParent() == a) child = b;

                if (child == null) continue;

                int rowA = (int) (a.getY() / city.getCellSize());
                int colA = (int) (a.getX() / city.getCellSize());
                int rowB = (int) (b.getY() / city.getCellSize());
                int colB = (int) (b.getX() / city.getCellSize());

                if (rowA < startRow || rowA >= cells.length || colA < startCol || colA >= cells[0].length || rowB < startRow || rowB >= cells.length || colB < startCol || colB >= cells[0].length) continue;

                if (child.isClosed()) sr.setColor(Color.ORANGE);
                else if (child.isOpen()) sr.setColor(Color.SKY);
                else continue;

                sr.rectLine(startX + (colA - startCol) * cellSize, startY + (rowA - startRow) * cellSize, startX + (colB - startCol) * cellSize, startY + (rowB - startRow) * cellSize, cellSize * 0.5f);
            }

            for (Node node : graph.getNodes()) {
                if (!node.isClosed() && !node.isOpen()) continue;

                int row = (int) (node.getY() / city.getCellSize());
                int col = (int) (node.getX() / city.getCellSize());

                if (row < startRow || row >= cells.length || col < startCol || col >= cells[0].length) continue;

                if (node.isClosed()) sr.setColor(Color.ORANGE);
                else sr.setColor(Color.SKY);

                sr.rect(startX + (col - startCol) * cellSize, startY + (row - startRow) * cellSize, cellSize, cellSize);
            }

            if (pathfinder.isPathFound()) {
                ArrayList<Node> foundPath = pathfinder.getCurrentPath();
                sr.setColor(Color.CYAN);

                for (int i = 0; i < foundPath.size() - 1; i++) {
                    Node a = foundPath.get(i);
                    Node b = foundPath.get(i + 1);

                    int rowA = (int) (a.getY() / city.getCellSize());
                    int colA = (int) (a.getX() / city.getCellSize());
                    int rowB = (int) (b.getY() / city.getCellSize());
                    int colB = (int) (b.getX() / city.getCellSize());

                    if (rowA < startRow || rowA >= cells.length || colA < startCol || colA >= cells[0].length || rowB < startRow || rowB >= cells.length || colB < startCol || colB >= cells[0].length) continue;

                    sr.rectLine(startX + (colA - startCol) * cellSize, startY + (rowA - startRow) * cellSize, startX + (colB - startCol) * cellSize, startY + (rowB - startRow) * cellSize, cellSize * 0.6f);
                }
            }

            Node current = pathfinder.getCurrentNode();

            if (current != null && current != startNode && current != endNode) {
                int row = (int) (current.getY() / city.getCellSize());
                int col = (int) (current.getX() / city.getCellSize());

                if (row >= startRow && row < cells.length && col >= startCol && col < cells[0].length) {
                    sr.setColor(Color.MAGENTA);
                    sr.rect(startX + (col - startCol) * cellSize, startY + (row - startRow) * cellSize, cellSize, cellSize);
                }
            }

            if (startNode != null) {
                int row = (int) (startNode.getY() / city.getCellSize());
                int col = (int) (startNode.getX() / city.getCellSize());

                if (row >= startRow && row < cells.length && col >= startCol && col < cells[0].length) {
                    sr.setColor(Color.LIME);
                    sr.rect(startX + (col - startCol) * cellSize, startY + (row - startRow) * cellSize, cellSize, cellSize);
                }
            }

            if (endNode != null) {
                int row = (int) (endNode.getY() / city.getCellSize());
                int col = (int) (endNode.getX() / city.getCellSize());

                if (row >= startRow && row < cells.length && col >= startCol && col < cells[0].length) {
                    sr.setColor(Color.WHITE);
                    sr.rect(startX + (col - startCol) * cellSize, startY + (row - startRow) * cellSize, cellSize, cellSize);
                }
            }
        }

        if (!visulisationEnabled) {
            sr.setColor(Color.YELLOW);
            sr.rect(startX + 15 * cellSize, startY + 15 * cellSize, cellSize, cellSize);
        }
    }


    private Color getColor(tileTypes zone){
        if (zone == tileTypes.RESIDENTIAL) return Color.GREEN;
        if (zone == tileTypes.COMMERCIAL) return Color.BLUE;
        if (zone == tileTypes.INDUSTRIAL) return Color.RED;
        if (zone == tileTypes.ROAD) return Color.DARK_GRAY;
        return Color.BLACK;
    }
}
