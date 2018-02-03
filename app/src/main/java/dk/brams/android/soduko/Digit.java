package dk.brams.android.soduko;

public class Digit {
    int row;
    int col;
    int value;

    public Digit(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public String getValue() {
        return (value==0?" ":Integer.toString(value));
    }

    public int getIntValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}