package com.creationline.sudoku.solver;

import org.junit.Test;

import static com.google.common.truth.Truth.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class SolverTest {
    private Board solve(String s) throws UnsolvableBoardException {
        var b = Board.read(s);

        System.out.println(b);
        b.solve();
        System.out.println(b);

        // if the solution is correct there shouldn't be any digits conflicting with each other
        assertThat(b.findInconsistencies()).isEmpty();
        return b;
    }

    @Test
    public void firstTry() throws UnsolvableBoardException {
        solve("""
            53..7....
            6..195...
            .98....6.
            8...6...3
            4..8.3..1
            7...2...6
            .6....28.
            ...419..5
            ....8..79
            """);
    }

    @Test
    public void medium() throws UnsolvableBoardException {
        // solution
        var x = Board.read("""
            374965821
            182374569
            956128437
            529641378
            731582946
            648739215
            463857192
            215496783
            897213654
            """);
        assertThat(x.findInconsistencies()).isEmpty();

        // this one has multiple solutions!
        solve("""
            .7...582.
            ...374569
            .561...37
            ..96.....
            .31..294.
            ......215
            .6....1..
            ....9....
            ....1.6.4
            """);
    }


    @Test
    public void hard() throws UnsolvableBoardException {
        solve("""
            .7.53....
            8.16..2.7
            .......1.
            ...4.6...
            3.....745
            .8......6
            4.5....7.
            ..31...29
            ......5..
            """);
    }

    @Test
    public void fromLeetCode() {
        solve("""
            53..7....
            6..195...
            .98....6.
            8...6...3
            4..8.3..1
            7...2...6
            .6....28.
            ...419..5
            ....8..79
            """);
    }

    /*
        solve("""
            .........
            .........
            .........
            .........
            .........
            .........
            .........
            .........
            .........
            """);
     */
}
