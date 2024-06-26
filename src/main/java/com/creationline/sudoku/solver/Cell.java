package com.creationline.sudoku.solver;

import java.util.BitSet;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Keeps track of what digits are possible for a cell.
 *
 * @author Kohsuke Kawaguchi
 */
public final class Cell {
    private final Board board;
    public final int x,y;

    private final BitSet possibilities = new BitSet(10);


    /**
     * Creates a new cell that can take any possible value from 1 to 9.
     */
    public Cell(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        possibilities.set(1,10,true);
    }

    /**
     * Overwrites the state to that of the given cell.
     */
    public void updateTo(Cell c) {
        this.possibilities.clear();
        this.possibilities.or(c.possibilities);
    }

    /**
     * Returns true if this cell can be possibly the given digit.
     */
    public boolean canBe(int digit) {
        assert 1<=digit && digit<=9;
        return possibilities.get(digit);
    }

    /**
     * Declares that this cell cannot possibly be 'd'
     *
     * @return true if the state of this cell changes.
     */
    public boolean eliminate(int d) {
        boolean old = possibilities.get(d);
        possibilities.set(d,false);
        return old;
    }

    /**
     * Fixes this cell to 'd', used when we can guarantee that this cell cannot possibly
     * take any other digits.
     */
    public void setTo(int d) {
        if (!canBe(d))
            throw new UnsolvableBoardException();
        possibilities.clear();
        possibilities.set(d,true);
    }

    /**
     * True if this cell is determined to be one digit.
     */
    public boolean isUnique() {
        return possibilities.cardinality() == 1;
    }

    /**
     * True if this cell cannot be any of the digits, which indicates the sudoku is not solvable.
     */
    public boolean isVoid() {
        return possibilities.cardinality()==0;
    }

    /**
     * If this cell has already been determined to be a specific digit, return it, or else empty.
     */
    public Optional<Integer> uniqueDigit() {
        return isUnique() ? Optional.of(possibilities.nextSetBit(0)) : Optional.empty();
    }

    /**
     * List up all other cells across all three constraint groups.
     */
    public Stream<Cell> allOtherCells() {
        return board.groupsAt(x,y).flatMap(this::otherCellsOf);
    }

    /**
     * List up other 8 cells of a given constraint group
     */
    private Stream<Cell> otherCellsOf(Group g) {
        return g.cells().filter(c -> c!=this);
    }

    /**
     * If this is the only cell that allows 'd' then this cell must be d.
     *
     * TODO: reduce strength. Maybe document this further.
     */
    public boolean mustBe(int d) {
        return board.groupsAt(x,y).anyMatch(g -> mustBe(d,g));
    }

    private boolean mustBe(int d, Group g) {
        return otherCellsOf(g).noneMatch(c -> c.canBe(d));
    }

    // TODO: int[] is a waste of memory
    public int[] possibilities() {
        return possibilities.stream().toArray();
    }

    /**
     * Print this cell for {@link Board#toString()}
     */
    public String[] print() {
        return uniqueDigit().map(d -> {
            // compact representation when the digit is determined to be one
            return new String[]{"   ",String.format(" %d ",d),"   "};
        }).orElseGet(() -> {
            // more general "all possibilities" display
            var buf = new StringBuilder();
            for (int i=1; i<=9; i++) {
                buf.append(canBe(i)?(char)('0'+i):'.');
            }
            var s = buf.toString();
            return new String[]{s.substring(0,3),s.substring(3,6),s.substring(6,9)};

        });
    }
}
