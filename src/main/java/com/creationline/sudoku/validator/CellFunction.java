package com.creationline.sudoku.validator;

public interface CellFunction<T,R> {
    R apply(int x, int y, T cell);
}
