package common;

import client.gui.Observer;

import java.util.LinkedList;
import java.util.List;

public class WhackAMoleBoard {
    private int rows;
    private int cols;


    private List<Observer<WhackAMoleBoard>> observers ;

    public WhackAMoleBoard(int rows, int columns){
        this.observers = new LinkedList<>() ;
        this.rows = rows ;
        this.cols = columns ;
    }

    public int getColumns(){
        return cols ;
    }

    public int getRows(){
        return rows ;
    }

    public void addObserver(Observer<WhackAMoleBoard> observer){
        this.observers.add(observer) ;
    }

    public void alertObservers(){
        for(Observer<WhackAMoleBoard> obs: this.observers){
            obs.update(this) ;
        }
    }

    public void close(){
        alertObservers();
    }

    public void moleUp(int mole_number){

    }

    public void moleDown(int mole_number){

    }

    public void whack(int mole_number, int player_number){

    }

}
