package SPRINT;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// FOCUS ON UPDATING BOTTOM TEXT AND SIMPLE/GENERAL GAME

public class GUI extends JFrame {
    public GamePanel game = new GamePanel();
    public static Player red = new Player(Color.RED);
    public static Player blue = new Player(Color.BLUE);

    GUI() {
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
        JLabel playerText = generatePlayerText(player);
        ButtonGroup playerSelect = new ButtonGroup();
        ButtonGroup letterSelect = new ButtonGroup();

        pnl.setLayout(new FlowLayout());
        //pnl.setBackground(new Color(80, 20, 10));
        pnl.setPreferredSize(new Dimension(100, 200));

        human = new JRadioButton("Human");
        human.addActionListener(e -> {
            player.setPlayerType("Human");
        });
        sButton = new JRadioButton("S");
        sButton.addActionListener(e -> {
            player.setLetter(Square.value.S);
        });
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

        pnl.add(playerText);
        pnl.add(human);
        pnl.add(sButton);
        pnl.add(oButton);
        pnl.add(computer);
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
        /*
        simple.addActionListener(e -> {   // will need a check for SOS function

        });

        general.addActionListener(e -> {

        });
        */
        size.addActionListener(e -> {
            String input = size.getText();
            game.populate(Integer.parseInt(input));
            resetGame();
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
        gameBoard.setPreferredSize(new Dimension(gameBoard.getGameSize() * gameBoard.SQUARE_SIZE + 1,
                gameBoard.getGameSize() * gameBoard.SQUARE_SIZE + 1));
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
        newGame.addActionListener(e -> resetGame());

        JPanel turnPanel = new JPanel();
        JLabel currentTurn = generateLabel("Current turn: ", 12);
        JLabel redOrBlue = generateLabel("blue (or red)", 12);
        /*
        redOrBlue.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Object source = evt.getSource();   // DOESN'T WORK AS IS
                if (source == game && red.isTurn) {   // if game updates / turn changes
                    redOrBlue.setText("red");
                }
                else {
                    redOrBlue.setText("blue");
                }
            }
        });
        */
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
            blue.isTurn = true;   // initialize game with blue turn
        }
        text.setFont(new Font("SansSerif", Font.BOLD, 15));
        return text;
    }

    public static void main(String[] args) {
        new GUI();
    }
}