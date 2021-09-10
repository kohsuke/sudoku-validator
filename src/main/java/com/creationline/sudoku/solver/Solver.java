package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kohsuke Kawaguchi
 */
public class Solver {
    public void solve(Board board) throws UnsolvableBoardException {
        // TODO: find a link on the internet that talks about this algorithm and point to it
        while (!board.isFull()) {
            var madeProgress = new AtomicBoolean();
            board.walk((x, y) -> {
                if (board.isEmpty(x, y)) {
                    // if we can uniquely determine the digit for this cell, we can fill it
                    int unique = uniqueDigitFor(board, x, y);
                    if (unique != 0) {
                        board.set(x, y, unique);
                        madeProgress.set(true);
                    }
                }
            });
            if (!madeProgress.get()) {
                throw new UnsolvableBoardException(board);
            }
        }
    }

    private int uniqueDigitFor(Board board, int x, int y) {
        // design choice to think about: is BitSet worth it?
        BitSet possibilities = new BitSet(9);
        possibilities.set(1,10);    // initially all digits are possible

        // scan all digits within the constrained groups. Digits we see there
        // cannot possibly fill (x,y)
        board.groupsOf(x,y).forEach(g -> {
            g.cells().forEach(n -> possibilities.set(n,false));
        });

        if (possibilities.cardinality() == 1)
            return possibilities.nextSetBit(0);
        else
            return 0;   // not unique
    }
}
