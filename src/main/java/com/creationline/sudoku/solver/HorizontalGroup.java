package com.creationline.sudoku.solver;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Group of digits that occupies a row
 */
record HorizontalGroup(Board board, int y) implements Group {
    @Override
    public Stream<Cell> cells() {
        return IntStream.range(0,9).mapToObj(x -> board.get(x,y));
    }
}
