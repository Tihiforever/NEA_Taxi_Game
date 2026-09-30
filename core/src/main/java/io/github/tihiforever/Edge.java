package io.github.tihiforever;

public class Edge {

    // the two nodes connected by the edge
    private Node start;
    private Node end;

    // the cost of travelling along the dge
    private float weight;

    public Edge(Node start, Node end, float weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Node getStart() {return start;}
    public void setStart(Node start) {this.start = start;}

    public float getWeight() {return weight;}
    public void setWeight(float weight) {this.weight = weight;}

    public Node getEnd() {return end;}
    public void setEnd(Node end) {this.end = end;}
}
