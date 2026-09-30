package io.github.tihiforever;

public class Cell {

    // grid position
    private int i;
    private int j;

    // tile type and connected roadnetwork id
    private tileTypes type;
    public int id;

    public Cell(int i, int j) {
        this.type = tileTypes.NONE;
        this.i = i;
        this.j = j;
        this.id = -1;
    }

    public int getI() {return i;}
    public void setI(int i) {this.i = i;}

    public int getJ() {return j;}
    public void setJ(int j) {this.j = j;}

    public tileTypes getType() {return type;}
    public void setType(tileTypes type) {this.type = type;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
}
