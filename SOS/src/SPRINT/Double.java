package SPRINT;

import java.awt.*;

public class Double {
    private final int row;
    private final int rowTo;
    private final int col;
    private final int colTo;
    private final Color color;

    //   (x, y) -> (x, y)
    Double(int col, int colTo, int row, int rowTo, Color color) {
        this.row = row;
        this.rowTo = rowTo;
        this.col = col;
        this.colTo = colTo;
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public int getRowTo() {
        return rowTo;
    }

    public int getCol() {
        return col;
    }

    public int getColTo() {
        return colTo;
    }

    public Color getColor() {
        return color;
    }
}