package com.creationline.sudoku.validator;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Kohsuke Kawaguchi
 */
class VerticalGroup extends Group {
    final int x;

    public VerticalGroup(Board board, int x) {
        super(board);
        this.x = x;
    }

    @Override
    public IntStream cells() {
        return IntStream.range(0,9).map(y -> board.get(x, y));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"@"+x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerticalGroup that = (VerticalGroup) o;
        return x == that.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x);
    }
}
