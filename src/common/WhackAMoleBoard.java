package common;

public class WhackAMoleBoard {
    public int rows;
    public int cols;
    private Status status;

    public enum Status {
        I_WON, I_LOST, TIE, ERROR, NOT_OVER;
    }

    public WhackAMoleBoard(int cols, int rows){
        this.cols = cols;
        this.rows = rows;

    }

}
