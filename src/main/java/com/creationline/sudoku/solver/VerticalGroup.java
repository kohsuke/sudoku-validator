package com.creationline.sudoku.solver;

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
    public Iterable<Cell> cells() {
        return () -> IntStream.range(0,9).mapToObj(y -> board.get(x, y)).iterator();
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
