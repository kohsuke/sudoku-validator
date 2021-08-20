package com.creationline.sudoku.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.IntStream.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class Board {
    private final int[] cells = new int[9*9];

    public int get(int x, int y) {
        rangeRange(x);
        rangeRange(y);
        return cells[y*9+x];
    }

    public void set(int x, int y, int value) {
        rangeRange(x);
        rangeRange(y);
        rangeRange(value);
        cells[y*9+x] = value;
    }

    public boolean isEmpty(int x, int y) {
        return get(x,y)==EMPTY;
    }

    private void rangeRange(int i) {
        if (i<0 || i>=9)
            throw new IllegalArgumentException("Value out of range: "+i);
    }

    public Stream<Group> listConstraintGroup() {
        // TODO: range(0,9) x3 is redundant
        return concat(
            range(0,9).mapToObj(i -> new VerticalGroup(this,i)),
            range(0,9).mapToObj(i -> new HorizontalGroup(this,i)),
            range(0,9).mapToObj(i -> new Block3x3Group(this,i/3,i%3)));
    }

    @SafeVarargs
    private static <T> Stream<T> concat(Stream<T>... items) {
        return Arrays.stream(items).reduce(Stream.empty(), Stream::concat);
    }

    public static Board read(Reader r) throws IOException {
        var board = new Board();
        try (var br = new BufferedReader(r)) {
            for (int y=0; y<9; y++) {
                String str = br.readLine();
                if (str.length()!=9)
                    throw new IllegalArgumentException(
                        String.format("Line length not 9: '%s'",str));
                for (int x = 0; x < 9; x++) {
                    char ch = str.charAt(x);
                    if (ch == ' ') ch = '0';
                    board.set(x, y, ch - '0');
                }
            }
        }
        return board;
    }

    /**
     * Constant representing an empty cell.
     */
    public static final int EMPTY = 0;
}
