package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kohsuke Kawaguchi
 */
public class Solver {
    public void solve(Board<Cell> board) throws UnsolvableBoardException {
        // TODO: find a link on the internet that talks about this algorithm and point to it
        int loop=0;
        while (!board.allCellIs((x,y,c) -> c.isUnique())) {
            var madeProgress = new AtomicBoolean();
            System.out.println("loop #"+(loop++));
            System.out.println(board.toString(Cell.PRINTER));

            board.walk((x, y, c) -> {
                if (c.isUnique())
                    return; // nothing to do
                for (int d : c.possibilities()) {
                    if (c.mustBe(d)) {
                        c.setTo(d);
                        c.setOthersNotTo(d);
                        madeProgress.set(true);
                    } else {
                        boolean b = c.isPossible(d);
                        if (!b) {
                            if (c.eliminate(d))
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
