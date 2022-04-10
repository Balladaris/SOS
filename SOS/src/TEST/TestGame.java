package TEST;

import SPRINT.*;
import org.junit.*;

import static org.junit.Assert.*;

public class TestGame {
    Board gui = new Board();

    @Test
    public void testGameSize() {   // initialize at size 3x3
        assertEquals(3, gui.getGameSize());
    }

    @Test
    public void testResizeGame() {
        gui = new Board(4);
        assertEquals(4, gui.getGameSize());
    }

    @Test
    public void testSimpleMode() {   // initialize as simple game
        assertEquals(true, gui.getGameType());   // true means simple
    }

    @Test
    public void testGeneralMode() {
        gui = new GeneralBoard();
        assertEquals(false, gui.getGameType());
    }

    @Test
    public void testSPlacement() {   // initialize player selection as s and check if placed
        gui.blue.setLetter(Square.value.S);
        gui.makeMove(0, 0);
        assertEquals(Square.value.S, gui.getSquareValue(0, 0));
    }

    @Test
    public void testOPlacement() {
        gui.blue.setLetter(Square.value.O);   // blue turn first
        gui.makeMove(0,0);
        assertEquals(Square.value.O, gui.getSquareValue(0,0));
    }

    @Test
    public void testSimpleWin() {   // will take turns. blue is 'S', red is 'O'
        gui.blue.setLetter(Square.value.S);
        gui.red.setLetter(Square.value.O);
        gui.makeMove(0, 0);
        gui.makeMove(0, 1);
        gui.makeMove(0, 2);
        assertEquals(true, gui.getGameOver());
    }

    @Test
    public void testGeneralWin() {
        gui = new GeneralBoard();
        gui.blue.setLetter(Square.value.S);
        gui.red.setLetter(Square.value.O);
        gui.makeMove(0, 0);   // make 'SOS' combination to generate score
        gui.makeMove(0, 1);
        gui.makeMove(0, 2);
        assertEquals(false, gui.getGameOver());
        for (int i = 0; i < gui.getGameSize(); i++) {
            for (int j = 0; j < gui.getGameSize(); j++) {
                if (gui.getSquareValue(i, j) == Square.value.NULL) {   // place 'S' in all empty squares
                    gui.makeMove(i, j);
                }
            }
        }
        assertEquals(4, gui.blue.getScore()); // if red changes to 'O' and place sequentially, will be 4-0
        assertEquals(0, gui.red.getScore());
        assertEquals(true, gui.getGameOver());
    }

    @Test
    public void testSimpleGameIsDraw() {
        gui.blue.setLetter(Square.value.S);
        gui.red.setLetter(Square.value.S);
        for (int i = 0; i < gui.getGameSize(); i++) {
            for (int j = 0; j < gui.getGameSize(); j++) {
                gui.makeMove(i, j);   // would be full board of "S"
            }
        }
        assertEquals(0, gui.red.getScore());   // if scores 0, and game is over
        assertEquals(0, gui.blue.getScore());
        assertEquals(true, gui.getGameOver());
    }

    @Test
    public void testGeneralGameIsDraw() {
        gui.blue.setLetter(Square.value.S);
        gui.red.setLetter(Square.value.S);
        for (int i = 0; i < gui.getGameSize(); i++) {
            for (int j = 0; j < gui.getGameSize(); j++) {
                gui.makeMove(i, j);   // would be full board of "S"
            }
        }
        assertEquals(0, gui.red.getScore());   // if scores 0, and game is over
        assertEquals(0, gui.blue.getScore());
        assertEquals(true, gui.getGameOver());
    }
}