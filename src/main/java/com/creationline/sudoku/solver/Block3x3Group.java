package com.creationline.sudoku.solver;

import java.util.ArrayList;

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
}
