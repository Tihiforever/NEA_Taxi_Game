package io.github.tihiforever;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;

public class City {
    private final Random random;

    private Cell[][] grid;
    private Cell[][] newGrid;

    private int mapSize;
    private int cellSize;
    private int scaleSize;

    public City(){
        mapSize = 25;
        cellSize = 192;
        scaleSize = 2;

        random = new Random();
        generateCity();
    }

    public void generateCity(){
        grid = new Cell[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            grid[i] = new Cell[mapSize];
            for (int j = 0; j < mapSize; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }

        ArrayList<Cell> checkOrder = getAllCells();
        Collections.shuffle(checkOrder);

        for (int id = 0; id < checkOrder.size(); id++) {
            Cell currentTile = checkOrder.get(id);

            if (currentTile.getType() == tileTypes.NONE){
                int width;
                int height;

                if(random.nextBoolean()){
                    width = random.nextInt(2,5);
                    height = 2;
                } else {
                    width = 2;
                    height = random.nextInt(2,5);
                }

                tileTypes[] zones = {tileTypes.COMMERCIAL, tileTypes.INDUSTRIAL, tileTypes.RESIDENTIAL};
                tileTypes zone = zones[random.nextInt(0, zones.length)];

                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        if (isInBounds(currentTile.getI() + i, currentTile.getJ() + j)) {
                            grid[currentTile.getI() + i][currentTile.getJ() + j].setId(id);
                            grid[currentTile.getI() + i][currentTile.getJ() + j].setType(zone);
                        }
                    }
                }
            }
        }

        newGrid = new Cell[mapSize * scaleSize][mapSize * scaleSize];

        for (int i = 0; i < mapSize * scaleSize; i++) {
            for (int j = 0; j < mapSize * scaleSize; j++) {
                newGrid[i][j] = new Cell(i, j);

                Cell originalCell = grid[i / scaleSize][j / scaleSize];

                newGrid[i][j].setId(originalCell.getId());
                newGrid[i][j].setType(originalCell.getType());
            }
        }

        for (int i = 0; i < mapSize * scaleSize; i++) {
            for (int j = 0; j < mapSize * scaleSize; j++) {
                checkIfRoad(i, j);
            }
        }

    }

    public void draw(ShapeRenderer sr) {
        for (int i = 0; i < newGrid.length; i++) {
            for (int j = 0; j < newGrid[i].length; j++) {
                sr.setColor(getColor(newGrid[i][j].getType()));
                sr.rect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    private ArrayList<Cell> getAllCells(){
        ArrayList<Cell> cells = new ArrayList<>(mapSize^2);
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                cells.add(grid[i][j]);
            }
        }

        return cells;
    }

    private boolean isInBounds(int i, int j){return i >= 0 && i< mapSize && j >= 0 && j < mapSize;}
    private boolean isInBoundsScaled(int i, int j){return i >= 0 && i< mapSize * scaleSize && j >= 0 && j < mapSize * scaleSize;}

    public boolean isBuilding(int row, int col) {
        if (row < 0 || row >= newGrid.length || col < 0 || col >= newGrid[0].length) return true;
        tileTypes type = newGrid[row][col].getType();
        return type == tileTypes.RESIDENTIAL || type == tileTypes.COMMERCIAL || type == tileTypes.INDUSTRIAL;
    }

    public boolean isRoad(int row, int col) {
        if (row < 0 || row >= newGrid.length || col < 0 || col >= newGrid[0].length) return false;
        return newGrid[row][col].getType() == tileTypes.ROAD;
    }

    private void checkIfRoad(int i, int j){
        if (isInBoundsScaled(i + 1, j) && newGrid[i + 1][j].getId() != newGrid[i][j].getId()){
            newGrid[i][j].setType(tileTypes.ROAD);
        }
        if (isInBoundsScaled(i, j + 1) && newGrid[i][j + 1].getId() != newGrid[i][j].getId()){
            newGrid[i][j].setType(tileTypes.ROAD);
        }
    }

    private Color getColor(tileTypes zone){
        if (zone == tileTypes.RESIDENTIAL) return Color.GREEN;
        if (zone == tileTypes.COMMERCIAL) return Color.BLUE;
        if (zone == tileTypes.INDUSTRIAL) return Color.RED;
        if (zone == tileTypes.ROAD) return Color.DARK_GRAY;
        return Color.BLACK;
    }

    public int getWorldWidth(){return mapSize * scaleSize * cellSize;}
    public int getWorldHeight(){return mapSize * scaleSize * cellSize;}
    public int getCellSize(){return cellSize;}
    public Cell[][] getGrid(){return newGrid;}
    public int getMapSize(){return mapSize;}
    public int getScaleSize(){return scaleSize;}
}
