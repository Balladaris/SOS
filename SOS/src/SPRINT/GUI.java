package SPRINT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.sqrt;

// FOCUS ON CHECK FOR SOS -- UPDATE SCORE COUNT FOR GENERAL GAME AND GREATER THAN 3X3

public class GUI extends JFrame {
    public GamePanel game = new GamePanel();
    public Player red = new Player(Color.RED);
    public Player blue = new Player(Color.BLUE);
    private JLabel redOrBlue, currentTurn, redScore, blueScore;
    private Boolean simpleGame;
    private Boolean isTurn = true;

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SOS");
        addPanels();
        setVisible(true);
    }
    /*
    private void replayGame() {

    }
    */
    private void resetGame() {
        red.setScore(0);
        blue.setScore(0);
        game.populate(game.getGameSize());
        addPanels();
        repaint();
    }

    private void addPanels() {   // initializing game
        Container content = getContentPane();
        content.removeAll();
        content.add(generateSidePanel(red), BorderLayout.EAST);
        content.add(generateSidePanel(blue), BorderLayout.WEST);
        content.add(generateTopPanel(), BorderLayout.NORTH);
        content.add(generateGamePanel(game), BorderLayout.CENTER);
        content.add(generateBottomPanel(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel generateSidePanel(Player player) {
        JRadioButton human, sButton, oButton, computer;
        JPanel pnl = new JPanel();
        JPanel scorePanel = new JPanel();
        JLabel playerText = generatePlayerText(player);
        JLabel scoreText = generateLabel("Score: ", 12);
        ButtonGroup playerSelect = new ButtonGroup();
        ButtonGroup letterSelect = new ButtonGroup();

        pnl.setLayout(new FlowLayout());
        //pnl.setBackground(new Color(80, 20, 10));
        pnl.setPreferredSize(new Dimension(100, 200));

        human = new JRadioButton("Human");
        human.addActionListener(e -> {
            player.setPlayerType("Human");
        });
        human.doClick();
        sButton = new JRadioButton("S");

        sButton.addActionListener(e -> {
            player.setLetter(Square.value.S);
        });
        sButton.doClick();
        oButton = new JRadioButton("O");
        oButton.addActionListener(e -> {
            player.setLetter(Square.value.O);
        });
        computer = new JRadioButton("Computer");
        computer.addActionListener(e -> {
            player.setPlayerType("Computer");
        });
        playerSelect.add(human);
        letterSelect.add(sButton);
        letterSelect.add(oButton);
        playerSelect.add(computer);

        scorePanel.setLayout(new FlowLayout());
        scorePanel.add(scoreText);
        if (player.getColor().equals(Color.RED)) {
            redScore = new JLabel("0");
            scorePanel.add(redScore);
        }
        else {
            blueScore = new JLabel("0");
            scorePanel.add(blueScore);
        }

        pnl.add(playerText);
        pnl.add(human);
        pnl.add(sButton);
        pnl.add(oButton);
        pnl.add(computer);
        pnl.add(scorePanel);
        return pnl;
    }

    private JPanel generateTopPanel() {
        JPanel top = new JPanel();
        JRadioButton simple = new JRadioButton("Simple Game");
        JRadioButton general = new JRadioButton("General Game");
        ButtonGroup gameType = new ButtonGroup();
        JPanel gameTypePanel = new JPanel();
        JPanel boardSizePanel = new JPanel();
        JTextField size = new JTextField(3);

        simple.addActionListener(e -> {
            simpleGame = true;
        });
        simple.doClick();
        general.addActionListener(e -> {
            simpleGame = false;
        });

        size.addActionListener(e -> {
            String input = size.getText();
            resize(input);
        });

        JLabel sizeText = generateLabel("Board Size", 12);
        boardSizePanel.setLayout(new FlowLayout());
        boardSizePanel.add(sizeText);
        boardSizePanel.add(size);

        top.setLayout(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(0,10,0,30));
        //top.setBackground(new Color(125,100,200));

        gameType.add(simple);
        gameType.add(general);
        gameTypePanel.setLayout(new FlowLayout());
        gameTypePanel.add(simple);
        gameTypePanel.add(general);

        top.add(generateLabel("SOS Game", 15), BorderLayout.WEST);
        top.add(gameTypePanel, BorderLayout.CENTER);
        top.add(boardSizePanel, BorderLayout.EAST);
        return top;
    }

    private JPanel generateGamePanel(GamePanel gameBoard) {
        JPanel game = new JPanel();
        gameBoard.setPreferredSize(new Dimension(gameBoard.getGameSize() * gameBoard.getSquareSize() + 1,
                gameBoard.getGameSize() * gameBoard.getSquareSize() + 1));
        //game.setBackground(new Color(150,75,25));
        game.add(gameBoard);
        return game;
    }

    private JPanel generateBottomPanel() {
        JPanel bottom = new JPanel();
        JPanel recordGamePanel = new JPanel();
        JCheckBox record = new JCheckBox();
        JLabel recordGameText = generateLabel("Record Game", 12);
        JPanel gameButtonPanel = new JPanel();

        JButton replay = new JButton("Replay");
        //replay.addActionListener(e -> replayGame());   // from recorded game

        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> {   // reset human/comp/s/o/game size?
            game.setGameOver(false);
            resetGame();
        });

        JPanel turnPanel = new JPanel();
        currentTurn = generateLabel("Current turn: ", 12);
        redOrBlue = generateLabel("Blue", 12);

        recordGamePanel.setLayout(new FlowLayout());
        recordGamePanel.add(record);
        recordGamePanel.add(recordGameText);

        turnPanel.setLayout(new FlowLayout());
        turnPanel.add(currentTurn);
        turnPanel.add(redOrBlue);

        gameButtonPanel.setLayout(new BorderLayout());
        gameButtonPanel.add(replay, BorderLayout.PAGE_START);
        gameButtonPanel.add(newGame, BorderLayout.PAGE_END);

        bottom.setLayout(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(0,0,10,30));

        bottom.add(recordGamePanel, BorderLayout.WEST);
        bottom.add(turnPanel, BorderLayout.CENTER);
        bottom.add(gameButtonPanel, BorderLayout.EAST);
        return bottom;
    }

    private JLabel generateLabel(String text, int size) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        return label;
    }

    private JLabel generatePlayerText(Player player) {   // could probably refactor
        JLabel text = new JLabel();
        if (player.getColor().equals(Color.RED)) {
            text.setText("Red Player");
        }
        else {
            text.setText("Blue Player");
        }
        text.setFont(new Font("SansSerif", Font.BOLD, 15));
        return text;
    }

    public String getGameType() {
        if (simpleGame) {
            return "Simple Game";
        }
        return "General Game";
    }

    public void resize (String input) {
        try {
            game.populate(Integer.parseInt(input));
            addPanels();
            repaint();
        }
        catch(NumberFormatException ignored) {   // ignore anything not integer

        }
    }

    public void setGameType(Boolean type) {
        simpleGame = type;
    }

    public Boolean getTurn() {
        return isTurn;
    }

    public void setTurn(Boolean turn) {
        isTurn = turn;
    }

    public void changeText(int i, String text) {
        switch (i) {
            case 1 -> redOrBlue.setText(text);
            case 2 -> currentTurn.setText(text);
            case 3 -> redScore.setText(text);
            case 4 -> blueScore.setText(text);
        }
    }

    public class GamePanel extends JPanel {
        private Boolean gameOver = false;
        private final int SQUARE_SIZE = 50;
        private final int MIN_GRID_SIZE = 3;
        private final int MAX_GRID_SIZE = 12;
        private final int X_OFFSET = 15;
        private final int Y_OFFSET = 37;
        private final int DIAGONAL = (int) (SQUARE_SIZE * sqrt(2.0f));
        private int currentSize = MIN_GRID_SIZE;
        private Square[][] square;

        GamePanel() {
            populate(currentSize);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int rowCheck = e.getY() / SQUARE_SIZE;
                    int colCheck = e.getX() / SQUARE_SIZE;
                    makeMove(rowCheck, colCheck);
                    repaint();
                    gameOver = checkWin();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.GRAY);
            drawGrid(g);
            drawLetter(g);
            //g.drawLine(0,0, DIAGONAL * currentSize, DIAGONAL * currentSize);
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
                    //else if (square[rowLetter][colLetter].getValue() == Square.value.NULL) {   // WILL DELETE ??
                        //g2d.drawString("X", colLetter * SQUARE_SIZE + X_OFFSET, rowLetter * SQUARE_SIZE + Y_OFFSET);
                    //}
                }
            }
        }

        public void makeMove(int rowCheck, int colCheck) {
            if (!gameOver && square[rowCheck][colCheck].getValue() == Square.value.NULL) {   // need to check playerType
                if (!getTurn()) {
                    square[rowCheck][colCheck].setValue(red.getLetter());
                    red.setScore(red.getScore() + addToScore(rowCheck, colCheck));
                    changeText(3, String.valueOf(red.getScore()));
                    setTurn(true);
                    changeText(1, "Blue");
                } else {
                    square[rowCheck][colCheck].setValue(blue.getLetter());
                    blue.setScore(blue.getScore() + addToScore(rowCheck, colCheck));
                    changeText(4, String.valueOf(blue.getScore()));
                    setTurn(false);
                    changeText(1, "Red");
                }
            }
        }

        public void setGameSize(int size) {
            if (size > 2) {
                currentSize = size;
            }
        }

        public int getGameSize() {
            return currentSize;
        }

        public int getSquareSize() {
            return SQUARE_SIZE;
        }

        public Boolean isGameOver() {
            return gameOver;
        }

        public void setGameOver(Boolean which) {
            gameOver = which;
        }

        public void populate(int size) {   // will reset game
            setGameSize(size);
            setPreferredSize(new Dimension(currentSize * SQUARE_SIZE + 1, currentSize * SQUARE_SIZE + 1));
            square = new Square[currentSize][currentSize];
            for (int row = 0; row < currentSize; row++) {
                for (int col = 0; col < currentSize; col++) {
                    square[row][col] = new Square(Square.value.NULL);
                }
            }
        }

        private int addToScore(int row, int col) {
            int totalScore = 0;

            // check all around S
            if (row > 1 && col > 1 && row < currentSize - 3 && col < currentSize - 3) {
                if (square[row][col].getValue() == Square.value.S) {
                    if (square[row - 1][col - 1].getValue() == Square.value.O && square[row - 2][col - 2].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row - 1][col].getValue() == Square.value.O && square[row - 2][col].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row - 1][col + 1].getValue() == Square.value.O && square[row - 2][col + 2].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row + 1][col + 1].getValue() == Square.value.O && square[row + 2][col + 2].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row + 1][col - 1].getValue() == Square.value.O && square[row + 2][col - 2].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                    if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                        totalScore += 1;
                    }
                }
            }
            // check corners if S
            else if (row == 0 && col == 0) {
                if (square[row][col].getValue() == Square.value.S) {
                    if (square[row + 1][col + 1].getValue() == Square.value.O && square[row + 2][col + 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row][col + 1].getValue() == Square.value.O && square[row][col + 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                }
            }
            else if (row == 0 && col == currentSize - 1) {
                if (square[row][col].getValue() == Square.value.S) {
                    if (square[row + 1][col].getValue() == Square.value.O && square[row + 2][col].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row + 1][col - 1].getValue() == Square.value.O && square[row + 2][col - 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row][col - 1].getValue() == Square.value.O && square[row][col - 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                }
            }
            else if (row == currentSize - 1 && col == 0) {
                if (square[row][col].getValue() == Square.value.S) {
                    if (square[row - 1][col].getValue() == Square.value.O && square [row - 2][col].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row][col + 1].getValue() == Square.value.O && square [row][col + 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row - 1][col + 1].getValue() == Square.value.O && square [row - 2][col + 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                }
            }
            else if (row == currentSize - 1 && col == currentSize - 1) {
                if (square[row][col].getValue() == Square.value.S) {
                    if (square[row - 1][col].getValue() == Square.value.O && square [row - 2][col].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row][col - 1].getValue() == Square.value.O && square [row][col - 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row - 1][col - 1].getValue() == Square.value.O && square [row - 2][col - 2].getValue() == Square.value.S) {
                        totalScore++;
                    }
                }
            }
            // check O not in corner
            else if (row > 0 && col > 0 && row < currentSize - 1 && col < currentSize - 1) {
                if (square[row][col].getValue() == Square.value.O) {
                    if (square[row - 1][col].getValue() == Square.value.S && square[row + 1][col].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row][col - 1].getValue() == Square.value.S && square[row][col + 1].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row + 1][col - 1].getValue() == Square.value.S && square[row - 1][col + 1].getValue() == Square.value.S) {
                        totalScore++;
                    }
                    if (square[row - 1][col - 1].getValue() == Square.value.S && square[row + 1][col + 1].getValue() == Square.value.S) {
                        totalScore++;
                    }
                }
            }
            return totalScore;
        }

        public Boolean checkWin() {
            int squaresOccupied = 0;
            if (getGameType().equals("Simple Game")) {
                if (red.getScore() > 0) {
                    changeText(2, "Red Won!");
                    changeText(1, "");
                    return true;   // if any score, SOS has been made
                }
                else if (blue.getScore() > 0) {
                    changeText(2, "Blue Won!");
                    changeText(1, "");
                    return true;
                }
                for (int i = 0; i < currentSize; i++) {   // only do for loop if no win
                    for (int j = 0; j < currentSize; j++) {
                        if (square[i][j].getValue() != Square.value.NULL) {
                            squaresOccupied++;
                        }
                    }
                }
            }
            else if (getGameType().equals("General Game")) {
                for (int i = 0; i < currentSize; i++) {
                    for (int j = 0; j < currentSize; j++) {
                        if (square[i][j].getValue() == Square.value.NULL) {
                            return false;   // if any squares not occupied, game not over
                        }
                        squaresOccupied++;
                    }
                }
                if (red.getScore() > blue.getScore()) {
                    changeText(2, "Red Won!");
                    changeText(1, "");
                    return true;
                }
                else if (blue.getScore() > red.getScore()) {
                    changeText(2, "Blue Won!");
                    changeText(1, "");
                    return true;
                }
                //return true;   // CHECK IF NEEDS TO BE DELETED -- move up to if/else?
            }
            if(squaresOccupied == currentSize * currentSize) {   // if here and true, all occupied, and no score
                changeText(2, "It's a Draw!");
                changeText(1, "");
                return true;
            }
            return false;
        }

        public Square.value getSquareValue(int row, int col) {
            return square[row][col].getValue();
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}