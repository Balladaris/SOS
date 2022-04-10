package SPRINT;

public class GeneralBoard extends Board {
    public GeneralBoard() {
        populate(getGameSize());
        setGameType(false);
    }

    public GeneralBoard(int size) {
        populate(size);
        setGameType(false);
    }

    public Boolean checkWin() {
        int squaresOccupied = 0;
        for (int i = 0; i < getGameSize(); i++) {
            for (int j = 0; j < getGameSize(); j++) {
                if (getSquareValue(i, j) == Square.value.NULL) {
                    return false;   // if any squares not occupied, game not over
                }
                squaresOccupied++;
            }
        }
        if (red.getScore() > blue.getScore() || blue.getScore() > red.getScore()) {
            return true;
        }
        // if here and true, all occupied, and no score
        return squaresOccupied == getGameSize() * getGameSize();
    }
}