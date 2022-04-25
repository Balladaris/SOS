package SPRINT;

//   USED TO STORE EMPTY SQUARES
public class Coordinate {
    private final int row;
    private final int col;

    Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}