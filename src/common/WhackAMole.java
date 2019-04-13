package common;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WhackAMole {

    private int rows ;
    private int columns ;
    private ArrayList<Moles> moles ;
    private WhackAMoleGame game ;
    private boolean active ;

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

    public boolean isValid(int mole_num){
        return moles.get(mole_num).isUp() ;
    }

    public boolean isActive(){
        return active ;
    }

    public void startGame(int game_time){
        try{
            active = true ;
            System.out.println("Starting mole threads!");
            for(Moles mole: moles){
                mole.start();
            }
            System.out.println("Starting timer!");
            sleep(game_time * 1000) ;
            active = false ;
            System.out.println("Timer finished!");
        }
        catch(InterruptedException ex){
            System.out.println("Interrupted Exception!");
        }
    }
}
