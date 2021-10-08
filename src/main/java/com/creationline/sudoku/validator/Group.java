package com.creationline.sudoku.validator;

/**
 * Group of 9 cells that are in the mutual exclusion relationship
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class Group<T> {
    protected final Board<T> board;

    /*package*/ Group(Board<T> board) {
        this.board = board;
    }

    /**
     * Lists up 9 digits that constitute a group.
     * @return
     */
    public abstract Iterable<T> cells();

    // Make sure subtypes override this method
    public abstract String toString();
}
