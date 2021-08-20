package com.creationline.sudoku.validator;

/**
 * Represents an inconsistency / constraint violation in Sudoku cells
 *
 * @author Kohsuke Kawaguchi
 */
public class Inconsistency {
    public final Group group;
    public final int digit;

    public Inconsistency(Group group, int digit) {
        this.group = group;
        this.digit = digit;
    }

    public void report() {
        // TODO
        System.err.printf("%s is inconsistent%n", "");
    }
}
