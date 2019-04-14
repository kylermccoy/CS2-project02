package common;

import java.util.ArrayList;

/**
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */

public class WhackAMole {

    private int rows ;
    private int columns ;
    private ArrayList<Moles> moles ;
    private WhackAMoleGame game ;

    /**
     * The array for the threads in the game
     * @param rows rows in the game
     * @param columns columns in the game
     * @param game the actual game
     */
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

    /**
     * Sets the specified moles whacked parameter to the given boolean.
     * @param mole_num the moles number
     * @param bool the boolean whether or not it was whacked
     */
    public void setWhacked(int mole_num, boolean bool){
        moles.get(mole_num).setWhacked(bool);
    }

    /**
     * Returns whether the mole is up and the mole hasnt been whacked
     * @param mole_num the moles number
     * @return
     */
    public boolean isValid(int mole_num){
        return moles.get(mole_num).isUp() && !moles.get(mole_num).gotWhacked() ;
    }

    /**
     * Starts all the mole threads for the game
     */
    public void startGame(){
        for (Moles mole : moles) {
            mole.start();
        }
    }
}
