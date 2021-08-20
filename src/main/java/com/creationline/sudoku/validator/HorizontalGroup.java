package com.creationline.sudoku.validator;

import java.util.stream.IntStream;

/**
 * @author Kohsuke Kawaguchi
 */
class HorizontalGroup extends Group {
    final int x;

    public HorizontalGroup(Board board, int x) {
        super(board);
        this.x = x;
    }

    @Override
    public IntStream cells() {
        return IntStream.range(0,9).map(y -> board.get(x, y));
    }
}
