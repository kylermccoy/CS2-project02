package common;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WhackAMoleGame implements Runnable {
    private ArrayList<WhackAMolePlayer> players ;
    private int row ;
    private int column ;
    private int game_time ;
    private WhackAMole game ;
    private boolean active ;

    public WhackAMoleGame(ArrayList players, int row, int column, int game_time){
        this.players = players ;
        this.row = row ;
        this.column = column ;
        this.game_time = game_time ;
        game = new WhackAMole(row, column, this) ;
    }

    public boolean isActive(){
        return active ;
    }

    @Override
    public void run(){
        active = true ;
        try{
            sleep(game_time * 1000) ;
        }
        catch(InterruptedException ex){
            System.out.println("Interrupted Exception!");
        }
        active = false ;
        for(WhackAMolePlayer player: players){
            player.close();
        }
    }
}
