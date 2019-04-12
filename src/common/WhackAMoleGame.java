package common;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class WhackAMoleGame implements Runnable {
    private ArrayList<WhackAMolePlayer> players ;
    private int row ;
    private int column ;
    private int game_time ;
    private WhackAMole game ;

    public WhackAMoleGame(ArrayList players, int row, int column, int game_time){
        this.players = players ;
        this.row = row ;
        this.column = column ;
        this.game_time = game_time ;
        game = new WhackAMole() ;
    }

    @Override
    public void run(){
        while(game_time > 0){
            try {
                sleep(1000);
                game_time--;
            }
            catch(InterruptedException e){
                System.out.println("Sleep failed!");
            }
        }
        for(WhackAMolePlayer player: players){
            player.close();
        }
    }
}
