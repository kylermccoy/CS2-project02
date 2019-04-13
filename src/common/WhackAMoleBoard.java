package common;

import client.gui.Observer;
import client.gui.WAMGUI;
import javafx.scene.control.Button;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WhackAMoleBoard {
    private int rows;
    private int cols;
    private Status status;
    private ArrayList<Button> buttons;


    private List<Observer<WhackAMoleBoard>> observers ;

    public enum Status{
        I_WON, I_LOST, TIE, ERROR;
    }

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

    }

    public void moleDown(int mole_number){

    }

    public void whack(int mole_number, int player_number){

    }

}
