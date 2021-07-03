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
    static private int size = 5;
    static Tile[][] tiles = new Tile[size+2][size+2];  // Convenience collection of all tiles

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);

        mainLayout.getChildren().add(field);

        for (int i = 0; i < size+2; i++) {
            for (int j = 0; j < size+2; j++) {
                Tile tile = new Tile(false, i, j, size);

                if (i > 0 && i < size+1 && j > 0 && j < size+1) {
                    tile = new Tile(true, i, j, size);
                }
                tiles[i][j] = tile;
                field.add(tile, i, j);

                tile.prefWidthProperty().bind(Bindings.min(scene.widthProperty().divide(size+2),
                        scene.heightProperty().divide(size+2)));
                tile.prefHeightProperty().bind(Bindings.min(scene.widthProperty().divide(size+2),
                        scene.heightProperty().divide(size+2)));
            }
        }

        // set font for everything
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(Bindings.min(scene.widthProperty().divide(size*3), scene.heightProperty().divide(size*3)));
        field.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";" ,"-fx-alignment:center"));

        // weird bug with initial window size
        primaryStage.show();
        primaryStage.hide();
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(400);
        primaryStage.show();

        Solution.generate(tiles);



    }
}
