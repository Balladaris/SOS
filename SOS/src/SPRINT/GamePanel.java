package SPRINT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.sqrt;

public class GamePanel extends JPanel {
    public final int SQUARE_SIZE = 50;
    private final int MIN_GRID_SIZE = 3;
    private final int MAX_GRID_SIZE = 12;
    private final int X_OFFSET = 15;
    private final int Y_OFFSET = 37;
    private int currentSize = MIN_GRID_SIZE;
    private Square[][] square;
    private final int DIAGONAL = (int) (SQUARE_SIZE * sqrt(2.0f));

    public GamePanel() {
        populate(currentSize);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowCheck = e.getY() / SQUARE_SIZE;
                int colCheck = e.getX() / SQUARE_SIZE;

                if (square[rowCheck][colCheck].getValue() == Square.value.NULL) {   // need to check win/playerType
                    if (GUI.red.isTurn) {
                        square[rowCheck][colCheck].setValue(GUI.red.getLetter());
                        GUI.red.isTurn = false;
                        GUI.blue.isTurn = true;
                    }
                    else if (GUI.blue.isTurn) {
                        square[rowCheck][colCheck].setValue(GUI.blue.getLetter());
                        GUI.blue.isTurn = false;
                        GUI.red.isTurn = true;
                    }
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.GRAY);
        drawGrid(g);
        drawLetter(g);
        g.drawLine(0,0, DIAGONAL * currentSize, DIAGONAL * currentSize);
    }

    private void drawGrid(Graphics g) {
        for (int rowGrid = 0; rowGrid < currentSize; rowGrid++) {
            for (int colGrid = 0; colGrid < currentSize; colGrid++) {
                g.setColor(Color.BLACK);
                g.drawRect(rowGrid * SQUARE_SIZE, colGrid * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawLetter(Graphics g) {   // NEED TO ADD PLAYER COLOR OR ALL BLACK?
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 30));
        for (int rowLetter = 0; rowLetter < currentSize; rowLetter++) {
            for (int colLetter = 0; colLetter < currentSize; colLetter++) {
                if (square[rowLetter][colLetter].getValue() == Square.value.S) {
                    g2d.drawString("S", colLetter * SQUARE_SIZE + X_OFFSET, rowLetter * SQUARE_SIZE + Y_OFFSET);
                }
                else if (square[rowLetter][colLetter].getValue() == Square.value.O) {
                    g2d.drawString("O", colLetter * SQUARE_SIZE + X_OFFSET, rowLetter * SQUARE_SIZE + Y_OFFSET);
                }
                else if (square[rowLetter][colLetter].getValue() == Square.value.NULL) {   // WILL DELETE ??
                    g2d.drawString("X", colLetter * SQUARE_SIZE + X_OFFSET, rowLetter * SQUARE_SIZE + Y_OFFSET);
                }
            }
        }
    }
    /*
    public void turn() {

    }
    */
    private void setGameSize(int size) {
        currentSize = size;
    }

    public int getGameSize() {
        return currentSize;
    }

    public void populate(int size) {   // will reset game
        setGameSize(size);
        setPreferredSize(new Dimension(currentSize * SQUARE_SIZE + 1, currentSize * SQUARE_SIZE + 1));
        square = new Square[size][size];
        for (int row = 0; row < currentSize; row++) {
            for (int col = 0; col < currentSize; col++) {
                square[row][col] = new Square(Square.value.NULL);
            }
        }
    }
}
