package com.tomgregory;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void canAttackLastRowAndColumn() {
        RecordingBoard board = new RecordingBoard(10, 0);
        Player player = new Player(board, new BoardTest.ScriptedRandom(9, 9));
        quiet(() -> player.takeTurn(0));
        assertEquals(List.of(new Point(9, 9)), board.attacks);
    }

    @Test
    void retriesPreviouslyAttackedCoordinates() {
        RecordingBoard board = new RecordingBoard(10, 0);
        Player player = new Player(board, new BoardTest.ScriptedRandom(0, 0, 0, 0, 1, 0));
        quiet(() -> {
            player.takeTurn(0);
            player.takeTurn(1);
        });
        assertEquals(List.of(new Point(0, 0), new Point(1, 0)), board.attacks);
    }

    @Test
    void attacksEveryCellExactlyOnceIncludingEdges() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> quiet(() -> {
            RecordingBoard board = new RecordingBoard(10, 1);
            Player player = new Player(board, new Random(2));
            for (int turn = 0; turn < 100; turn++) player.takeTurn(turn);
            assertEquals(100, new HashSet<>(board.attacks).size());
            assertTrue(board.attacks.contains(new Point(9, 9)));
            assertTrue(board.isSunk());
        }));
    }

    @Test
    void oneThousandSeededGamesRespectTurnLimitAndSinkAfterThreeHits() {
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> quiet(() -> {
            int sunkGames = 0;
            int survivedGames = 0;
            for (int seed = 0; seed < 1_000; seed++) {
                RecordingBoard board = new RecordingBoard(10, seed);
                Player player = new Player(board, new Random(seed + 10_000));
                int turn = 0;
                while (turn < 50 && !board.isSunk()) player.takeTurn(turn++);
                assertEquals(turn, new HashSet<>(board.attacks).size());
                assertTrue(turn <= 50);
                long hits = board.attacks.stream().filter(p -> board.ship.contains(p)).count();
                assertEquals(hits == 3, board.isSunk());
                if (board.isSunk()) sunkGames++;
                else survivedGames++;
            }
            assertTrue(sunkGames > 0);
            assertTrue(survivedGames > 0);
        }));
    }

    private static void quiet(Runnable action) {
        PrintStream original = System.out;
        try (PrintStream sink = new PrintStream(OutputStream.nullOutputStream())) {
            System.setOut(sink);
            action.run();
        } finally {
            System.setOut(original);
        }
    }

    private static final class RecordingBoard extends Board {
        private final List<Point> attacks = new ArrayList<>();
        private final List<Point> ship;

        RecordingBoard(int size, long seed) {
            super(size, new Random(seed));
            try {
                ship = BoardTest.shipPoints(this);
            } catch (Exception exception) {
                throw new AssertionError(exception);
            }
        }

        @Override
        public boolean attack(Point point) {
            assertTrue(point.x >= 0 && point.x < getGridSize());
            assertTrue(point.y >= 0 && point.y < getGridSize());
            attacks.add(new Point(point));
            return super.attack(point);
        }
    }
}
