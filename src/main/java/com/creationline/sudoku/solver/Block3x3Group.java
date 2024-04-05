package com.creationline.sudoku.solver;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Kohsuke Kawaguchi
 */
class Block3x3Group extends Group {
    private final int x;
    private final int y;

    /**
     * @param x
     *      x={0,3,6}
     */
    public Block3x3Group(Board board, int x, int y) {
        super(board);
        assert x%3==0 && y%3==0;
        this.x = x;
        this.y = y;
    }

    @Override
    public Iterable<Cell> cells() {
//        return IntStream.range(0,9).map(i -> board.get(x+i/3,y+i%3));

        var cells = new ArrayList<Cell>();
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                cells.add(board.get(x+i, y+j));
            }
        }
        return cells;
    }

    @Override
    public String toString() {
        return String.format("%s@%dx%d", getClass().getSimpleName(), x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block3x3Group that = (Block3x3Group) o;
        return x == that.x &&
            y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
