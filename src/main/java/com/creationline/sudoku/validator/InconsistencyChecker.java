package com.creationline.sudoku.validator;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Kohsuke Kawaguchi
 */
public class InconsistencyChecker {
    private final Board<Integer> board;

    public InconsistencyChecker(Board<Integer> board) {
        this.board = board;
    }

    public Stream<Inconsistency> findInconsistencies() {
        return board.listConstraintGroup()
            .map(this::findInconsistencyIn)
            .filter(Objects::nonNull);
    }

    /**
     * Tests if this group has a group of digits that meet the Sudoku constraint,
     * i.e. no duplicate numbers.
     */
    private Inconsistency findInconsistencyIn(Group<Integer> g) {
        boolean[] present = new boolean[10];

        for (Integer digit : g.cells()) {
            if (digit == board.EMPTY) continue;   // OK to have multiple empty cells
            if (present[digit])
                return new Inconsistency(g, digit);
            present[digit] = true;
        }
        return null;
    }

}
