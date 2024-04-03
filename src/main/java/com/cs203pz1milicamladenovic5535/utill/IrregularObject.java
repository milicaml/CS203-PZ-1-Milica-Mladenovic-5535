package com.cs203pz1milicamladenovic5535.utill;

import com.cs203pz1milicamladenovic5535.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class IrregularObject extends Object {
    public static IrregularObject draggedObject = null;
    private final Vertex center = new Vertex();
    private final double radius;
    private final List<Vertex> vertices;
    private final List<Line> lines;

    public IrregularObject(List<Vertex> vertices, Color objectColor) {
        this(0, 0, vertices, objectColor);
    }

    public IrregularObject(double x, double y, List<Vertex> vertices, Color objectColor) {
        super(objectColor);

        this.center.set(x, y);

        this.vertices = new ArrayList<>(vertices);
        this.lines = new ArrayList<>();

        Vertex maxDistancePoint = null;
        double maxDistance = Double.NEGATIVE_INFINITY;

        for (Vertex vertex : vertices) {
            double distance = Vertex.Distance(App.HALF_WIDTH + vertex.x + center.x, App.HALF_HEIGHT + vertex.y + center.y, App.HALF_WIDTH, App.HALF_HEIGHT);
            if (distance > maxDistance) {
                maxDistance = distance;
                maxDistancePoint = vertex;
            }
        }

        for (int i = 0; i < this.vertices.size() - 1; i++) {
            final Vertex v0 = this.vertices.get(i);
            final Vertex v1 = this.vertices.get(i + 1);
            lines.add(new Line(
                    v0.x + App.HALF_WIDTH + this.center.x,
                    v0.y + App.HALF_HEIGHT + this.center.y,
                    v1.x + App.HALF_WIDTH + this.center.x,
                    v1.y + App.HALF_HEIGHT + this.center.y
            ) {{
                setStroke(objectColor);
            }});
        }
        lines.add(new Line(
                this.vertices.getLast().x + App.HALF_WIDTH + this.center.x,
                this.vertices.getLast().y + App.HALF_HEIGHT + this.center.y,
                this.vertices.getFirst().x + App.HALF_WIDTH + this.center.x,
                this.vertices.getFirst().y + App.HALF_HEIGHT + this.center.y) {{
            setStroke(objectColor);
        }});

        assert maxDistancePoint != null;
        this.radius = Vertex.Distance(App.HALF_WIDTH + center.x, App.HALF_HEIGHT + center.y, App.HALF_WIDTH + maxDistancePoint.x + center.x, App.HALF_HEIGHT + maxDistancePoint.y + center.y);

        xPositionField.setText(Double.toString(this.center.x));
        yPositionField.setText(Double.toString(this.center.y));
    }

    public boolean isHovered(double x, double y) {
        return Vertex.Distance(
                this.center.x + App.HALF_WIDTH,
                this.center.y + App.HALF_HEIGHT,
                x, y
        ) < this.radius;
    }

    public void setTranslation(double x, double y) {
        this.center.set(x, y);

        for (int i = 0; i < this.vertices.size() - 1; i++) {
            final Vertex v0 = this.vertices.get(i);
            final Vertex v1 = this.vertices.get(i + 1);
            final Line line = lines.get(i);
            line.setStartX(v0.x + App.HALF_WIDTH + this.center.x);
            line.setStartY(v0.y + App.HALF_HEIGHT + this.center.y);
            line.setEndX(v1.x + App.HALF_WIDTH + this.center.x);
            line.setEndY(v1.y + App.HALF_HEIGHT + this.center.y);
        }
        final Line line = lines.getLast();
        line.setStartX(this.vertices.getLast().x + App.HALF_WIDTH + this.center.x);
        line.setStartY(this.vertices.getLast().y + App.HALF_HEIGHT + this.center.y);
        line.setEndX(this.vertices.getFirst().x + App.HALF_WIDTH + this.center.x);
        line.setEndY(this.vertices.getFirst().y + App.HALF_HEIGHT + this.center.y);

        onPropertyChanged.onPropertyChanged(this);

        xPositionField.setText(Double.toString(this.center.x));
        yPositionField.setText(Double.toString(this.center.y));
    }

    public double getTranslationX() {
        return this.center.x;
    }

    public double getTranslationY() {
        return this.center.y;
    }

    @Override
    public boolean isIntersecting(Object other) {
        if (!(other instanceof IrregularObject otherObject)) {
            return false;
        }

        for (Line line : this.lines) {
            if (isSeparatingAxis(line, otherObject)) {
                return false;
            }
        }

        for (Line line : otherObject.getShape()) {
            if (isSeparatingAxis(line, this)) {
                return false;
            }
        }

        return true;
    }

    private boolean isSeparatingAxis(Line line, IrregularObject other) {
        Vertex normal = new Vertex(-(line.getEndY() - line.getStartY()), line.getEndX() - line.getStartX());

        double minThis = Double.POSITIVE_INFINITY;
        double maxThis = Double.NEGATIVE_INFINITY;
        for (Vertex vertex : this.vertices) {
            double dotProduct = (vertex.x - this.center.x) * normal.x + (vertex.y - this.center.y) * normal.y;
            minThis = Math.min(minThis, dotProduct);
            maxThis = Math.max(maxThis, dotProduct);
        }

        double minOther = Double.POSITIVE_INFINITY;
        double maxOther = Double.NEGATIVE_INFINITY;
        for (Vertex vertex : other.vertices) {
            double dotProduct = (vertex.x - other.center.x) * normal.x + (vertex.y - other.center.y) * normal.y;
            minOther = Math.min(minOther, dotProduct);
            maxOther = Math.max(maxOther, dotProduct);
        }

        return maxThis < minOther || maxOther < minThis;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Line> getShape() {
        return this.lines;
    }

    @Override
    public Node getInfo() {
        VBox node = (VBox) super.getInfo();
        EventHandler<ActionEvent> e = event -> this.setTranslation(Double.parseDouble(xPositionField.getText()), Double.parseDouble(yPositionField.getText()));
        xPositionField.setOnAction(e);
        yPositionField.setOnAction(e);
        return node;
    }
}