package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;

/**
 * @author Kohsuke Kawaguchi
 */
public class UnsolvableBoardException extends Exception {
    public UnsolvableBoardException() {
        super();
    }
}
