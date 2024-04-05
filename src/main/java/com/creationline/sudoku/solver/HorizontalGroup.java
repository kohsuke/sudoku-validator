package com.creationline.sudoku.solver;

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
    public Iterable<Cell> cells() {
        return () -> IntStream.range(0,9).mapToObj(x -> board.get(x,y)).iterator();
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
