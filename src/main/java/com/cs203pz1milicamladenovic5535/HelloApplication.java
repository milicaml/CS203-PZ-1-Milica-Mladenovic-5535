package com.cs203pz1milicamladenovic5535;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final long WINDOW_WIDTH = 800;
    public static final long WINDOW_HEIGHT = 600;

    public static Label intersectionLabel;

    static class ObjectInfo extends VBox {
        public ObjectInfo(final Object o) {
            super();

            centerXField = new TextField(Double.toString(o.getTranslateX()));
            centerYField = new TextField(Double.toString(o.getTranslateY()));
            widthField = new TextField(Double.toString(o.getWidth()));
            heightField = new TextField(Double.toString(o.getHeight()));

            super.getChildren().addAll(
                    new Label("Enter rectangle 1 info"),
                    new HBox(new Label("Center x:"), centerXField),
                    new HBox(new Label("Center y:"), centerYField),
                    new HBox(new Label("Width:"), widthField),
                    new HBox(new Label("Height:"), heightField)
            );

            o.translateXProperty().addListener((v, oldValue, newValue) -> {
                double value = Math.floor((double) newValue);
                String text = Double.toString(value);
                centerXField.setText(text);
            });
            o.translateYProperty().addListener((v, oldValue, newValue) -> {
                double value = Math.floor((double) newValue);
                String text = Double.toString(value);
                centerYField.setText(text);
            });
            o.widthProperty().addListener((v, oldValue, newValue) -> {
                double value = Math.floor((double) newValue);
                String text = Double.toString(value);
                widthField.setText(text);
            });
            o.heightProperty().addListener((v, oldValue, newValue) -> {
                double value = Math.floor((double) newValue);
                String text = Double.toString(value);
                heightField.setText(text);
            });
        }

        private final TextField centerXField;
        private final TextField centerYField;
        private final TextField widthField;
        private final TextField heightField;
    }

    @FunctionalInterface
    interface Intersector {
        boolean isIntersecting(Object other);
    }

    @FunctionalInterface
    interface OnObjectPropertyChanged {
        void onObjectPropertyChanged(Object o);
    }

    static class Object extends Rectangle implements Intersector {
        private double positionX, positionY;

        public Object(double v, double v1, double v2, double v3) {
            super(WINDOW_WIDTH * 0.5, WINDOW_HEIGHT * 0.5, v2, v3);
            super.setTranslateX(v - v2 * 0.5);
            super.setTranslateY(v1 - v3 * 0.5);

            // TODO : Add move events

            super.setOnMousePressed(event -> {
                positionX = event.getSceneX();
                positionY = event.getSceneY();
            });

            super.setOnMouseDragged(event -> {
                double offsetX = event.getSceneX() - positionX;
                double offsetY = event.getSceneY() - positionY;
                super.setTranslateX(super.getTranslateX() + offsetX);
                super.setTranslateY(super.getTranslateY() + offsetY);
                positionX = event.getSceneX();
                positionY = event.getSceneY();
            });

            super.translateXProperty().addListener((t, oldValue, newValue) -> {
                if (objectPropertyChangedListener != null) {
                    objectPropertyChangedListener.onObjectPropertyChanged(this);
                }
            });
            super.translateYProperty().addListener((t, oldValue, newValue) -> {
                if (objectPropertyChangedListener != null) {
                    objectPropertyChangedListener.onObjectPropertyChanged(this);
                }
            });
            super.widthProperty().addListener((t, oldValue, newValue) -> {
                if (objectPropertyChangedListener != null) {
                    objectPropertyChangedListener.onObjectPropertyChanged(this);
                }
            });
            super.heightProperty().addListener((t, oldValue, newValue) -> {
                if (objectPropertyChangedListener != null) {
                    objectPropertyChangedListener.onObjectPropertyChanged(this);
                }
            });
        }

        public final ObjectInfo info = new ObjectInfo(this);

        public OnObjectPropertyChanged objectPropertyChangedListener = null;

        @Override
        public boolean isIntersecting(Object other) {
            return this.intersects(other.getLayoutBounds()); // TODO : Add intersection
        }
    }

    @Override
    public void start(Stage stage) throws IOException {

        final Object first = new Object(0, 0, 100, 100);
        final Object second = new Object(150, 0, 100, 100);

        BorderPane root = new BorderPane();
        intersectionLabel = new Label("Two rectangles intersect? No");

        first.objectPropertyChangedListener = o -> {
            intersectionLabel.setText(first.isIntersecting(second) ? "Two rectangles intersect? Yes" : "Two rectangles intersect? No");
        };
        second.objectPropertyChangedListener = o -> {
            intersectionLabel.setText(first.isIntersecting(second) ? "Two rectangles intersect? Yes" : "Two rectangles intersect? No");
        };

        root.setTop(intersectionLabel);

        root.setCenter(new Pane(
                first, second
        ));

        root.setBottom(new HBox(
                first.info, second.info
        ));

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Intersection");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}