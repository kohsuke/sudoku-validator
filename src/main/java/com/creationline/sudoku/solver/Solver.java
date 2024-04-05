package com.creationline.sudoku.solver;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Kohsuke Kawaguchi
 */
public class Solver {

    public void solve(Board board) throws UnsolvableBoardException {
        // TODO: find a link on the internet that talks about this algorithm and point to it
        int loop=0;
        while (true) {
            if (isSolved(board))
                return;
            if (isInconsistent(board))
                throw new UnsolvableBoardException();

            var madeProgress = new AtomicBoolean();

            board.walk(c -> {
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

            System.out.println("loop #"+(++loop));
            System.out.println(board);

            if (!madeProgress.get()) {
                // we run out of deterministic moves
                // make a speculative move

                var cell = board.cells().filter(c->!c.isUnique()).findFirst().orElseThrow();
                for (int d : cell.possibilities()) {
                    var boardCopy = new Board(board);
                    boardCopy.get(cell.x, cell.y).setTo(d);
                    try {
                        solve(boardCopy);
                        board.walk(c ->
                            c.updateTo(boardCopy.get(c.x,c.y))
                        );
                        return;
                    } catch (UnsolvableBoardException e) {
                        // this was a dead end, keep trying the next speculation
                    }
                }

                // if none of the possibilities led to a solution
                // this puzzle has no solution
                throw new UnsolvableBoardException();
            }
        }
    }

    private boolean isInconsistent(Board board) {
        return board.anyCellIs(Cell::isVoid);
    }

    private boolean isSolved(Board board) {
        return board.allCellIs(Cell::isUnique);
    }

}
