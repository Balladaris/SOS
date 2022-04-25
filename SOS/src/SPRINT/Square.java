package SPRINT;

public class Square {
    private value val;

    Square(value value) {
        setValue(value);
    }

    public void setValue(Square.value value) {
        this.val = value;
    }

    public Square.value getValue() {
        return val;
    }

    public enum value {
        S, O, NULL
    }
}