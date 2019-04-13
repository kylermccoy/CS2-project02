package common;

import java.util.ArrayList;

public class WhackAMoleGame implements Runnable {
    private ArrayList<WhackAMolePlayer> players;
    private int row;
    private int column;
    private int game_time;
    private WhackAMole game ;

    public WhackAMoleGame(int row, int column, int game_time) {
        players = new ArrayList<>();
        this.row = row;
        this.column = column;
        this.game_time = game_time;
        game = new WhackAMole(row, column, this) ;
    }

    public void addPlayer(WhackAMolePlayer player){
        players.add(player) ;
    }

    public boolean isValid(int mole_num){
        return game.isValid(mole_num) ;
    }

    public boolean isActive(){
        return game.isActive() ;
    }

    public void moleUp(int mole_num){
        System.out.println("Mole Up at " + mole_num);
        for(WhackAMolePlayer player: players){
            player.moleUp(mole_num);
        }
    }

    public void moleDown(int mole_num){
        System.out.println("Mole Down at " + mole_num);
        for(WhackAMolePlayer player: players){
            player.moleDown(mole_num);
        }
    }

    public int getTime(){
        return game_time ;
    }

    public String getScore(){
        String score = "" ;
        for(WhackAMolePlayer player: players){
            score = score + player.getScore() + " " ;
        }
        return score ;
    }

    public void run(){
        System.out.println("Game starting!");
        game.startGame() ;
    }
}
