package SPRINT;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class Board {
    private final int MIN_GRID_SIZE = 3;
    private final int MAX_GRID_SIZE = 12;
    private final int SQUARE_SIZE = 50;
    private int currentSize = MIN_GRID_SIZE;
    private Boolean gameOver = false;
    private Boolean isTurn = true;
    private Boolean simpleGame = true;
    public Player red = new Player(Color.RED);
    public Player blue = new Player(Color.BLUE);
    private Square[][] square;
    private Double[] lines = new Double[currentSize * currentSize];   // ensure enough space for combinations
    private int lineCounter = 0;
    private Random random = new Random();

    public Board() {
        populate(currentSize);
    }

    public Board(int size) {
        populate(size);
    }

    public void populate(int size) {   // will reset game
        setGameSize(size);
        lines = new Double[currentSize * currentSize];
        lineCounter = 0;
        square = new Square[currentSize][currentSize];
        for (int row = 0; row < currentSize; row++) {
            for (int col = 0; col < currentSize; col++) {
                square[row][col] = new Square(Square.value.NULL);
            }
        }
    }

    public Square.value getSquareValue(int row, int col) {
        return square[row][col].getValue();
    }

    public void setGameOver(Boolean over) {
        gameOver = over;
    }

    public Boolean getGameOver() {
        return gameOver;
    }

    public Color getPlayerTurnColor() {
        if (getTurn()) {
            return Color.BLUE;
        }
        return Color.RED;
    }

    public void setGameSize(int size) {
        if (size >= MIN_GRID_SIZE && size <= MAX_GRID_SIZE) {
            currentSize = size;
        }
    }

    public int getGameSize() {
        return currentSize;
    }

    public Boolean getTurn() {   // getTurn: blue, not getTurn: red
        return isTurn;
    }

    public void setTurn(Boolean turn) {
        isTurn = turn;
    }

    public void setGameType(Boolean type) {
        simpleGame = type;
    }

    public Boolean getGameType() {
        return simpleGame;
    }

    public int getSquareSize() {
        return SQUARE_SIZE;
    }

    public int getLineCounter() {
        return lineCounter;
    }

    public Color getLineColor(int i) {
        return lines[i].getColor();
    }

    public int getLineDouble(int i, int j) {
        int value = -1;
        switch (i) {
            case 1 -> value = lines[j].getCol();
            case 2 -> value = lines[j].getRow();
            case 3 -> value = lines[j].getColTo();
            case 4 -> value = lines[j].getRowTo();
        }
        return value;
    }

    private void updateLines(int x1, int x2, int y1, int y2) {
        lines[lineCounter] = new Double(x1, x2, y1, y2, getPlayerTurnColor());
        lineCounter++;
    }

    private Vector<Coordinate> getNullSquares() {   // all squares that == null
        Vector<Coordinate> coords = new Vector<Coordinate>();
        for (int i = 0; i < currentSize; i++) {
            for (int j = 0; j < currentSize; j++) {
                if (square[i][j].getValue() == Square.value.NULL) {
                    coords.add(new Coordinate(i, j));
                }
            }
        }
        return coords;
    }

    public void makeMove(int rowCheck, int colCheck) throws AWTException {
        if (!gameOver && square[rowCheck][colCheck].getValue() == Square.value.NULL) {   // need to check playerType
            if (!getTurn()) {
                square[rowCheck][colCheck].setValue(red.getLetter());
                red.setScore(red.getScore() + addToScore(rowCheck, colCheck));
                setTurn(true);
            } else {
                square[rowCheck][colCheck].setValue(blue.getLetter());
                blue.setScore(blue.getScore() + addToScore(rowCheck, colCheck));
                setTurn(false);
            }
            setGameOver(checkWin());
            if (Objects.equals(red.getPlayerType(), "Computer") && !getTurn() && !gameOver ||
                    Objects.equals(blue.getPlayerType(), "Computer") && getTurn() && !gameOver) {
                makeMoveComputer();
            }
        }
    }

    public void makeMoveComputer() throws AWTException {
        Vector<Coordinate> nullSquares = getNullSquares();
        Coordinate select = nullSquares.get(random.nextInt(nullSquares.size()));
        Square letterSelect = new Square(Square.value.values()[random.nextInt(2)]);

        if (!getTurn()) {
            square[select.getRow()][select.getCol()].setValue(letterSelect.getValue());
            red.setScore(red.getScore() + addToScore(select.getRow(), select.getCol()));
            setTurn(true);
        } else {
            square[select.getRow()][select.getCol()].setValue(letterSelect.getValue());
            blue.setScore(blue.getScore() + addToScore(select.getRow(), select.getCol()));
            setTurn(false);
        }
        setGameOver(checkWin());
        if (Objects.equals(red.getPlayerType(), "Computer") && !getTurn() && !gameOver ||
                Objects.equals(blue.getPlayerType(), "Computer") && getTurn() && !gameOver) {
            makeMoveComputer();
        }
    }

    protected Boolean checkWin() {
        int squaresOccupied = 0;
        if (red.getScore() > 0 || blue.getScore() > 0) {
            return true;   // if any score, SOS has been made
        }
        for (int i = 0; i < getGameSize(); i++) {   // only do for loop if no win
            for (int j = 0; j < getGameSize(); j++) {
                if (getSquareValue(i, j) != Square.value.NULL) {
                    squaresOccupied++;
                }
            }
        }
        // if here and true, all occupied, and no score
        return squaresOccupied == getGameSize() * getGameSize();
    }

    private int addToScore(int row, int col) {   // THERE HAS TO BE A BETTER WAY !!!
        int totalScore = 0;

        // check all around S
        if (row > 1 && col > 1 && row < currentSize - 2 && col < currentSize - 2 && square[row][col].getValue() == Square.value.S) {
            if (square[row - 1][col - 1].getValue() == Square.value.O && square[row - 2][col - 2].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines((col - 2) * SQUARE_SIZE, (col + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col + 1].getValue() == Square.value.O && square[row - 2][col + 2].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE);
            }
            if (square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row + 1][col + 1].getValue() == Square.value.O && square[row + 2][col + 2].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row + 1][col - 1].getValue() == Square.value.O && square[row + 2][col - 2].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                totalScore += 1;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        }
        // check S sides
        else if (row > 1 && row < currentSize - 2 && col <= 1 && square[row][col].getValue() == Square.value.S) {
            if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col + 1].getValue() == Square.value.O && square[row - 2][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE);
            }
            if (square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row + 1][col + 1].getValue() == Square.value.O && square[row + 2][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
        } else if (col > 1 && col < currentSize - 2 && row <= 1 && square[row][col].getValue() == Square.value.S) {
            if (square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row + 1][col + 1].getValue() == Square.value.O && square[row + 2][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row + 1][col - 1].getValue() == Square.value.O && square[row + 2][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        } else if (row > 1 && row < currentSize - 2 && col >= currentSize - 2 && square[row][col].getValue() == Square.value.S) {
            if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row + 1][col - 1].getValue() == Square.value.O && square[row + 2][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row - 1][col - 1].getValue() == Square.value.O && square[row - 2][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 2) * SQUARE_SIZE, (col + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
        } else if (row >= currentSize - 2 && col > 1 && col < currentSize - 2 && square[row][col].getValue() == Square.value.S) {
            if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row - 1][col - 1].getValue() == Square.value.O && square[row - 2][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 2) * SQUARE_SIZE, (col + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col + 1].getValue() == Square.value.O && square[row - 2][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE);
            }
            if (square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        }
        // check corners if S
        else if (row <= 1 && col <= 1 && square[row][col].getValue() == Square.value.S) {
            if (getGameSize() > 3 && square[row + 1][col + 1].getValue() == Square.value.O && square[row + 2][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (getGameSize() > 3 && square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (getGameSize() > 3 && square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        } else if (row <= 1 && col >= currentSize - 2 && col <= currentSize - 1 && square[row][col].getValue() == Square.value.S) {
            if (getGameSize() > 3 && square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (getGameSize() > 3 && square[row + 1][col - 1].getValue() == Square.value.O && square[row + 2][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, row * SQUARE_SIZE, (row + 3) * SQUARE_SIZE);
            }
            if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        } else if (row >= currentSize - 2 && row <= currentSize - 1 && col <= 1 && square[row][col].getValue() == Square.value.S) {
            if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (getGameSize() > 3 && square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (getGameSize() > 3 && square[row - 1][col + 1].getValue() == Square.value.O && square[row - 2][col + 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines(col * SQUARE_SIZE, (col + 3) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE);
            }
        } else if (row >= currentSize - 2 && row <= currentSize - 1 && col >= currentSize - 2 && col <= currentSize - 1 && square[row][col].getValue() == Square.value.S) {
            if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
            if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col + 1) * SQUARE_SIZE, (col - 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row - 1][col - 1].getValue() == Square.value.O && square[row - 2][col - 2].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 2) * SQUARE_SIZE, (col + 1) * SQUARE_SIZE, (row - 2) * SQUARE_SIZE, (row + 1) * SQUARE_SIZE);
            }
        }
        // check O not in corner
        else if (row > 0 && col > 0 && row < currentSize - 1 && col < currentSize - 1 && square[row][col].getValue() == Square.value.O) {
            if (square[row - 1][col].getValue() == Square.value.S && square[row + 1][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 1) * SQUARE_SIZE, (row + 2) * SQUARE_SIZE);
            }
            if (square[row][col - 1].getValue() == Square.value.S && square[row][col + 1].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 1) * SQUARE_SIZE, (col + 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
            if (square[row + 1][col - 1].getValue() == Square.value.S && square[row - 1][col + 1].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 1) * SQUARE_SIZE, (col + 2) * SQUARE_SIZE, (row + 2) * SQUARE_SIZE, (row - 1) * SQUARE_SIZE);
            }
            if (square[row - 1][col - 1].getValue() == Square.value.S && square[row + 1][col + 1].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 1) * SQUARE_SIZE, (col + 2) * SQUARE_SIZE, (row - 1) * SQUARE_SIZE, (row + 2) * SQUARE_SIZE);
            }
        }
        // check O sides
        else if (row > 0 && row < currentSize - 1 && col == 0 && square[row][col].getValue() == Square.value.O) {
            if (square[row - 1][col].getValue() == Square.value.S && square[row + 1][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 1) * SQUARE_SIZE, (row + 2) * SQUARE_SIZE);
            }
        } else if (row > 0 && row < currentSize - 1 && col == currentSize - 1 && square[row][col].getValue() == Square.value.O) {
            if (square[row - 1][col].getValue() == Square.value.S && square[row + 1][col].getValue() == Square.value.S) {
                totalScore++;
                updateLines(((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((col + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), (row - 1) * SQUARE_SIZE, (row + 2) * SQUARE_SIZE);
            }
        } else if (col > 0 && row == 0 && col < currentSize - 1 && square[row][col].getValue() == Square.value.O) {
            if (square[row][col - 1].getValue() == Square.value.S && square[row][col + 1].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 1) * SQUARE_SIZE, (col + 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        } else if (row == currentSize - 1 && col > 0 && col < currentSize - 1 && square[row][col].getValue() == Square.value.O) {
            if (square[row][col - 1].getValue() == Square.value.S && square[row][col + 1].getValue() == Square.value.S) {
                totalScore++;
                updateLines((col - 1) * SQUARE_SIZE, (col + 2) * SQUARE_SIZE, ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE), ((row + 1) * SQUARE_SIZE) - (int) (0.5 * SQUARE_SIZE));
            }
        }
        return totalScore;
    }
}