package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class SolverTest {
    @Test
    public void firstTry() throws UnsolvableBoardException {
        var b = Board.read(new String[]{
            "53..7....",
            "6..195...",
            ".98....6.",
            "8...6...3",
            "4..8.3..1",
            "7...2...6",
            ".6....28.",
            "...419..5",
            "....8..79"
        });

        new Solver().solve(b);

        System.out.println(b.toString());

        // if the solution is correct there shouldn't be any digits conflicting with each other
        assertThat((int)b.findInconsistencies().count(), is(0));
    }

    /*
        var b = Board.read(new String[]{
            ".........",
            ".........",
            ".........",
            ".........",
            ".........",
            ".........",
            ".........",
            ".........",
            "........."
        });
     */

    @Test
    public void medium() throws UnsolvableBoardException {
        var b = Board.read(new String[]{
            ".....96.3",
            "......9..",
            "6..8.1.57",
            "9.6.1.3.8",
            "34.92....",
            ".5...6...",
            "46.......",
            "28.3...4.",
            "..5..41.6"
        });

        new Solver().solve(b);

        System.out.println(b.toString());

        // if the solution is correct there shouldn't be any digits conflicting with each other
        assertThat((int)b.findInconsistencies().count(), is(0));
    }


    @Test
    public void hard() throws UnsolvableBoardException {
        var b = Board.read(new String[]{
            ".7.53....",
            "8.16..2.7",
            ".......1.",
            "...4.6...",
            "3.....745",
            ".8......6",
            "4.5....7.",
            "..31...29",
            "......5.."
        });

        new Solver().solve(b);

        System.out.println(b.toString());

        // if the solution is correct there shouldn't be any digits conflicting with each other
        assertThat((int)b.findInconsistencies().count(), is(0));
    }
}
