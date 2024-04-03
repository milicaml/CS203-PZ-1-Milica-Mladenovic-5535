package com.cs203pz1milicamladenovic5535.utill;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class Object implements Intersector {
    public static double deltaX, deltaY;

    public Object(Color color) {
        super();
        objectColor = color;
        xPositionField = new TextField();
        yPositionField = new TextField();
    }

    public Object(double x, double y, Color color) {
        super();
        objectColor = color;
        xPositionField = new TextField(Double.toString(x));
        yPositionField = new TextField(Double.toString(y));
    }

    public abstract <T> T getShape();

    protected final Color objectColor;
    protected final TextField yPositionField, xPositionField;

    public Node getInfo() {
        return new VBox(
                new HBox(
                        new Label("Position X:"),
                        xPositionField
                ),
                new HBox(
                        new Label("Position Y:"),
                        yPositionField
                )
        ) {{
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(5);
            setPadding(new javafx.geometry.Insets(10));
            setStyle("-fx-border-color: " + ColorToHex(objectColor) + "; -fx-border-width: 2px;");
        }};
    }

    private static String ColorToHex(Color color) {
        return String.format("#%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    public OnPropertyChanged onPropertyChanged = null;
}