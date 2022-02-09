package SPRINT;

public class Square {
    private value value;

    Square(value value) {
        setValue(value);
    }

    public void setValue(Square.value value) {
        this.value = value;
    }

    public Square.value getValue() {
        return value;
    }

    public enum value {
        S, O, NULL
    }
}
