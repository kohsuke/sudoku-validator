package com.creationline.sudoku.validator.design2;

import java.awt.*;

/**
 * Observes 9 cells that are supposed to be mutually exclusive and raise
 * an expcetion if it spots an inconsistency.
 *
 * @author Kohsuke Kawaguchi
 */
public class Checker {
    final Point[] digitsSeen = new Point[9];

    public void observe(int x, int y, int i) throws InconsitencyException {
        var pt = new Point(x,y);
        if (digitsSeen[i]!=null)
            throw new InconsitencyException(digitsSeen[i], pt);
        digitsSeen[i] = pt;
    }
}
