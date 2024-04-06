package com.creationline.sudoku.solver;

import java.util.stream.Stream;

/**
 * Group of 9 cells that are in the mutual exclusion relationship
 *
 * @author Kohsuke Kawaguchi
 */
public sealed interface Group permits Block3x3Group, HorizontalGroup, VerticalGroup {
    /**
     * Lists up 9 digits that constitute a group.
     * @return
     */
    Stream<Cell> cells();
}
