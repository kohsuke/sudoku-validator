package com.creationline.sudoku.validator;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * What needs to be covered in these tests?
 *
 *
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

        Assert.assertEquals(b.findInconsistencies().collect(toList()), emptyList());
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

            assertInconsistenciesOf(b,
                new Inconsistency(new HorizontalGroup(b, y), 1));
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

            assertInconsistenciesOf(b,
                new Inconsistency(new VerticalGroup(b, y), 1));
        }
    }

    void assertInconsistenciesOf(Board b, Inconsistency... expected) {
        Assert.assertEquals(
            List.of(expected),
            b.findInconsistencies().collect(toList())
        );
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
