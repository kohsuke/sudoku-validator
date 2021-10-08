package com.creationline.sudoku.validator;

import java.util.Objects;

/**
 * Represents an inconsistency / constraint violation in Sudoku cells
 *
 * @author Kohsuke Kawaguchi
 */
public class Inconsistency {
    public final Group<Integer> group;
    public final int digit;

    public Inconsistency(Group<Integer> group, int digit) {
        this.group = group;
        this.digit = digit;
    }

    public void report() {
        // TODO
        System.err.printf("%s is inconsistent%n", "");
    }

    @Override
    public String toString() {
        return String.format("Inconsistency[%s,%d]",group,digit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inconsistency that = (Inconsistency) o;
        return digit == that.digit &&
            group.equals(that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, digit);
    }
}
