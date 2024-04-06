package com.creationline.sudoku.solver;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Group of digits that occupies a column.
 */
record VerticalGroup(Board board, int x) implements Group {
    @Override
    public Stream<Cell> cells() {
        return IntStream.range(0,9).mapToObj(y -> board.get(x, y));
    }
}
