package com.creationline.sudoku.solver;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.google.common.truth.Truth.*;

/**
 * What needs to be covered in these tests?
 */
public class AppTest 
{
    /**
     * Simplest form of the test that should pass.
     */
    @Test
    public void testSanity() {
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

        assertThat(b.findInconsistencies()).isEmpty();
    }

    /**
     * Intent: make sure inconsistency in every row is separately checked.
     */
    @Test
    public void rowCheck() {
        for (int y=0; y<9; y++) {
            var b = board(
                    repeat(y,"........."),
                    ".1. ... .1.",
                    repeat(9-y-1,"........."));

            assertThat(b.findInconsistencies()).containsExactly(new Inconsistency(new HorizontalGroup(b, y), 1));
        }
    }

    /**
     * Intent: make sure inconsistency in every column is separately checked.
     */
    @Test
    public void columnCheck() {
        for (int y=0; y<9; y++) {
            var b = board(
                repeat(y,"........."),
                ".1. ... .1.",
                repeat(9-y-1,"........."));
            b = b.flipDiagonal();

            assertThat(b.findInconsistencies()).containsExactly(new Inconsistency(new VerticalGroup(b, y), 1));
        }
    }

    Board board(Object... rows) {
        var lines = new ArrayList<String>();
        for (var row : rows) {
            if (row instanceof String)
                lines.add((String) row);
            if (row instanceof String[])
                lines.addAll(Arrays.asList((String[])row));
        }
        return Board.read(lines.toArray(new String[0]));
    }

    String[] repeat(int n, String line) {
        var lines = new String[n];
        for (int i=0; i<n; i++)
            lines[i] = line;
        return lines;
    }
}
