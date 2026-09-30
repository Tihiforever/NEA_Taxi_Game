package io.github.tihiforever;

public class Node {

    // grid position of node
    private int row;
    private int col;

    //screen position
    private float x;
    private float y;

    // A* stuff
    private float gCost;
    private float hCost;
    private float fCost;

    private Node parent;

    public boolean open;
    public boolean closed;

    public Node(int row, int col, float x, float y) {
        this.row = row;
        this.col = col;
        this.x = x;
        this.y = y;
    }

    public int getRow() {return row;}
    public void setRow(int row) {this.row = row;}

    public int getCol() {return col;}
    public void setCol(int col) {this.col = col;}

    public float getX() {return x;}
    public void setX(float x) {this.x = x;}

    public float getY() {return y;}
    public void setY(float y) {this.y = y;}

    public float getgCost() {return gCost;}
    public void setgCost(float gCost) {this.gCost = gCost;}

    public float gethCost() {return hCost;}
    public void sethCost(float hCost) {this.hCost = hCost;}

    public float getfCost() {return fCost;}
    public void setfCost(float fCost) {this.fCost = fCost;}

    public Node getParent() {return parent;}
    public void setParent(Node parent) {this.parent = parent;}

    public boolean isOpen() {return open;}
    public void setOpen(boolean open) {this.open = open;}

    public boolean isClosed() {return closed;}
    public void setClosed(boolean closed) {this.closed = closed;}
}
