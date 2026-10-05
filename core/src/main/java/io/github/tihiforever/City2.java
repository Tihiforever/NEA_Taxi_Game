package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class City2 {

    private Cell[][] grid;
    private Cell[][] newGrid;

    private int mapSize = 25;
    private int cellSize = 192;
    private int scaleSize = 2;

    public City2() {
        generateCity();
    }

    private float randomRange(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    private List<Cell> getAllCells() {
        List<Cell> cells = new ArrayList<>(mapSize * mapSize);
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                cells.add(grid[i][j]);
            }
        }
        return cells;
    }

    private boolean isInBounds(int i, int j) {
        return i >= 0 && i < mapSize && j >= 0 && j < mapSize;
    }

    private boolean isInBoundsScaled(int i, int j) {
        int scaledSize = mapSize * scaleSize;
        return i >= 0 && i < scaledSize && j >= 0 && j < scaledSize;
    }

    // Performs symmetrical road conversion
    private void checkIfRoad(int i, int j) {
        // Check Right neighbor
        if (isInBoundsScaled(i + 1, j) && newGrid[i + 1][j].id != newGrid[i][j].id) {
            newGrid[i][j].type = tileTypes.ROAD;
        }
        // Check Top neighbor
        if (isInBoundsScaled(i, j + 1) && newGrid[i][j + 1].id != newGrid[i][j].id) {
            newGrid[i][j].type = tileTypes.ROAD;
        }
    }

    public void generateCity() {
        grid = new Cell[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            grid[i] = new Cell[mapSize];
            for (int j = 0; j < mapSize; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }

        List<Cell> checkOrder = getAllCells();
        Collections.shuffle(checkOrder); // Fast, native O(N) shuffling

        for (int id = 1; id < checkOrder.size(); id++) {
            Cell curTile = checkOrder.get(id);

            if (curTile.type == tileTypes.NONE) {
                int direction = Math.random() > 0.5 ? 1 : 0;
                int squareWidth = (direction == 1) ? (int) randomRange(2, 5) : 2;
                int squareHeight = (direction == 1) ? 2 : (int) randomRange(2, 5);

                tileTypes[] zones = {
                    tileTypes.COMMERCIAL,
                    tileTypes.RESIDENTIAL,
                    tileTypes.INDUSTRIAL
                };
                tileTypes zone = zones[(int) (Math.random() * zones.length)];

                for (int x = 0; x < squareWidth; x++) {
                    for (int y = 0; y < squareHeight; y++) {
                        if (isInBounds(curTile.i + x, curTile.j + y)) {
                            grid[curTile.i + x][curTile.j + y].id = id;
                            grid[curTile.i + x][curTile.j + y].type = zone;
                        }
                    }
                }
            }
        }

        int scaledDimensions = mapSize * scaleSize;
        newGrid = new Cell[scaledDimensions][scaledDimensions];

        // Map old grid directly into scaled grid
        for (int i = 0; i < scaledDimensions; i++) {
            newGrid[i] = new Cell[scaledDimensions];
            for (int j = 0; j < scaledDimensions; j++) {
                newGrid[i][j] = new Cell(i, j);

                int sourceI = i / scaleSize;
                int sourceJ = j / scaleSize;

                newGrid[i][j].id = grid[sourceI][sourceJ].id;
                newGrid[i][j].type = grid[sourceI][sourceJ].type;
            }
        }

        // Apply road pass
        for (int i = 0; i < scaledDimensions; i++) {
            for (int j = 0; j < scaledDimensions; j++) {
                checkIfRoad(i, j);
            }
        }
    }

    public void draw(ShapeRenderer sr) {
        int scaledDimensions = mapSize * scaleSize;
        for (int i = 0; i < scaledDimensions; i++) {
            for (int j = 0; j < scaledDimensions; j++) {
                sr.setColor(getColor(newGrid[i][j].type));
                // i rules Y-axis, j rules X-axis
                sr.rect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    private Color getColor(tileTypes zone) {
        if (zone == tileTypes.RESIDENTIAL) return Color.valueOf("#00FF00");
        if (zone == tileTypes.COMMERCIAL) return Color.valueOf("#0000FF");
        if (zone == tileTypes.INDUSTRIAL) return Color.valueOf("#FF0000");
        if (zone == tileTypes.ROAD) return Color.valueOf("#303030");
        return Color.BLACK;
    }

    public int getWorldWidth() { return mapSize * scaleSize * cellSize; }
    public int getWorldHeight() { return mapSize * scaleSize * cellSize; }
    public int getCellSize() { return cellSize; }
    public Cell[][] getGrid() { return newGrid; }
    public int getMapSize() { return mapSize; }

    public boolean isBuilding(int row, int col) {
        if (row < 0 || row >= newGrid.length || col < 0 || col >= newGrid[0].length) {
            return true;
        }
        tileTypes type = newGrid[row][col].type;
        return type == tileTypes.RESIDENTIAL || type == tileTypes.COMMERCIAL || type == tileTypes.INDUSTRIAL;
    }
}

