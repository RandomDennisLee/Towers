package Towers;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static java.lang.Long.MAX_VALUE;

public class Start extends Application {
    static GridPane field = new GridPane();
    static HBox mainLayout = new HBox();
    static VBox menuLayout = new VBox();
    static Button newGameBtn = new Button("New Game");
    static Button instructionsBtn = new Button("How to play");
    static Button undoBtn = new Button("Undo");
    static Button redoBtn = new Button("Redo");
    static Alert instructionBox;
    static Alert victoryBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Towers");

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);

        Puzzle.init();

        menuLayout.getStylesheets().add(this.getClass().getResource("stylesheet.css").toExternalForm());
        mainLayout.getChildren().add(menuLayout);

        newGameBtn.setOnAction(e -> {Puzzle.newGame();});
        newGameBtn.setMaxWidth(MAX_VALUE);
        newGameBtn.setMinWidth(150);

        undoBtn.setOnAction(e -> {Puzzle.undo();});
        redoBtn.setOnAction(e -> {Puzzle.redo();});
        undoBtn.setMaxWidth(MAX_VALUE);
        redoBtn.setMaxWidth(MAX_VALUE);

        instructionsBtn.setMaxWidth(MAX_VALUE);
        instructionsBtn.setOnAction(e -> {
            instructionBox = new Alert(Alert.AlertType.INFORMATION);
            instructionBox.setTitle("Game Instructions");
            instructionBox.setHeaderText(null);
            instructionBox.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            instructionBox.setContentText("""
                    The rules are simple. Every row and column may not have duplicate numbers. Now, imagine the grid as a grid of city towers, with the numbers representing how tall they are.

                    From any point along the sides, you can only see towers in that row / column that are not blocked by a higher tower. The numbers along the edges represent how many towers are visible to you at that point.
                    
                    For example, a hint of 1 tells you that the closest tower is of height 6 (assuming a 6x6 game) A hint of 4 would indicate there are 4 towers visible, such as in the sequence 243561.
                    
                    Left click on a square and type in a number to fill it in, or right click and type to leave a 'pencil mark' note for yourself. Use the backspace to clear a square.
                    
                    Have fun!""");
            instructionBox.showAndWait();
        });

        menuLayout.getChildren().add(newGameBtn);
        menuLayout.getChildren().add(instructionsBtn);
        menuLayout.getChildren().add(undoBtn);
        menuLayout.getChildren().add(redoBtn);


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
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static void victory() {
        victoryBox = new Alert(Alert.AlertType.INFORMATION);
        victoryBox.setTitle("YAY!");
        victoryBox.setHeaderText(null);
        victoryBox.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        victoryBox.setContentText("You Did It!");
        victoryBox.showAndWait();
    }
}
