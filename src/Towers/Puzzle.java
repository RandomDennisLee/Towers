package Towers;

public class Puzzle {
    static int size = 6;
    static Tile[][] tiles = new Tile[size+2][size+2];  // collection of all tiles, including border clues

    public static void init() {
        for (int i = 0; i < size+2; i++) {
            for (int j = 0; j < size + 2; j++) {
                Tile tile = new Tile(false, i, j, size);

                if (i > 0 && i < size + 1 && j > 0 && j < size + 1) {
                    tile = new Tile(true, i, j, size);
                }
                tiles[i][j] = tile;
            }
        }
        Solution.generate(tiles);
        Solver.solve(tiles);
    }

    public static void validate() {
        for (int x1 = 1; x1 < size+1; x1++) {
            for (int y1 = 1; y1 < size + 1; y1++) {
                // Check for duplicates in same row, column
                Tile target = tiles[x1][y1];
                boolean valid = true;
                for (int x = 1; x < size + 1; x++) {
                    if (x != target.x && tiles[x][target.y].getShownValue() == target.getShownValue() && target.getShownValue() > 0) {
                        tiles[x][target.y].setValid(false);
                        target.setValid(false);
                        valid = false;
                    }
                }
                for (int y = 1; y < size + 1; y++) {
                    if (y != target.y && tiles[target.x][y].getShownValue() == target.getShownValue() && target.getShownValue() > 0) {
                        tiles[target.x][y].setValid(false);
                        target.setValid(false);
                        valid = false;
                    }
                }
                if (valid) {
                    target.setValid(true);
                }
            }
        }

        //Check does not violate border clue
        for (int x = 1; x < size+1; x++) {
            int topClue = tiles[x][0].shownValue;
            int bottomClue = tiles[x][size+1].shownValue;
            boolean clueApplies = true;
            int counter = 0;
            int biggestValue = 0;

            for (int y = 1; y < size+1 && clueApplies; y++) {
                int v = tiles[x][y].getShownValue();
                if (v == 0) {
                    clueApplies = false;
                } else if (v > biggestValue) {
                    counter++;
                    biggestValue = v;
                }
            }
            // counter = topClue and biggestValue < size, clue is violated
            // counter < topClue and biggestValue = size, clue is violated
            // counter > topClue, border is violated
            // clueApplies true means the entire row/column is filled, so biggestValue should = size
            //noinspection RedundantIfStatement
            if (counter > topClue || (counter < topClue && biggestValue == size) || (clueApplies && biggestValue < size ||
                    (counter == topClue && biggestValue < size))) {
                tiles[x][0].setValid(false);
            } else {
                tiles[x][0].setValid(true);
            }

            clueApplies = true;
            counter = 0;
            biggestValue = 0;
            for (int y = size; y >= 0 && clueApplies; y--) {
                int v = tiles[x][y].getShownValue();
                if (v == 0) {
                    clueApplies = false;
                } else if (v > biggestValue) {
                    counter++;
                    biggestValue = v;
                }
            }
            // See explanation of logic above
            //noinspection RedundantIfStatement
            if (counter > bottomClue || (counter < bottomClue && biggestValue == size) || (clueApplies && biggestValue < size ||
                    (counter == bottomClue && biggestValue < size))) {
                tiles[x][size+1].setValid(false);
            } else {
                tiles[x][size+1].setValid(true);
            }
        }
        for (int y = 1; y < size + 1; y++) {
            int leftClue = tiles[0][y].shownValue;
            int rightClue = tiles[size+1][y].shownValue;
            boolean clueApplies = true;
            int counter = 0;
            int biggestValue = 0;

            for (int x = 1; x < size+1 && clueApplies; x++) {
                int v = tiles[x][y].getShownValue();
                if (v == 0) {
                    clueApplies = false;
                } else if (v > biggestValue) {
                    counter++;
                    biggestValue = v;
                }
            }
            // See explanation of logic above
            //noinspection RedundantIfStatement
            if (counter > leftClue || (counter < leftClue && biggestValue == size) || (clueApplies && biggestValue < size ||
                    (counter == leftClue && biggestValue < size))) {
                tiles[0][y].setValid(false);
            } else {
                tiles[0][y].setValid(true);
            }

            clueApplies = true;
            counter = 0;
            biggestValue = 0;
            for (int x = size; x >= 0 && clueApplies; x--) {
                int v = tiles[x][y].getShownValue();
                if (v == 0) {
                    clueApplies = false;
                } else if (v > biggestValue) {
                    counter++;
                    biggestValue = v;
                }
            }
            // See explanation of logic above
            //noinspection RedundantIfStatement
            if (counter > rightClue || (counter < rightClue && biggestValue == size) || (clueApplies && biggestValue < size ||
                    (counter == rightClue && biggestValue < size))) {
                tiles[size+1][y].setValid(false);
            } else {
                tiles[size+1][y].setValid(true);
            }
        }
    }
}
