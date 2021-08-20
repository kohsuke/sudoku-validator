package com.creationline.sudoku.validator;

import java.util.stream.IntStream;

/**
 * Group of 9 cells that are in the mutual exclusion relationship
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class Group {
    protected final Board board;

    /*package*/ Group(Board board) {
        this.board = board;
    }

    /**
     * Lists up 9 digits that constitute a group.
     */
    public abstract IntStream cells();

    /**
     * Tests if this group has a group of digits that meet the Sudoku constraint,
     * i.e. no duplicate numbers.
     */
    public final Inconsistency findInconsistency() {
        boolean[] present = new boolean[10];

        for (int digit : cells().toArray()) {
            if (digit == Board.EMPTY) continue;   // OK to have multiple empty cells
            if (present[digit])
                return new Inconsistency(this, digit);
            present[digit] = true;
        }
        return null;
    }
}
