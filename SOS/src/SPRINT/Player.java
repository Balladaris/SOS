package SPRINT;

import java.awt.*;

public class Player {
    private Color color;
    private String playerType;
    private Square.value letter;
    public Boolean isTurn = false;   // to check if red or blue turn when letter placed??

    Player(Color color) {
        setColor(color);
        setLetter(Square.value.NULL);
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
}
