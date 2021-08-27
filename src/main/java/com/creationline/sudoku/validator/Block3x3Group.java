package com.creationline.sudoku.validator;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Kohsuke Kawaguchi
 */
class Block3x3Group extends Group {
    private final int x;
    private final int y;

    public Block3x3Group(Board board, int x, int y) {
        super(board);
        assert x%3==0 && y%3==0;
        this.x = x;
        this.y = y;
    }

    @Override
    public IntStream cells() {
//        return IntStream.range(0,9).map(i -> board.get(x+i/3,y+i%3));

        var cells = new ArrayList<Integer>();
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                cells.add(board.get(x+i, y+j));
            }
        }
        return cells.stream().mapToInt(x -> x);
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
