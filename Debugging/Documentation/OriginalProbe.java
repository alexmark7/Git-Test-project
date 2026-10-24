package com.tomgregory;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.Arrays;

/** Diagnostic harness for the UNMODIFIED upstream code, not part of the game. */
public final class OriginalProbe {
    public static void main(String[] args) throws Exception {
        if (args[0].equals("sinking")) {
            Board board = validBoard();
            Field field = Board.class.getDeclaredField("grid");
            field.setAccessible(true);
            boolean[][] grid = (boolean[][]) field.get(board);
            // A known three-cell ship isolates the hit-count condition.
            for (boolean[] column : grid) Arrays.fill(column, false);
            for (int x = 0; x < 3; x++) grid[x][0] = true;
            for (int x = 0; x < 3; x++) board.attack(new Point(x, 0));
            System.out.println("After three distinct hits: isSunk=" + board.isSunk());
        } else {
            new Board(10);
        }
    }

    private static Board validBoard() {
        while (true) {
            try {
                return new Board(10);
            } catch (ArrayIndexOutOfBoundsException ignored) {
                // Ignore the independent placement bug when isolating isSunk.
            }
        }
    }
}
