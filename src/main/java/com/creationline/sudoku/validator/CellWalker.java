package com.creationline.sudoku.validator;

public interface CellWalker<T> {
    void visit(int x, int y, T cell);
}
