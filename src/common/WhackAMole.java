package common;

import java.util.ArrayList;

public class WhackAMole {

    private int rows ;
    private int columns ;
    private ArrayList<Moles> moles ;

    public WhackAMole(int rows, int columns) {
        this.rows = rows ;
        this.columns = columns ;
        this.moles = new ArrayList<>() ;
    }
}
