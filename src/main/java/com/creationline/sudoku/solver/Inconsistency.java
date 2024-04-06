package com.creationline.sudoku.solver;

/**
 * Represents an inconsistency / constraint violation in Sudoku cells
 *
 * @author Kohsuke Kawaguchi
 */
public record Inconsistency(Group group, int digit) {
}
