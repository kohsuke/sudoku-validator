package com.creationline.sudoku.solver;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.IntStream.*;

/**
 * Represents the 9x9 Sudoku game board.
 */
public class Board {
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

    /**
     * List all the 9x3 constraint groups on this board.
     */
    public Stream<Group> listGroups() {
        return range(0,9).boxed().flatMap(i -> Stream.of(
            new HorizontalGroup(this,i),
            new VerticalGroup(this,i),
            new Block3x3Group(this,(i/3)*3,(i%3)*3)
        ));
    }

    /**
     * Find three groups that govern the given cell.
     */
    public Stream<Group> groupsAt(int x, int y) {
        return Stream.of(
            new HorizontalGroup(this, y),
            new VerticalGroup(this, x),
            new Block3x3Group(this, (x / 3) * 3, (y / 3) * 3)
        );
    }

    public static Board read(String line) {
        return read(line.split("\n"));
    }

    public static Board read(String[] lines) {
        if (lines.length != 9)
            throw new IllegalArgumentException();

        var board = new Board();
        for (int y = 0; y < 9; y++) {
            var line = lines[y].replace(" ", "");    // allow input to have whitespaces
            for (int x = 0; x < 9; x++) {
                char digit = line.charAt(x);
                Cell c = board.get(x, y);

                if ('1' <= digit && digit <= '9') {
                    c.setTo(digit - '0');
                } else if (digit != '.') {
                    throw new IllegalArgumentException("Unexpected character: '" + digit + "'");
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

    public boolean isInconsistent() {
        return cells().anyMatch(Cell::isVoid);
    }

    public boolean isSolved() {
        return cells().allMatch(Cell::isUnique);
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
        return listGroups()
            .flatMap(g -> {
                boolean[] present = new boolean[10];

                return g.cells().map(c -> {
                    var u = c.uniqueDigit();
                    if (u.isPresent()) {
                        int digit = u.get();
                        if (present[digit])
                            return new Inconsistency(g, digit);
                        present[digit] = true;
                    }
                    return null;
                });
            })
            .filter(Objects::nonNull);
    }

    /**
     * Solves this sudoku.
     *
     * <p>
     * The general approach is to make progress by elimination, just like humans do.
     * <ul>
     *     <li>If a cell's digit is already known, cross off that digit from other cells in the relevant groups
     *     <li>If there's a digit that none of the other cells in a group can be, then the cell's value must be that.
     * </ul>
     * <p>
     * This is probably slower than the algorithm that "pushes" the constraint out as we make changes through
     * the board, for example by maintaining the queue of cells, but 
     */
    public void solve() throws UnsolvableBoardException {
        // TODO: find a link on the internet that talks about this algorithm and point to it
        int loop=0;
        while (!isSolved()) {
            if (isInconsistent())
                throw new UnsolvableBoardException();

            var madeProgress = new AtomicBoolean();

            walk(c -> {
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
            System.out.println(this);

            if (!madeProgress.get()) {
                // we run out of deterministic moves
                // make a speculative move

                // find a cell that we can't decide
                var cell = cells().filter(c->!c.isUnique()).findFirst().orElseThrow();

                // try each possible digit there
                for (int d : cell.possibilities()) {
                    var copy = new Board(this);
                    copy.get(cell.x, cell.y).setTo(d);
                    try {
                        copy.solve();
                        // `copy` was solved, now copy its final state back into `this`
                        walk(c ->
                            c.updateTo(copy.get(c.x,c.y))
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
}
