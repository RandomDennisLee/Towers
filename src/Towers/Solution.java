package Towers;

import java.util.ArrayList;
import java.util.Random;

public class Solution {
    static int[][] solution;           // x, y, and possibility array of valid values
    static int[][] clues;              // Clues are the numbers at the borders that tell the player how many towers are visible
    static int size;                   // width & height are always the same
    static Random r = new Random();
    static Tile[][] tiles;

    public static void generate (Tile[][] tiles1) {
        tiles = tiles1;
        size = tiles.length-2;  // Size of the puzzle, without borders
        solution = new int[size][size];
        clues = new int[size+2][size+2];

        initialise();
        saveSolution();
        saveClues();
    }

    public static void initialise() {
        boolean successfulGeneration;
        do {
            System.out.println("Attempting generation...");
            // initialise field. 1st column and row can be fixed - will scramble later
            // Creates reduced latin square for faster generation
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (x == 0) {
                        solution[x][y] = y+1;
                    } else if (y == 0) {
                        solution[x][y] = x+1;
                    } else {
                        solution[x][y] = 0;
                    }
                }
            }

            // Some attempts will result in dead ends. Backtracking to find a solution is an option, but would favor
            // certain latin squares over others, by a significant factor. Hence totally random brute force attempt
            successfulGeneration = true;
            for (int x = 1; x < size && successfulGeneration; x++) {
                for (int y = 1; y < size && successfulGeneration; y++) {
                    successfulGeneration = fillRandom(x, y);
                }
            }

            // This step randomises the reduced latin square, removing the fixed lines
            int[] temp;
            int tempInt;
            int swap1;
            int swap2;

            for (int i = 0; i < 1000; i++) {
                swap1 = r.nextInt(size);
                swap2 = r.nextInt(size);
                temp = solution[swap1];
                solution[swap1] = solution[swap2];
                solution[swap2] = temp;
            }
            for (int i = 0; i < 1000; i++) {
                swap1 = r.nextInt(size);
                swap2 = r.nextInt(size);
                for (int j = 0; j < size; j++) {
                    tempInt = solution[j][swap1];
                    solution[j][swap1] = solution[j][swap2];
                    solution[j][swap2] = tempInt;
                }
            }

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    System.out.print (solution[x][y]);
                }
                System.out.println();
            }

        } while (!successfulGeneration);

        generateClues();
    }


    public static void generateClues() {
        for (int x = 0; x < size+2; x++) {
            for (int y = 0; y < size+2; y++) {
                clues[x][y] = 0;
            }
        }

        for (int x = 0; x < size; x++) {
            int counter = 0;
            int biggestNumber = 0;
            for (int y = 0; y < size; y++) {
                if (solution[x][y] > biggestNumber) {
                    biggestNumber = solution[x][y];
                    counter++;
                }
            }
            clues[x+1][0]=counter;

            counter = 0;
            biggestNumber = 0;
            for (int y = size-1; y >= 0; y--) {
                if (solution[x][y] > biggestNumber) {
                    biggestNumber = solution[x][y];
                    counter++;
                }
            }
            clues[x+1][size+1]=counter;
        }

        for (int y = 0; y < size; y++) {
            int counter = 0;
            int biggestNumber = 0;
            for (int x = 0; x < size; x++) {
                if (solution[x][y] > biggestNumber) {
                    biggestNumber = solution[x][y];
                    counter++;
                }
            }
            clues[0][y+1]=counter;

            counter = 0;
            biggestNumber = 0;
            for (int x = size-1; x >= 0; x--) {
                if (solution[x][y] > biggestNumber) {
                    biggestNumber = solution[x][y];
                    counter++;
                }
            }
            clues[size+1][y+1]=counter;
        }
    }

    public static ArrayList checkPossibilities(int targetX, int targety) {
        ArrayList possibilities = new ArrayList();
        for (int i = 1; i <= size; i++) {
            possibilities.add(i);
        }
        for (int x = 0; x < size; x++) {
            if (possibilities.remove(Integer.valueOf(solution[x][targety]))) {
            }
        }
        for (int y = 0; y < size; y++) {
            if (possibilities.remove(Integer.valueOf(solution[targetX][y]))) {
            }
        }
        return possibilities;
    }

    public static boolean fillRandom(int targetX, int targety) {
        ArrayList possibilities = checkPossibilities(targetX, targety);
        if (possibilities.size() > 0) {
            solution[targetX][targety] = (Integer) possibilities.get(r.nextInt(possibilities.size()));
        } else {
            return false;
        }
        return true;
    }

    public static void saveSolution() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[x+1][y+1].setActualValue(solution[x][y]);
                //tiles[x+1][y+1].setShownValue(solution[x][y]);
            }
        }
    }

    public static void saveClues() {
        for (int x = 0; x < size+2; x++) {
            for (int y = 0; y < size+2; y++) {
                if (clues[x][y] > 0) {
                    tiles[x][y].setShownValue(clues[x][y]);
                }
            }
        }
    }
}
