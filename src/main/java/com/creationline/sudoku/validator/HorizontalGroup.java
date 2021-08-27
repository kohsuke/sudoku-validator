package com.creationline.sudoku.validator;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Kohsuke Kawaguchi
 */
class HorizontalGroup extends Group {
    final int y;

    public HorizontalGroup(Board board, int y) {
        super(board);
        this.y = y;
    }

    @Override
    public IntStream cells() {
        return IntStream.range(0,9).map(x -> board.get(x,y));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"@"+y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HorizontalGroup that = (HorizontalGroup) o;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(y);
    }
}
