package com.creationline.sudoku.validator.design2;

import java.awt.*;

/**
 * @author Kohsuke Kawaguchi
 */
public class InconsitencyException extends Exception {
    public final Point cell1, cell2;

    public InconsitencyException(Point cell1, Point cell2) {
        this.cell1 = cell1;
        this.cell2 = cell2;
    }
}
