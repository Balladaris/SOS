package SPRINT;

import java.awt.Color;

public class Player {
    private Color color;
    private String playerType;
    private Square.value letter;
    private int score = 0;

    Player(Color color) {
        setColor(color);
        setLetter(Square.value.S);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPlayerType() {   // used to place letter/countdown (?) place random letter
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public Square.value getLetter() {
        return letter;
    }

    public void setLetter(Square.value letter) {
        this.letter = letter;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score =  score;
    }
}