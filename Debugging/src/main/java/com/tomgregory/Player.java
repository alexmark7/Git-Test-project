package com.tomgregory;

import java.awt.Point;
import java.util.Random;

public class Player {
    private final boolean[][] previousAttacks;
    private final Board board;
    private final Random random;

    public Player(Board board) {
        this(board, new Random());
    }

    Player(Board board, Random random) {
        this.board = board;
        this.random = random;
        previousAttacks = new boolean[board.getGridSize()][board.getGridSize()];
    }

    public void takeTurn(int turn) {
        Point attackPoint = getAttackPoint();
        System.out.printf("Turn %s: %s,%s", turn + 1, attackPoint.x, attackPoint.y);

        if (board.attack(attackPoint)) {
            System.out.print(" direct hit!");
        }
        System.out.println();
    }

    private Point getAttackPoint() {
        Point attackPoint = randomGridPoint();
        while (previousAttacks[attackPoint.x][attackPoint.y]) {
            attackPoint = randomGridPoint();
        }
        previousAttacks[attackPoint.x][attackPoint.y] = true;
        return attackPoint;
    }

    private Point randomGridPoint() {
        return new Point(randomInteger(board.getGridSize()), randomInteger(board.getGridSize()));
    }

    private int randomInteger(int upperBoundExclusive) {
        return random.nextInt(upperBoundExclusive);
    }
}
