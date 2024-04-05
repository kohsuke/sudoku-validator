package com.creationline.sudoku.solver;

import com.creationline.sudoku.validator.Board;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Kohsuke Kawaguchi
 */
public class SolverTest {
    /**
     * Converts board for validation to board for solution
     */
    private Board<Cell> toSolve(Board<Integer> i) {
        var o = new Board<Cell>();
        o.walk((x,y,c) -> {
            c = new Cell(o, x, y);
            o.set(x, y, c);
            var n = i.get(x,y);
            if (n!=i.EMPTY)
                c.setTo(n);
        });
        return o;
    }

    private void solve(String boardRep) throws UnsolvableBoardException, IOException {
        var b = Board.read(new StringReader(boardRep));

        new Solver().solve(toSolve(b));

        System.out.println(b);

//        // if the solution is correct there shouldn't be any digits conflicting with each other
//        assertThat((int)b.findInconsistencies().count(), is(0));
    }

    @Test
    public void firstTry() throws Exception {
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
    public void medium() throws Exception {
        solve("""
            .....96.3
            ......9..
            6..8.1.57
            9.6.1.3.8
            34.92....
            .5...6...
            46.......
            28.3...4.
            ..5..41.6
            """);
    }


    @Test
    public void hard() throws Exception {
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
