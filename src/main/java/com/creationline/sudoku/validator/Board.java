package com.creationline.sudoku.validator;

import com.creationline.sudoku.solver.Cell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.IntStream.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class Board<T> {
    // this is type unsafe but since it never leaks outside this class this is OK
    // Array.newInstance requires an explicit type casting anyway
    private final T[] cells = (T[])new Object[9*9];

    public Board() {
    }

    public T get(int x, int y) {
        rangeRange(x);
        rangeRange(y);
        return cells[y*9+x];
    }

    public void set(int x, int y, T value) {
        rangeRange(x);
        rangeRange(y);
//        rangeRange(value);
        cells[y*9+x] = value;
    }

    public boolean isEmpty(int x, int y) {
        return get(x,y)==EMPTY;
    }

    private void rangeRange(int i) {
        if (i<0 || i>9)
            throw new IllegalArgumentException("Value out of range: "+i);
    }

    public Stream<Group> listConstraintGroup() {
        // TODO: range(0,9) x3 is redundant
        return concat(
            range(0,9).mapToObj(i -> new HorizontalGroup(this,i)),
            range(0,9).mapToObj(i -> new VerticalGroup(this,i)),
            range(0,9).mapToObj(i -> new Block3x3Group(this,(i/3)*3,(i%3)*3)));
    }

    /**
     * Find three groups that govern the given cell.
     */
    public Iterable<Group<T>> groupsOf(int x, int y) {
        return List.of(
            new HorizontalGroup<>(this, y),
            new VerticalGroup<>(this, x),
            new Block3x3Group<>(this, (x / 3) * 3, (y / 3) * 3)
        );
    }

    @SafeVarargs
    private static <T> Stream<T> concat(Stream<T>... items) {
        return Arrays.stream(items).reduce(Stream.empty(), Stream::concat);
    }

    public static Board<Integer> read(String[] lines) {
        if (lines.length!=9)
            throw new IllegalArgumentException();

        var board = new Board<Integer>();
        for (int y=0; y<9; y++) {
            var line = lines[y].replace(" ","");    // allow input to have whitespaces
            for (int x=0; x<9; x++) {
                char digit = line.charAt(x);
                if ('1'<=digit && digit<='9') {
                    board.set(x,y,digit-'0'); // a digit is present
                } else
                if (digit=='.')  {
                    // empty cell, pass through
                } else {
                    throw new IllegalArgumentException("Unexpected character: '"+digit+"'");
                }
            }
        }
        return board;
    }

    public static Board<Integer> read(Reader r) throws IOException {
        var board = new Board<Integer>();
        try (var br = new BufferedReader(r)) {
            for (int y=0; y<9; y++) {
                String str = br.readLine();
                if (str.length()!=9)
                    throw new IllegalArgumentException(
                        String.format("Line length not 9: '%s'",str));
                for (int x = 0; x < 9; x++) {
                    char ch = str.charAt(x);
                    if ('1'<=ch && ch<='9')
                        board.set(x, y, ch - '0');
                }
            }
        }
        return board;
    }

    /**
     * Swap X & Y axis
     */
    public Board<T> flipDiagonal() {
        //noinspection SuspiciousNameCombination
        return transform((x,y,c) -> get(y,x));
    }

    public <R> Board<R> transform(CellFunction<T,R> f) {
        var that = new Board<R>();
        walk((x, y, c) -> that.set(x, y, f.apply(x,y,c)));
        return that;
    }

    /**
     * Iterates through all cells of the board.
     */
    public void walk(CellWalker<T> walker) {
        for (int y=0; y<9; y++) {
            for (int x=0; x<9; x++) {
                walker.visit(x,y,get(x,y));
            }
        }
    }

    /**
     * True if every cell of the digit is filled with a number
     * and no cell returns true from {@link #isEmpty(int, int)}.
     */
    public boolean allCellIs(CellFunction<T,Boolean> predicate) {
        var r = new AtomicBoolean(true);
        walk((x,y,c) -> {
            if (!predicate.apply(x,y,c))
                r.set(false);
        });
        return r.get();
    }

    public boolean anyCellIs(CellFunction<T,Boolean> predicate) {
        return !allCellIs((x,y,c) -> !predicate.apply(x,y,c));
    }

    /**
     * Formats this board into a string by formatting individual cells.
     * The printer is expected to produce the same dimension for every cell.
     */
    public String toString(CellFunction<T,String[]> cellPrinter) {
        var buf = new StringBuilder();

        var strs = this.transform(cellPrinter);
        int height = strs.get(0,0).length;

        for (int y=0; y<9; y++) {
            for (int h=0; h<height; h++) {
                for (int x=0; x<9; x++) {
                    buf.append(strs.get(x,y)[h]);
                }
                buf.append('\n');
            }
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        var buf = new StringBuilder();
        for (int y=0; y<9; y++) {
            for (int x=0; x<9; x++) {
                var c = get(x,y);
                buf.append(c==EMPTY ? '.' : c.toString());
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    /**
     * Constant representing an empty cell.
     */
    public final T EMPTY = null;

}
