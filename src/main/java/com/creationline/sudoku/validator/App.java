package com.creationline.sudoku.validator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        try (var r = new InputStreamReader(System.in)) {
            Board b = Board.read(r);

            b.listConstraintGroup()
                .map(Group::findInconsistency)
                .filter(Objects::nonNull)
                .forEach(Inconsistency::report);
        }
    }
}
