package Towers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Solver {
    // Easy implementation would be to iterate through all possible values recursively. To implement a hint function however,
    // we'll need to use logic the player can follow and understand instead of brute force
    // It also helps ensure the puzzle is solvable without the player having to guess and backtrack
    // run the puzzle through a series of rules, and if anything changes, run it again
    public static boolean solve(boolean isHint) {
        boolean changedInLoop = false;
        boolean anyChange = false;
        int biggestValue = Puzzle.size;
        ArrayList<Integer>[][] possibilities = new ArrayList[Puzzle.size+2][Puzzle.size+2];

        for (int x = 0; x < Puzzle.size+2; x++) {
            for (int y = 0; y < Puzzle.size+2; y++) {
                possibilities[x][y] = new ArrayList<>();
                if (Puzzle.tiles[x][y].hasNotes()) {
                    for (int i = 0; i < Puzzle.tiles[x][y].notes.length; i++) {
                        if (Puzzle.tiles[x][y].notes[i]) {
                            // Use existing notes if any
                            possibilities[x][y].add (i+1);
                        }
                    }
                } else {
                    for (int i = 1; i < Puzzle.size+2-1; i++) {
                        // initial possibility array. Possibilities should only ever decrease, never increase
                        possibilities[x][y].add(i);
                    }
                }
            }
        }

        do {
            changedInLoop = false;
            // Determine where largest numbers (l) are. #1 clues always touch l, #s above that will always have at least #-1 spaces
            // between clue and l. Larger clues eliminate more possibilities the closer to the clue
            for (int x = 0; x < Puzzle.size+2 && !(isHint && changedInLoop); x++) {
                for (int y = 0; y < Puzzle.size+2 && !(isHint && changedInLoop); y++) {
                    // determine direction from clue to puzzle
                    // 1 of the 2 offsets will always be 0
                    int xOffset = 0, yOffset = 0;
                    if (x == 0) {xOffset++;}
                    else if (x == Puzzle.size+2 - 1) {xOffset--;}
                    else if (y == 0) {yOffset++;}
                    else if (y == Puzzle.size+2 - 1) {yOffset--;}

                    int clue = 0;
                    if (xOffset != 0 || yOffset != 0) {
                        clue = Puzzle.tiles[x][y].getShownValue();
                    }
                    Tile t = Puzzle.tiles[x + xOffset][y + yOffset];
                    ArrayList<Integer> p = possibilities[x + xOffset][y + yOffset];
                    t.setNotes(p);
                    if (clue > 0) {
                        if (clue == 1 && t.getShownValue() == 0) {
                            t.setShownValue(biggestValue);
                            changedInLoop = true;
                            p.clear();
                        } else if (t.getShownValue()==0) {
                            for (int i = clue - 1; i > 0 && !(isHint && changedInLoop); i--) {
                                for (int j = biggestValue - i + 1; j <= biggestValue; j++) {
                                    p.remove(Integer.valueOf(j));
                                }
                                changedInLoop = t.setNotes(p);
                                t = Puzzle.tiles[t.x + xOffset][t.y + yOffset];
                                p = possibilities[t.x][t.y];
                            }
                        }
                        if (changedInLoop && isHint) {
                            System.out.println("Mark1");
                            break;
                        }

                        System.out.println("Mark2 " + changedInLoop);
                        // When visible towers = clue-1, next square cannot be too small a value, depending on how far away
                        // biggestValue is and what numbers are available
                        // In this case, none of the numbers after the next square can be bigger than it as well
                        int counter = 1;
                        int distanceToBiggest = 0;
                        int biggestValueSeen = 0;
                        t = Puzzle.tiles[x + xOffset][y + yOffset];
                        p = possibilities[x + xOffset][y + yOffset];
                        while (t.getShownValue() > 0 && t.getShownValue() < biggestValue) {
                            if (t.getShownValue() > biggestValueSeen) {
                                counter++;
                                biggestValueSeen = t.getShownValue();
                            }
                            t = Puzzle.tiles[t.x + xOffset][t.y + yOffset];
                            p = possibilities[t.x][t.y];
                        }
                        if (counter == clue-1 && biggestValueSeen < Collections.min(p)) {
                            // Triggers only if empty square found before biggestValue and no smaller values available than biggestValueSeen
                            // Find distance to biggestValue
                            // None of the numbers between t and biggestValue can be larger than t
                            int maxValue = Collections.max(p);
                            Tile temp = t;
                            while (temp.getShownValue() != biggestValue && !possibilities[temp.x][temp.y].contains(biggestValue)) {
                                if (distanceToBiggest > 0) {
                                    if (possibilities[temp.x][temp.y].size() > 0 && Collections.max(possibilities[temp.x][temp.y]) >= maxValue) {
                                        possibilities[temp.x][temp.y].remove(Collections.max(possibilities[temp.x][temp.y]));
                                    }
                                }
                                distanceToBiggest++;
                                temp = Puzzle.tiles[temp.x + xOffset][temp.y + yOffset];
                            }

                            // Minimum value of t should be biggestValueSeen + distanceToBiggest
                            int minValue = biggestValueSeen + distanceToBiggest;
                            while (p.size() > 0 && Collections.min(p) < minValue) {
                                System.out.println(p + " at " + t.x + ", " + t.y);
                                p.remove(Collections.min(p));
                                changedInLoop = true;
                            }
                        }
                    }
                }
            }

            System.out.println("Mark3 " + changedInLoop);
            // Remove possibilities that have been set as values within that row / column
            for (int targetX = 1; targetX < Puzzle.size+1; targetX++) {
                for (int targetY = 1; targetY < Puzzle.size+1; targetY++) {
                    int v = Puzzle.tiles[targetX][targetY].getShownValue();
                    if (v > 0) {
                        for (int x = 1; x < Puzzle.size+1; x++) {
                            changedInLoop = possibilities[x][targetY].remove(Integer.valueOf(v)) || changedInLoop;
                        }

                        for (int y = 1; y < Puzzle.size+1; y++) {
                            changedInLoop = possibilities[targetX][y].remove(Integer.valueOf(v)) || changedInLoop;
                        }
                    } else {
                        // Check if a tile only has one possible value
                        if (possibilities[targetX][targetY].size() == 1) {
                            Puzzle.tiles[targetX][targetY].setShownValue(possibilities[targetX][targetY].get(0));
                            changedInLoop = true;
                        }
                    }
                }
            }


            // Check if a number is only possible at one place in a row / column
            for (int x = 1; x < Puzzle.size+2-1; x++) {
                int[] occurrences = new int[biggestValue+1];   // For convenience, array is 1 bigger than necessary. index 0 empty
                int[] index = new int[biggestValue+1];
                Arrays.fill(occurrences, 0);     // Every number should show up 1+ times. We want those that show up exactly once
                Arrays.fill(index, 0);          // Most recent index each number showed up at

                for (int y = 1; y < Puzzle.size+2 - 1; y++) {
                    if (Puzzle.tiles[x][y].getShownValue() > 0) {
                        occurrences[Puzzle.tiles[x][y].getShownValue()] = 2;
                    } else {
                        for (int i = 0; i < possibilities[x][y].size(); i++) {
                            occurrences[possibilities[x][y].get(i)] = occurrences[possibilities[x][y].get(i)]+1;
                            index[possibilities[x][y].get(i)] = y;
                        }
                    }
                }
                for (int i = 1; i < occurrences.length && !changedInLoop; i++) {
                    if (occurrences[i] == 1) {
                        Puzzle.tiles[x][index[i]].setShownValue(i);
                        changedInLoop = true;
                    }
                }
            }
            for (int y = 1; y < Puzzle.size+2 - 1; y++) {
                int[] occurrences = new int[biggestValue+1];   // For convenience, array is 1 bigger than necessary. index 0 empty
                int[] index = new int[biggestValue+1];
                Arrays.fill(occurrences, 0);     // Every number should show up 1+ times. We want those that show up exactly once
                Arrays.fill(index, 0);          // Most recent index each number showed up at

                for (int x = 1; x < Puzzle.size+2 - 1; x++) {
                    if (Puzzle.tiles[x][y].getShownValue() > 0) {
                        occurrences[Puzzle.tiles[x][y].getShownValue()] = 2;
                    } else {
                        for (int i = 0; i < possibilities[x][y].size(); i++) {
                            occurrences[possibilities[x][y].get(i)] = occurrences[possibilities[x][y].get(i)]+1;
                            index[possibilities[x][y].get(i)] = x;
                        }
                    }
                }
                for (int i = 1; i < occurrences.length && !changedInLoop; i++) {
                    if (occurrences[i] == 1) {
                        Puzzle.tiles[index[i]][y].setShownValue(i);
                        changedInLoop = true;
                    }
                }
            }
            anyChange = changedInLoop || anyChange;
        } while (changedInLoop && !isHint);
        anyChange = changedInLoop || anyChange;
        if (anyChange) {
            Puzzle.backup();
        }
        return anyChange;
    }
    
    // Returns true if hint found
    public static boolean getHint() {
        return false;
    }
}
