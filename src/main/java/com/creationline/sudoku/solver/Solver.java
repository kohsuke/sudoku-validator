package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kohsuke Kawaguchi
 */
public class Solver {
    public void solve(Board<Cell> board) throws UnsolvableBoardException {
        // TODO: find a link on the internet that talks about this algorithm and point to it
        while (!board.isFull()) {// TODO: this logic is implemented wrong
            var madeProgress = new AtomicBoolean();
            board.walk((x, y) -> {
                var c = board.get(x,y);
                for (int d : c.possibilities()) {
                    if (c.mustBe(d)) {
                        c.setTo(d);
                    } else {
                        boolean b = c.isPossible(d);
                        if (!b) {
                            c.eliminate(d);
                            madeProgress.set(true);
                        }
                    }

                }
            });
            if (!madeProgress.get()) {
                throw new UnsolvableBoardException(board);
            }
        }
    }

}
