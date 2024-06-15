package com.cs203pz1milicamladenovic5535;

import com.cs203pz1milicamladenovic5535.utill.IrregularObject;
import com.cs203pz1milicamladenovic5535.utill.Object;
import com.cs203pz1milicamladenovic5535.utill.RegularObject;
import com.cs203pz1milicamladenovic5535.utill.Vertex;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    public static boolean REGULAR_OBJECTS = false;

    private static Label intersectionLabel;

    public static final long WINDOW_WIDTH = 800, HALF_WIDTH = WINDOW_WIDTH / 2;
    public static final long WINDOW_HEIGHT = 600, HALF_HEIGHT = WINDOW_HEIGHT / 2;

    Object object1 = null, object2 = null;

    void switchObject(BorderPane root) {
        if (REGULAR_OBJECTS) {
            object1 = new RegularObject(0, 0, 100, 100, Color.RED);
            object2 = new RegularObject(150, 0, 100, 100, Color.GREEN);
        } else {
            final List<Vertex> vertices1 = new ArrayList<>() {
                {
                    add(new Vertex(-50, -30));
                    add(new Vertex(-30, -50));
                    add(new Vertex(+30, -50));
                    add(new Vertex(+50, -30));
                    add(new Vertex(+50, +30));
                    add(new Vertex(+30, +50));
                    add(new Vertex(-30, +50));
                    add(new Vertex(-50, +30));
                }
            };
            final List<Vertex> vertices2 = new ArrayList<>() {
                {
                    add(new Vertex(-80, -50));
                    add(new Vertex(-30, -80));
                    add(new Vertex(+20, -70));
                    add(new Vertex(+60, -30));
                    add(new Vertex(+80, +20));
                    add(new Vertex(+70, +60));
                    add(new Vertex(+30, +80));
                    add(new Vertex(-20, +70));
                    add(new Vertex(-60, +30));
                    add(new Vertex(-80, -10));
                }
            };
            
            object1 = new IrregularObject(180, 0, vertices1, Color.RED);
            object2 = new IrregularObject(0, 0, vertices2, Color.GREEN);
        }

        assert object1 != null;
        assert object2 != null;

        final Object a = object1;
        final Object b = object2;

        root.setOnMousePressed(null);
        root.setOnMouseDragged(null);
        root.setOnMouseReleased(null);
        if (!REGULAR_OBJECTS) {
            root.setOnMousePressed(event -> {
                if (((IrregularObject) a).isHovered(event.getX(), event.getY())) {
                    Object.deltaX = event.getSceneX();
                    Object.deltaY = event.getSceneY();
                    IrregularObject.draggedObject = (IrregularObject) a;
                } else {
                    assert b instanceof IrregularObject;
                    if (((IrregularObject) b).isHovered(event.getX(), event.getY())) {
                        Object.deltaX = event.getSceneX();
                        Object.deltaY = event.getSceneY();
                        IrregularObject.draggedObject = (IrregularObject) b;
                    }
                }
            });

            root.setOnMouseDragged(event -> {
                if (IrregularObject.draggedObject == null) return;

                double offsetX = event.getSceneX() - Object.deltaX;
                double offsetY = event.getSceneY() - Object.deltaY;
                IrregularObject.draggedObject.setTranslation(
                        IrregularObject.draggedObject.getTranslationX() + offsetX,
                        IrregularObject.draggedObject.getTranslationY() + offsetY
                );
                Object.deltaX = event.getSceneX();
                Object.deltaY = event.getSceneY();
            });

            root.setOnMouseReleased(event -> IrregularObject.draggedObject = null);
        }

        object1.onPropertyChanged = event -> {
            final boolean isIntersecting = a.isIntersecting(b);
            intersectionLabel.setText(isIntersecting ? " Intersecting" : " Not Intersecting");
        };
        object2.onPropertyChanged = event -> {
            final boolean isIntersecting = b.isIntersecting(a);
            intersectionLabel.setText(isIntersecting ? " Intersecting" : " Not Intersecting");
        };

        root.setCenter(null);
        root.setCenter(new Pane() {{
            if (REGULAR_OBJECTS) {
                getChildren().addAll((Node) a.getShape());
                getChildren().addAll((Node) b.getShape());
            } else {
                List<Line> nodesA = a.getShape();
                List<Line> nodesB = b.getShape();

                getChildren().addAll(nodesA);
                getChildren().addAll(nodesB);
            }
        }});
        root.setBottom(null);
        root.setBottom(new HBox(
                a.getInfo(),
                b.getInfo()
        ) {{
            setSpacing(10);
            setAlignment(Pos.CENTER);
        }});
    }

    @Override
    public void start(Stage stage) throws IOException {
        intersectionLabel = new Label("");
        intersectionLabel.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        switchObject(root);

        root.setTop(new HBox(new CheckBox("Objects") {{
            setOnAction(event -> {
                REGULAR_OBJECTS = !REGULAR_OBJECTS;
                switchObject(root);
            });
        }}, intersectionLabel) {{
            setAlignment(Pos.CENTER);
        }});

        stage.setTitle("Intersection");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}