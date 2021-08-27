package com.creationline.sudoku.validator;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        try (var r = new InputStreamReader(System.in)) {
            Board b = Board.read(r);

            b.findInconsistencies()
                .forEach(Inconsistency::report);
        }
    }
}
