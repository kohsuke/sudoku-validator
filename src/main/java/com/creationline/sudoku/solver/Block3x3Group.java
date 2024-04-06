package com.creationline.sudoku.solver;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Group of digits that represent a 3x3 block
 *
 * @param x
 *      x={0,3,6}
 * @param y
 *      Ditto
 */
record Block3x3Group(Board board, int x, int y) implements Group {
    @Override
    public Stream<Cell> cells() {
        return IntStream.range(0,9).mapToObj(i -> board.get(x+i/3,y+i%3));
    }
}
