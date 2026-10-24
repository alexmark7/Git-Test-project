package com.tomgregory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(10)
class BoardTest {
    @Test
    void placesHorizontalShip() throws Exception {
        Board board = new Board(10, new ScriptedRandom(0, 0, 0));
        assertEquals(List.of(new Point(0, 0), new Point(1, 0), new Point(2, 0)), shipPoints(board));
    }

    @Test
    void placesVerticalShip() throws Exception {
        Board board = new Board(10, new ScriptedRandom(0, 0, 1));
        assertEquals(List.of(new Point(0, 0), new Point(0, 1), new Point(0, 2)), shipPoints(board));
    }

    @Test
    void sinksAfterExactlyThreeDistinctHits() {
        Board board = new Board(10, new ScriptedRandom(0, 0, 0));
        assertFalse(board.isSunk());
        assertFalse(board.attack(new Point(9, 9)));
        assertTrue(board.attack(new Point(0, 0)));
        assertFalse(board.isSunk());
        assertTrue(board.attack(new Point(1, 0)));
        assertFalse(board.isSunk());
        assertTrue(board.attack(new Point(2, 0)));
        assertTrue(board.isSunk());
    }

    @Test
    void placesHorizontalShipAtBottomRightEdge() throws Exception {
        Board board = new Board(10, new ScriptedRandom(7, 9, 0));
        assertEquals(List.of(new Point(7, 9), new Point(8, 9), new Point(9, 9)), shipPoints(board));
    }

    @Test
    void placesVerticalShipAtBottomRightEdge() throws Exception {
        Board board = new Board(10, new ScriptedRandom(9, 7, 1));
        assertEquals(List.of(new Point(9, 7), new Point(9, 8), new Point(9, 9)), shipPoints(board));
    }

    @Test
    void retriesHorizontalPlacementThatWouldLeaveBoard() throws Exception {
        ScriptedRandom random = new ScriptedRandom(8, 0, 0, 7, 0, 0);
        Board board = new Board(10, random);
        assertEquals(6, random.calls);
        assertEquals(List.of(new Point(7, 0), new Point(8, 0), new Point(9, 0)), shipPoints(board));
    }

    @Test
    void retriesVerticalPlacementThatWouldLeaveBoard() throws Exception {
        ScriptedRandom random = new ScriptedRandom(0, 8, 1, 0, 7, 1);
        Board board = new Board(10, random);
        assertEquals(6, random.calls);
        assertEquals(List.of(new Point(0, 7), new Point(0, 8), new Point(0, 9)), shipPoints(board));
    }

    @Test
    void supportsSmallestValidBoard() throws Exception {
        assertEquals(3, shipPoints(new Board(3, new Random(7))).size());
    }

    @Test
    void rejectsBoardsSmallerThanShip() {
        for (int size : new int[]{-1, 0, 1, 2}) {
            assertThrows(IllegalArgumentException.class, () -> new Board(size));
        }
    }

    @Test
    void fiveThousandSeededBoardsHaveOneStraightThreeCellShip() throws Exception {
        boolean horizontalSeen = false;
        boolean verticalSeen = false;
        boolean lastRowSeen = false;
        boolean lastColumnSeen = false;
        for (int seed = 0; seed < 5_000; seed++) {
            Board board = new Board(10, new Random(seed));
            List<Point> points = shipPoints(board);
            assertEquals(3, points.size(), "seed " + seed);
            boolean horizontal = points.stream().allMatch(p -> p.y == points.get(0).y);
            boolean vertical = points.stream().allMatch(p -> p.x == points.get(0).x);
            assertTrue(horizontal || vertical, "seed " + seed);
            assertEquals(2, points.get(0).distance(points.get(2)), "seed " + seed);
            horizontalSeen |= horizontal;
            verticalSeen |= vertical;
            lastRowSeen |= points.stream().anyMatch(p -> p.y == 9);
            lastColumnSeen |= points.stream().anyMatch(p -> p.x == 9);
            for (Point point : points) {
                assertTrue(board.attack(point));
            }
            assertTrue(board.isSunk(), "seed " + seed);
        }
        assertTrue(horizontalSeen && verticalSeen && lastRowSeen && lastColumnSeen);
    }

    @Test
    void sunkBoardProducesShipSunkMessageWithoutAnotherTurn() throws Exception {
        Board board = new Board(10, new ScriptedRandom(0, 0, 0));
        for (int x = 0; x < 3; x++) board.attack(new Point(x, 0));
        BattleshipGame game = new BattleshipGame();
        Field field = BattleshipGame.class.getDeclaredField("board");
        field.setAccessible(true);
        field.set(game, board);
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            game.play();
        } finally {
            System.setOut(original);
        }
        String text = output.toString(StandardCharsets.UTF_8);
        assertTrue(text.contains("Ship sunk!"));
        assertFalse(text.contains("Ship survived!"));
        assertFalse(text.contains("Turn 1:"));
    }

    static List<Point> shipPoints(Board board) throws Exception {
        Field field = Board.class.getDeclaredField("grid");
        field.setAccessible(true);
        boolean[][] grid = (boolean[][]) field.get(board);
        List<Point> points = new ArrayList<>();
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if (grid[x][y]) points.add(new Point(x, y));
            }
        }
        return points;
    }

    static final class ScriptedRandom extends Random {
        private final int[] values;
        private int calls;

        ScriptedRandom(int... values) {
            this.values = values;
        }

        @Override
        public int nextInt(int bound) {
            if (calls >= values.length) throw new AssertionError("Unexpected extra random draw");
            int value = values[calls++];
            if (value < 0 || value >= bound) {
                throw new AssertionError("Script value " + value + " outside [0," + bound + ")");
            }
            return value;
        }
    }
}
