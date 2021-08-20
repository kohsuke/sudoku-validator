package com.creationline.sudoku.validator;

import java.util.stream.IntStream;

/**
 * @author Kohsuke Kawaguchi
 */
class VerticalGroup extends Group {
    final int y;

    public VerticalGroup(Board board, int y) {
        super(board);
        this.y = y;
    }

    @Override
    public IntStream cells() {
        return IntStream.range(0,9).map(x -> board.get(x,y));
    }
}
