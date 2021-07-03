package Towers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class Tile extends StackPane {
    int actualValue = 0;                // correct answer to puzzle. 1-9
    int shownValue = 0;                 // user-selected answer. 1-9, 0 is blank
    boolean[] notes = new boolean[9];   // mini notes in each tile
    GridPane notesPane = new GridPane();// grid for displaying notes
    Label label = new Label();
    double defaultOpacity = 0.2;
    double opacity = 0.2;
    boolean clickable = false;
    boolean enteringSolution = false;   // false - user is entering notes. True - user is entering answer
    int maxValue = 0;
    int x, y;                           // coordinates of tile, should be for debugging purposes only
    //int isShowing = 0;                  // what the tile is showing. 0-nothing, 1-notes, 2-answer,3-given hint

    public Tile(boolean clickable1, int x1, int y1, int max) {
        for (int i = 0; i < notes.length; i++) {
            //initialise notes
            notes[i] = false;
        }

        clickable = clickable1;
        maxValue = max;

        // Set fixed width and height for notes
        ColumnConstraints col = new ColumnConstraints();
        RowConstraints row = new RowConstraints();
        col.setPercentWidth(33);
        row.setPercentHeight(33);
        notesPane.getColumnConstraints().add(col);
        notesPane.getColumnConstraints().add(col);
        notesPane.getColumnConstraints().add(col);
        notesPane.getRowConstraints().add(row);
        notesPane.getRowConstraints().add(row);
        notesPane.getRowConstraints().add(row);

        // Setting font of notes
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(Bindings.min(this.widthProperty().divide(5), this.heightProperty().divide(5)));
        notesPane.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));

        // focus listener
        this.setOnMouseClicked(event -> {
            this.requestFocus();
            if (event.getButton() == MouseButton.PRIMARY) {
                enteringSolution = true;
                setOpacity(2);
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                enteringSolution = false;
                setOpacity(3);
            }
        });

        // key listener
        this.setOnKeyTyped(event -> {
            int i = 0;
            if (Character.isDigit(event.getCharacter().charAt(0))) {
                i = Character.getNumericValue(event.getCharacter().charAt(0));
            }

            if (i > 0 && i <= maxValue) {
                if (enteringSolution) {         // was it a left or right click?
                    setShownValue(i);
                } else {
                    notes[i-1] = !notes[i-1];
                    showNotes();
                }
            }
        });

        if (clickable) {
            this.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                focusState(newValue);
            });
            setOpacity(1);
        }
    }

    private void focusState(boolean value) {
        if (!value) {
            setOpacity(1);
        }
    }

    // mode 0 = nothing
    // 1 = normal puzzle
    // 2 = selected left click
    // 3 = selected right click
    public void setOpacity(int mode) {
        if (mode == 0) {
            this.setStyle("");
        } else if (mode == 1) {
            this.setStyle("-fx-background-color: rgba(0, 255, 191, " + opacity + "); -fx-border-color: black");
        } else if (mode == 2) {
            this.setStyle("-fx-background-color: rgba(153, 204, 255, " + (opacity+0.5) + "); -fx-border-color: black");
        } else if (mode == 3) {
            this.setStyle("-fx-background-color: rgba(128, 255, 128, " + (opacity+0.5) + "); -fx-border-color: black");
        }
    }

    public void setActualValue(int v) {actualValue=v;}
    public void setShownValue(int v) {
        shownValue=v;
        label.setText(v+"");
        this.getChildren().clear();
        this.getChildren().add(label);

        // delete notes
        for (int i = 0; i < notes.length; i++) {
            notes[i] = false;
        }
    }

    public void showNotes() {
        this.getChildren().clear();
        this.getChildren().add(notesPane);
        notesPane.getChildren().clear();

        for (int i = 0; i < notes.length; i++) {
            if (notes[i]) {
                StackPane tempPane = new StackPane();
                Label tempLabel = new Label((i+1) + "");
                tempPane.getChildren().add(tempLabel);
                notesPane.add(tempPane,i%3,i/3);
            }
        }
    }
}
