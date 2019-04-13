package common;

import client.gui.Observer;
import javafx.scene.control.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WhackAMoleBoard {
    private int rows;
    private int cols;
    private Status status;
    private int[] molecheck;
    private String[] scores;


    private List<Observer<WhackAMoleBoard>> observers ;

    public enum Status{
        I_WON, I_LOST, TIE, ERROR;
    }

    public WhackAMoleBoard(int rows, int columns){
        this.observers = new LinkedList<>() ;
        this.rows = rows ;
        this.cols = columns ;
        this.molecheck = new int[rows*columns];
    }

    public int getColumns(){
        return cols ;
    }

    public int getRows(){
        return rows ;
    }

    public void alertObservers(){
        for(Observer<WhackAMoleBoard> obs: this.observers){
            obs.update(this) ;
        }
    }

    public Status getStatus(){return this.status;}

    public void gameWon(){
        this.status = Status.I_WON;
        alertObservers();
    }

    public void gameLost(){
        this.status = Status.I_LOST;
        alertObservers();
    }

    public void gameTied(){
        this.status = Status.TIE;
        alertObservers();
    }

    public void error(){
        this.status = Status.ERROR;
        alertObservers();
    }

    public void close(){
        alertObservers();
    }

    public void moleUp(int mole_number){
        this.molecheck[mole_number] = 1;
    }

    public void moleDown(int mole_number){
        this.molecheck[mole_number] = 0;
    }

    public int[] getMolecheck(){
        return this.molecheck;
    }

    public void score(String[] scores){
        this.scores = scores;
    }

    public String[] getScores(){
        return this.scores;
    }
}
