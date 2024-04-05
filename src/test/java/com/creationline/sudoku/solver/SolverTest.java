package com.creationline.sudoku.solver;

import org.junit.Assert;
import org.junit.Test;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class SolverTest {
    private void solve(String s) throws UnsolvableBoardException {
        var b = Board.read(s);

        System.out.println(b);
        new Solver().solve(b);
        System.out.println(b);

        // if the solution is correct there shouldn't be any digits conflicting with each other
        Assert.assertThat(b.findInconsistencies().count(), is(0L));
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
        Assert.assertEquals(x.findInconsistencies().collect(toList()), emptyList());

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
}
