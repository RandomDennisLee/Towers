package Towers;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Start extends Application {
    static GridPane field = new GridPane();
    static VBox mainLayout = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);

        Puzzle.init();

        mainLayout.getChildren().add(field);
        for (int i = 0; i < Puzzle.tiles.length; i++) {
            for (int j = 0; j < Puzzle.tiles[i].length; j++) {
                field.add(Puzzle.tiles[i][j], i, j);

                Puzzle.tiles[i][j].prefWidthProperty().bind(Bindings.min(scene.widthProperty().divide(Puzzle.size + 2),
                        scene.heightProperty().divide(Puzzle.size + 2)));
                Puzzle.tiles[i][j].prefHeightProperty().bind(Bindings.min(scene.widthProperty().divide(Puzzle.size + 2),
                        scene.heightProperty().divide(Puzzle.size + 2)));
            }
        }

        // set font for everything
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(Bindings.min(scene.widthProperty().divide(Puzzle.size*3), scene.heightProperty().divide(Puzzle.size*3)));
        field.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";" ,"-fx-alignment:center"));

        // weird bug with initial window size
        primaryStage.show();
        primaryStage.hide();
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }
}
