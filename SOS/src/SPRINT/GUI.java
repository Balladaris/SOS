package SPRINT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class GUI extends JFrame {
    private JLabel redOrBlue, currentTurn, redScore, blueScore;
    private Board board = new Board();
    private GamePanel game = new GamePanel();

    public GUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SOS");
        addPanels();
        setVisible(true);
    }

    private void resetGame(int size) {
        board = board.getGameType() ? new Board(size) : new GeneralBoard(size);
        addPanels();
        repaint();
    }

    private void addPanels() {   // initializing game
        Container content = getContentPane();
        content.removeAll();
        content.add(generateSidePanel(board.red), BorderLayout.EAST);
        content.add(generateSidePanel(board.blue), BorderLayout.WEST);
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
        pnl.setPreferredSize(new Dimension(100, 200));

        human = new JRadioButton("Human");
        human.addActionListener(e -> player.setPlayerType("Human"));
        human.doClick();
        sButton = new JRadioButton("S");

        sButton.addActionListener(e -> player.setLetter(Square.value.S));
        sButton.doClick();
        oButton = new JRadioButton("O");
        oButton.addActionListener(e -> player.setLetter(Square.value.O));
        computer = new JRadioButton("Computer");
        computer.addActionListener(e -> {
            player.setPlayerType("Computer");
            if (board.getTurn() && player.getColor().equals(Color.BLUE) || !board.getTurn() && player.getColor().equals(Color.RED)) {
                try {
                    board.makeMoveComputer();
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
            repaint();
            if (!board.getGameType()) {   // if general game, update score text
                changeText(3, String.valueOf(board.red.getScore()));
                changeText(4, String.valueOf(board.blue.getScore()));
            }
            game.makeMoveText();
        });
        playerSelect.add(human);
        letterSelect.add(sButton);
        letterSelect.add(oButton);
        playerSelect.add(computer);

        if (!board.getGameType()) {   // add score if general game
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
            board = new Board(board.getGameSize());
            addPanels();
            repaint();
        });
        general.addActionListener(e -> {
            board = new GeneralBoard(board.getGameSize());
            addPanels();
            repaint();
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

        (board.getGameType() ? simple : general).setSelected(true);
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
        JPanel game1 = new JPanel();
        gameBoard.setPreferredSize(new Dimension(board.getGameSize() * board.getSquareSize() + 1,
                board.getGameSize() * board.getSquareSize() + 1));
        game1.add(gameBoard);
        return game1;
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
        newGame.addActionListener(e -> {
            board.setGameOver(false);
            resetGame(board.getGameSize());
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

    public void resize (String input) {
        try {
            resetGame(Integer.parseInt(input));
            repaint();
        }
        catch(NumberFormatException ignored) {   // ignore anything not integer

        }
    }

    public void changeText(int i, String text) {
        switch (i) {
            case 1 -> redOrBlue.setText(text);
            case 2 -> currentTurn.setText(text);
            case 3 -> redScore.setText(text);
            case 4 -> blueScore.setText(text);
        }
    }

    private class GamePanel extends JPanel {
        GamePanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int rowCheck = e.getY() / board.getSquareSize();
                    int colCheck = e.getX() / board.getSquareSize();
                    try {   // needed for makeMoveComputer
                        board.makeMove(rowCheck, colCheck);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    makeMoveText();
                    if (!board.getGameType()) {   // if general game, update score text
                        changeText(3, String.valueOf(board.red.getScore()));
                        changeText(4, String.valueOf(board.blue.getScore()));
                    }
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.GRAY);
            drawGrid(g);
            drawLetter(g);
            drawWins(g);
        }

        private void drawGrid(Graphics g) {
            for (int rowGrid = 0; rowGrid < board.getGameSize(); rowGrid++) {
                for (int colGrid = 0; colGrid < board.getGameSize(); colGrid++) {
                    g.setColor(Color.BLACK);
                    g.drawRect(rowGrid * board.getSquareSize(), colGrid * board.getSquareSize(), board.getSquareSize(), board.getSquareSize());
                }
            }
        }

        private void drawLetter(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int x_OFFSET = 15;
            int y_OFFSET = 37;
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 30));
            for (int rowLetter = 0; rowLetter < board.getGameSize(); rowLetter++) {
                for (int colLetter = 0; colLetter < board.getGameSize(); colLetter++) {
                    if (board.getSquareValue(rowLetter, colLetter) == Square.value.S) {
                        g2d.drawString("S", colLetter * board.getSquareSize() + x_OFFSET, rowLetter * board.getSquareSize() + y_OFFSET);
                    } else if (board.getSquareValue(rowLetter, colLetter) == Square.value.O) {
                        g2d.drawString("O", colLetter * board.getSquareSize() + x_OFFSET, rowLetter * board.getSquareSize() + y_OFFSET);
                    }
                }
            }
        }

        public void drawWins(Graphics g) {
            if (board.getLineCounter() > 0) {
                for (int i = 0; i < board.getLineCounter(); i++) {
                    g.setColor(board.getLineColor(i));
                    g.drawLine(board.getLineDouble(1, i), board.getLineDouble(2, i), board.getLineDouble(3, i), board.getLineDouble(4, i));
                }
            }
        }

        private void makeMoveText() {
            if (!board.getTurn()) {   // will place this first
                changeText(1, "Red");
            } else {
                changeText(1, "Blue");
            }
            if (board.red.getScore() > board.blue.getScore() && board.getGameOver()) {   // place this if gameOver
                changeText(2, "Red Won!");
                changeText(1, "");
            } else if (board.blue.getScore() > board.red.getScore() && board.getGameOver()) {
                changeText(2, "Blue Won!");
                changeText(1, "");
            } else if (board.getGameOver()) {
                changeText(2, "It's a Draw!");
                changeText(1, "");
            }
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}