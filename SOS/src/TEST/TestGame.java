package TEST;

import SPRINT.*;
import org.junit.*;

import static org.junit.Assert.*;

public class TestGame {
    GUI gui = new GUI();

    @Test
    public void testGameSize() {   // initialize at size 3x3
        assertEquals(3, gui.game.getGameSize());
    }

    @Test
    public void testSimpleMode() {   // initialize as simple game
        assertEquals("Simple Game", gui.getGameType());
    }

    @Test
    public void testGeneralMode() {
        gui.setGameType(false);
        assertEquals("General Game", gui.getGameType());
    }

    @Test
    public void testSPlacement() {   // initialize player selection as s and check if placed
        gui.game.makeMove(0, 0);
        assertEquals(Square.value.S, gui.game.getSquareValue(0, 0));
    }

    @Test
    public void testOPlacement() {
        gui.blue.setLetter(Square.value.O);   // blue turn first
        gui.game.makeMove(0,0);
        assertEquals(Square.value.O, gui.game.getSquareValue(0,0));
    }

    @Test
    public void testSimpleWin() {   // check only score > 1
        gui.setGameType(true);   // true == simple
        gui.red.setScore(1);
        assertEquals(true, gui.game.checkWin());
    }

    @Test
    public void testGeneralWin() {   // check score > 0 and full board
        gui.setGameType(false);   // false == general game
        gui.red.setScore(1);
        for (int i = 0; i < gui.game.getGameSize(); i++) {
            for (int j = 0; j < gui.game.getGameSize(); j++) {
                gui.game.makeMove(i, j);
            }
        }
        assertEquals(true, gui.game.checkWin());
    }

    @Test
    public void testGameIsDraw() {
        for (int i = 0; i < gui.game.getGameSize(); i++) {
            for (int j = 0; j < gui.game.getGameSize(); j++) {
                gui.game.makeMove(i, j);   // would be full board of "S"
            }
        }
        assertEquals(0, gui.red.getScore());   // if scores 0, and game is over
        assertEquals(0, gui.blue.getScore());
        assertEquals(true, gui.game.checkWin());
    }
}