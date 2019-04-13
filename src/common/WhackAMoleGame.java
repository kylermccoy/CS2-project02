package common;

import java.util.ArrayList;

public class WhackAMoleGame implements Runnable {
    private ArrayList<WhackAMolePlayer> players;
    private int row;
    private int column;
    private int game_time;
    private WhackAMole game ;
    private GameTimer timer ;
    private boolean active ;
    private boolean closePlayers ;

    public WhackAMoleGame(int row, int column, int game_time) {
        players = new ArrayList<>();
        this.row = row;
        this.column = column;
        this.game_time = game_time;
        game = new WhackAMole(row, column, this) ;
        timer = new GameTimer(game_time) ;
        active = false ;
        closePlayers = false ;
    }

    public void addPlayer(WhackAMolePlayer player){
        players.add(player) ;
    }

    public boolean isValid(int mole_num){
        return game.isValid(mole_num) ;
    }

    public void setWhacked(int mole_num, boolean bool){
        game.setWhacked(mole_num, bool);
    }

    public boolean isActive(){
        return active ;
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

    public String getScore(){
        String score = "" ;
        for(WhackAMolePlayer player: players){
            score = score + player.getScore() + " " ;
        }
        return score ;
    }

    public boolean closePlayers(){
        return closePlayers;
    }

    public void run(){
        active = true ;
        timer.start();
        System.out.println("Starting mole threads!");
        game.startGame();
        try {
            timer.join();
        }
        catch (InterruptedException e){
            System.out.println("Interrupted!");
        }
        active = false ;
        System.out.println("Ending game!");
        System.out.println("Sending game messages!");
        int tied_check = 1 ;
        int best_score = players.get(0).getScore() ;
        for(WhackAMolePlayer player: players){
            if(best_score < player.getScore()){
                best_score = player.getScore() ;
                tied_check = 1 ;
            }
            if(best_score == player.getScore()){
                tied_check++ ;
            }
        }
        if(tied_check > 1){
            for(WhackAMolePlayer play: players){
                if(play.getScore() == best_score){
                    play.gameTied();
                }else{
                    play.gameLost();
                }
            }
        }else{
            for(WhackAMolePlayer pl: players){
                if(pl.getScore()==best_score){
                    pl.gameWon();
                }else{
                    pl.gameLost();
                }
            }
        }
        System.out.println("Closing player sockets!");
        closePlayers = true ;
        System.out.println("Closing Server!");
        System.exit(0);
    }
}
