package com.creationline.sudoku.validator.design2;

/**
 * @author Kohsuke Kawaguchi
 */
public class App {
    public static void main(String[] args) throws InconsitencyException {
        int[][] board = new int[9][];

        /*  Scan horizontal lines for their consistencies
            XXXXXXXXX
            ---------
            ---------
            ---------
         */
        for (int y=0; y<9; y++) {
            var checker = new Checker();
            for (int x=0; x<9; x++) {
                checker.observe(x,y, board[x][y]);
            }
        }

        /*  Scan vertical lines for their consistencies
            X--------
            X--------
            X--------
            X--------
            ...
         */
        for (int x=0; x<9; x++) {
            var checker = new Checker();
            for (int y=0; y<9; y++) {
                checker.observe(x,y, board[x][y]);
            }
        }

        // look at the grid of 3x3 cells
        for (int x=0; x<9; x+=3) {
            for (int y=0; y<9; y+=3) {
                var checker = new Checker();
                for (int xx=0; xx<3; xx++) {
                    for (int yy=0; yy<3; yy++) {
                        checker.observe(x+xx,y+yy, board[x+xx][y+yy]);
                    }
                }
            }
        }
    }
}
