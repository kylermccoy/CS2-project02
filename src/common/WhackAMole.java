package common;

import java.util.ArrayList;

public class WhackAMole {

    private int rows ;
    private int columns ;
    private ArrayList<Moles> moles ;
    private WhackAMoleGame game ;

    public WhackAMole(int rows, int columns, WhackAMoleGame game) {
        this.rows = rows ;
        this.columns = columns ;
        this.game = game ;
        this.moles = new ArrayList<>() ;
        for(int id = 0; id < rows * columns; id++){
            Moles mole = new Moles(id, game) ;
            moles.add(mole) ;
        }
    }

    public void startGame(){
        for(Moles mole: moles){
            mole.start();
        }
    }
}
