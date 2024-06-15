package com.cs203pz1milicamladenovic5535.utill;

import com.cs203pz1milicamladenovic5535.App;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RegularObject extends Object {

    private final Rectangle rectangle;

    public RegularObject(Color objectColor) {
        this(0, 0, objectColor);
    }

    public RegularObject(double x, double y, Color objectColor) {
        this(x, y, 100, 100, 0.0, objectColor);
    }

    public RegularObject(double x, double y, double width, double height, Color objectColor) {
        this(x, y, width, height, 0.0, objectColor);
    }

    public RegularObject(double x, double y, double width, double height, double angle, Color objectColor) {
        super(x, y, objectColor);

        this.widthField = new TextField(Double.toString(width));
        this.heightField = new TextField(Double.toString(height));

        this.rectangle = new Rectangle(
                App.WINDOW_WIDTH * 0.5,
                App.WINDOW_HEIGHT * 0.5,
                width, height) {{
            setFill(Color.TRANSPARENT);
            setStroke(objectColor);
        }};

        this.rectangle.setRotate(angle);

        this.rectangle.setTranslateX(x - width * 0.5);      //da se centar poklapa sa prosledjenim koordinatama
        this.rectangle.setTranslateY(y - height * 0.5);

        this.rectangle.setOnMousePressed(event -> {
            deltaX = event.getSceneX();
            deltaY = event.getSceneY();
        });

        this.rectangle.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - deltaX;
            double offsetY = event.getSceneY() - deltaY;
            this.rectangle.setTranslateX(this.rectangle.getTranslateX() + offsetX);
            this.rectangle.setTranslateY(this.rectangle.getTranslateY() + offsetY);
            deltaX = event.getSceneX();
            deltaY = event.getSceneY();
        });

        this.rectangle.translateXProperty().addListener((v, oldValue, newValue) -> {
            if (this.onPropertyChanged != null) {
                this.onPropertyChanged.onPropertyChanged(this);
            }
            yPositionField.setText(Double.toString(this.rectangle.getTranslateY()));
        });
        this.rectangle.translateYProperty().addListener((v, oldValue, newValue) -> {
            if (this.onPropertyChanged != null) {
                this.onPropertyChanged.onPropertyChanged(this);
            }
            xPositionField.setText(Double.toString(this.rectangle.getTranslateX()));
        });
        this.rectangle.widthProperty().addListener((v, oldValue, newValue) -> {
            if (this.onPropertyChanged != null) {
                this.onPropertyChanged.onPropertyChanged(this);
            }
            widthField.setText(Double.toString(this.rectangle.getWidth()));
        });
        this.rectangle.heightProperty().addListener((v, oldValue, newValue) -> {
            if (this.onPropertyChanged != null) {
                this.onPropertyChanged.onPropertyChanged(this);
            }
            heightField.setText(Double.toString(this.rectangle.getHeight()));
        });
    }

    @Override
    public boolean isIntersecting(Object other) {
        final Rectangle otherRect = other.getShape();

        final Vertex a = new Vertex(this.rectangle.getTranslateX(), this.rectangle.getTranslateY());
        final Vertex b = new Vertex(otherRect.getTranslateX(), otherRect.getTranslateY());

        final Vertex aRes = new Vertex(this.rectangle.getWidth(), this.rectangle.getHeight());
        final Vertex bRes = new Vertex(otherRect.getWidth(), otherRect.getHeight());

        return
                (Math.abs((a.x + aRes.x / 2) - (b.x + bRes.x / 2)) * 2 < (aRes.x + bRes.x)) &&
                (Math.abs((a.y + aRes.y / 2) - (b.y + bRes.y / 2)) * 2 < (aRes.y + bRes.y));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Rectangle getShape() {
        return this.rectangle;
    }

    private final TextField widthField, heightField;

    @Override
    public Node getInfo() {
        VBox node = (VBox) super.getInfo();
        node.getChildren().addAll(
                new HBox(
                        new Label("Width:"),
                        widthField
                ),
                new HBox(
                        new Label("Height:"),
                        heightField
                )
        );

        xPositionField.setOnAction(event -> {
            this.rectangle.setTranslateX(Double.parseDouble(xPositionField.getText()));
        });
        yPositionField.setOnAction(event -> {
            this.rectangle.setTranslateY(Double.parseDouble(yPositionField.getText()));
        });
        widthField.setOnAction(event -> {
            this.rectangle.setWidth(Double.parseDouble(widthField.getText()));
        });
        heightField.setOnAction(event -> {
            this.rectangle.setHeight(Double.parseDouble(heightField.getText()));
        });
        return node;
    }
}