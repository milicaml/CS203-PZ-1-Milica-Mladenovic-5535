package com.cs203pz1milicamladenovic5535.utill;

public class Vertex {
    public double x;
    public double y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex() {
        this(0, 0);
    }

    public Vertex(final Vertex v) {
        this(v.x, v.y);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double Distance(final Vertex v1, final Vertex v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
    }

    public static double Distance(final Vertex v1, double x, double y) {
        return Math.sqrt(Math.pow(v1.x - x, 2) + Math.pow(v1.y - y, 2));
    }

    public static double Distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}