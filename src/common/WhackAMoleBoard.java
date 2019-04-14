package common;

import client.gui.Observer;
import javafx.scene.control.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */
public class WhackAMoleBoard {
    private int rows;
    private int cols;
    private Status status;
    private int[] molecheck;
    private String[] scores;


    private List<Observer<WhackAMoleBoard>> observers ;

    /**
     * Enums used to determine the status of the game
     */
    public enum Status{
        I_WON, I_LOST, TIE, ERROR;
    }

    /**
     * Constructor for the game bord
     * @param rows rows in the game
     * @param columns columns in the game
     */
    public WhackAMoleBoard(int rows, int columns){
        this.observers = new LinkedList<>() ;
        this.rows = rows ;
        this.cols = columns ;
        this.molecheck = new int[rows*columns];
    }

    /**
     * Returns the columns
     */
    public int getColumns(){
        return cols ;
    }

    /**
     * Returns the rows
     */
    public int getRows(){
        return rows ;
    }

    /**
     * Adds the observer for the MVC to the observer list
     * @param observer
     */
    public void addObserver(Observer<WhackAMoleBoard> observer){
        this.observers.add(observer);
    }

    /**
     * Alerts the observers for all the observers in the linked list to update
     */
    public void alertObservers(){
        for(Observer<WhackAMoleBoard> obs: this.observers){
            obs.update(this) ;
        }
    }

    /**
     * Returns the status of the game
     */
    public Status getStatus(){return this.status;}

    /**
     * Sets the status to WON and alerts the observers
     */
    public void gameWon(){
        this.status = Status.I_WON;
        alertObservers();
    }

    /**
     * Sets the status of the game to LOST and alerts the observers
     */
    public void gameLost(){
        this.status = Status.I_LOST;
        alertObservers();
    }

    /**
     * Sets the status of the game to TIED and alerts the observers
     */
    public void gameTied(){
        this.status = Status.TIE;
        alertObservers();
    }

    /**
     * Sets the status of the game to ERROR and alerts the observers
     */
    public void error(){
        this.status = Status.ERROR;
        alertObservers();
    }

    /**
     * User can close the game at any time
     */
    public void close(){
        alertObservers();
    }

    /**
     * Sets the given mole in the array to 1 which means it is UP meaning the mole pops up on the GUI
     * @param mole_number the moles number
     */
    public void moleUp(int mole_number){
        this.molecheck[mole_number] = 1;
        alertObservers();
    }

    /**
     * Sets the given mole in the array to 0 meaning it is down, so the mole is set to down in the GUI
     * @param mole_number
     */
    public void moleDown(int mole_number){
        this.molecheck[mole_number] = 0;
        alertObservers();
    }

    /**
     * Returns the array of the moles that are up and down. 0 if they are down, and 1 if they are up
     */
    public int[] getMolecheck(){
        return this.molecheck;
    }

    /**
     * Sets the String array of scores given to the method to the scores variable
     * @param scores an array of each players score
     */
    public void score(String[] scores){
        this.scores = scores;
        System.out.println(Arrays.toString(scores));
        System.out.println(scores[0]);
    }

    /**
     * Returns the String array of the scores of each player
     */
    public String[] getScores(){
        return this.scores;
    }
}
