package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;
import com.creationline.sudoku.validator.Group;

import java.util.BitSet;

/**
 * Keeps track of what digits are possible for a cell.
 *
 * @author Kohsuke Kawaguchi
 */
public final class Cell {
    private final Board<Cell> board;
    private final int x,y;

    private final BitSet possibilities = new BitSet(10);


    /**
     * Creates a new cell that can take any possible value from 1 to 9.
     */
    public Cell(Board<Cell> board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        possibilities.set(1,10,true);
    }

    /**
     * Returns true if this cell can be possibly the given digit.
     */
    public boolean canBe(int digit) {
        assert 0<=digit && digit<9;
        return possibilities.get(digit);
    }

    /**
     * Declares that this cell cannot possibly be 'd'
     *
     * @return true if the state of this cell changes.
     */
    public boolean eliminate(int d) {
        boolean old = possibilities.get(d);
        possibilities.set(d,false);
        return old;
    }

    /**
     * Fixes this cell to 'd', used when we can guarantee that this cell cannot possibly
     * take any other digits.
     */
    public void setTo(int d) {
        possibilities.clear();
        possibilities.set(d,true);
    }

    /**
     * Returns true if this cell can only be one digit and that digit is d.
     */
    public boolean is(int d) {
        return possibilities.cardinality()==1 && canBe(d);
    }

    /**
     * Returns true if 'd' is a possible value in this cell.
     */
    public boolean isPossible(int d) {
        // if another mutually exclusive cell is already 'd' then clearly that's not possible
        for (var g : board.groupsOf(x,y)) {
            for (var c : g.cells()) {
                if (c!=this && c.is(d))
                    return false;
            }
        }
        return true;
    }

    /**
     * If this is the only cell that allows 'd' then this cell must be d.
     *
     * TODO: reduce strength. Maybe document this further.
     */
    public boolean mustBe(int d) {
        for (var g : board.groupsOf(x,y)) {
            if (mustBe(d, g))
                return true;
        }
        return false;
    }

    private boolean mustBe(int d, Group<Cell> g) {
        for (var c : g.cells()) {
            if (c!=this && c.canBe(d))
                return false;
        }
        return true;
    }

    // TODO: int[] is a waste of memory
    public int[] possibilities() {
        return possibilities.stream().toArray();
    }
}
