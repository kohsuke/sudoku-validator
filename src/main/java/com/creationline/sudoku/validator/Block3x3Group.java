package com.creationline.sudoku.validator;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * @author Kohsuke Kawaguchi
 */
class Block3x3Group extends Group {
    private final int x;
    private final int y;

    public Block3x3Group(Board board, int x, int y) {
        super(board);
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
}
