package TEST;

import SPRINT.*;
import org.junit.*;

import java.awt.*;

import static org.junit.Assert.*;

public class TestGame {
    Board board = new Board();

    @Test
    public void testGameSize() {   // initialize at size 3x3
        assertEquals(3, board.getGameSize());
    }

    @Test
    public void testResizeGame() {
        board = new Board(4);
        assertEquals(4, board.getGameSize());
    }

    @Test
    public void testSimpleMode() {   // initialize as simple game
        assertEquals(true, board.getGameType());   // true means simple
    }

    @Test
    public void testGeneralMode() {
        board = new GeneralBoard();
        assertEquals(false, board.getGameType());
    }

    @Test
    public void testSPlacement() throws AWTException {   // initialize player selection as s and check if placed
        board.blue.setLetter(Square.value.S);
        board.makeMove(0, 0);
        assertEquals(Square.value.S, board.getSquareValue(0, 0));
    }

    @Test
    public void testOPlacement() throws AWTException {
        board.blue.setLetter(Square.value.O);   // blue turn first
        board.makeMove(0,0);
        assertEquals(Square.value.O, board.getSquareValue(0,0));
    }

    @Test
    public void testSimpleWin() throws AWTException {   // will take turns. blue is 'S', red is 'O'
        board.blue.setLetter(Square.value.S);
        board.red.setLetter(Square.value.O);
        board.makeMove(0, 0);
        board.makeMove(0, 1);
        board.makeMove(0, 2);
        assertEquals(true, board.getGameOver());
    }

    @Test
    public void testGeneralWin() throws AWTException {
        board = new GeneralBoard();
        board.blue.setLetter(Square.value.S);
        board.red.setLetter(Square.value.O);
        board.makeMove(0, 0);   // make 'SOS' combination to generate score
        board.makeMove(0, 1);
        board.makeMove(0, 2);
        assertEquals(false, board.getGameOver());
        for (int i = 0; i < board.getGameSize(); i++) {
            for (int j = 0; j < board.getGameSize(); j++) {
                if (board.getSquareValue(i, j) == Square.value.NULL) {   // place 'S' in all empty squares
                    board.makeMove(i, j);
                }
            }
        }
        assertEquals(4, board.blue.getScore()); // if red changes to 'O' and place sequentially, will be 4-0
        assertEquals(0, board.red.getScore());
        assertEquals(true, board.getGameOver());
    }

    @Test
    public void testSimpleGameIsDraw() throws AWTException {
        board.blue.setLetter(Square.value.S);
        board.red.setLetter(Square.value.S);
        for (int i = 0; i < board.getGameSize(); i++) {
            for (int j = 0; j < board.getGameSize(); j++) {
                board.makeMove(i, j);   // would be full board of "S"
            }
        }
        assertEquals(0, board.red.getScore());   // if scores 0, and game is over
        assertEquals(0, board.blue.getScore());
        assertEquals(true, board.getGameOver());
    }

    @Test
    public void testGeneralGameIsDraw() throws AWTException {
        board = new GeneralBoard();
        board.blue.setLetter(Square.value.S);
        board.red.setLetter(Square.value.S);
        for (int i = 0; i < board.getGameSize(); i++) {
            for (int j = 0; j < board.getGameSize(); j++) {
                board.makeMove(i, j);   // would be full board of "S"
            }
        }
        assertEquals(0, board.red.getScore());   // if scores 0, and game is over
        assertEquals(0, board.blue.getScore());
        assertEquals(true, board.getGameOver());
    }

    @Test
    public void testSimpleComputerMove() throws AWTException { // if both computers, will move until game over
        board.blue.setPlayerType("Computer");
        board.red.setPlayerType("Computer");
        board.makeMoveComputer();
        assertEquals(true, board.getGameOver());
    }

    @Test
    public void testGeneralComputerMove() throws AWTException {
        board = new GeneralBoard();
        board.blue.setPlayerType("Computer");
        board.red.setPlayerType("Computer");
        board.makeMoveComputer();
        assertEquals(true, board.getGameOver());
    }
}