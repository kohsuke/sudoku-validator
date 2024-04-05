package com.creationline.sudoku.solver;

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
     * @return
     */
    public abstract Iterable<Cell> cells();

    // Make sure subtypes override this method
    public abstract String toString();
}
