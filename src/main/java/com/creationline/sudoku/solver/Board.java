package com.creationline.sudoku.solver;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.IntStream.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class Board {
    // this is type unsafe but since it never leaks outside this class this is OK
    // Array.newInstance requires an explicit type casting anyway
    private final Cell[] cells = new Cell[9*9];

    public Board() {
        for (int y=0; y<9; y++) {
            for (int x=0; x<9; x++) {
                set(x,y, new Cell(this,x,y));
            }
        }
    }

    public Board(Board src) {
        this();
        src.walk(c -> get(c.x,c.y).updateTo(c));
    }

    public Board flipDiagonal() {
        var dst = new Board();
        //noinspection SuspiciousNameCombination
        this.walk(c -> dst.get(c.y,c.x).updateTo(c));
        return dst;
    }

    public Cell get(int x, int y) {
        verifyRange(x);
        verifyRange(y);
        return cells[y*9+x];
    }

    private void set(int x, int y, Cell value) {
        verifyRange(x);
        verifyRange(y);
        cells[y*9+x] = value;
    }

    private void verifyRange(int i) {
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
    public Iterable<Group> groupsAt(int x, int y) {
        return List.of(
            new HorizontalGroup(this, y),
            new VerticalGroup(this, x),
            new Block3x3Group(this, (x / 3) * 3, (y / 3) * 3)
        );
    }

    @SafeVarargs
    private static <T> Stream<T> concat(Stream<T>... items) {
        return Arrays.stream(items).reduce(Stream.empty(), Stream::concat);
    }

    public static Board read(String line) {
        return read(line.split("\n"));
    }

        public static Board read(String[] lines) {
        if (lines.length!=9)
            throw new IllegalArgumentException();

        var board = new Board();
        for (int y=0; y<9; y++) {
            var line = lines[y].replace(" ","");    // allow input to have whitespaces
            for (int x=0; x<9; x++) {
                char digit = line.charAt(x);
                Cell c = board.get(x,y);

                if ('1'<=digit && digit<='9') {
                    c.setTo(digit-'0');
                } else
                if (digit != '.') {
                    throw new IllegalArgumentException("Unexpected character: '"+digit+"'");
                }
            }
        }
        return board;
    }

    /**
     * Iterates through all cells of the board.
     */
    public void walk(Consumer<Cell> walker) {
        for (int y=0; y<9; y++) {
            for (int x=0; x<9; x++) {
                walker.accept(get(x,y));
            }
        }
    }

    /**
     * True if every cell satisfies the given predicate.
     */
    public boolean allCellIs(Predicate<Cell> predicate) {
        return cells().allMatch(predicate);
    }

    public boolean anyCellIs(Predicate<Cell> predicate) {
        return cells().anyMatch(predicate);
    }

    public Stream<Cell> cells() {
        return Stream.of(cells);
    }

    /**
     * Formats this board into a string by formatting individual cells.
     * The printer is expected to produce the same dimension for every cell.
     */
    public String toString() {
        var buf = new StringBuilder();

        String[][] strs = new String[9*9][];

        walk(c -> strs[c.x+c.y*9]=c.print());

        int height = strs[0].length;

        for (int y=0; y<9; y++) {
            for (int h=0; h<height; h++) {
                for (int x=0; x<9; x++) {
                    buf.append(strs[x+y*9][h]);
                }
                buf.append('\n');
            }
        }
        return buf.toString();
    }

    public Stream<Inconsistency> findInconsistencies() {
        return listConstraintGroup()
            .map(g -> {
                boolean[] present = new boolean[10];

                for (Cell c : g.cells()) {
                    var u = c.uniqueDigit();
                    if (u.isPresent()) {
                        int digit = u.get();
                        if (present[digit])
                            return new Inconsistency(g, digit);
                        present[digit] = true;
                    }
                }
                return null;
            })
            .filter(Objects::nonNull);
    }

}
