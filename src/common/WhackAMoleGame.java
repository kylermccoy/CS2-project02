package common;

import java.util.ArrayList;

/**
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */
public class WhackAMoleGame implements Runnable {
    private ArrayList<WhackAMolePlayer> players;
    private int row;
    private int column;
    private int game_time;
    private WhackAMole game ;
    private GameTimer timer ;
    private boolean active ;
    private boolean closePlayers ;

    /**
     * Creates a new WhackAMoleGame
     * @param row rows of the game board
     * @param column columns of the game board
     * @param game_time the time that the game is run for
     */
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

    /**
     * adds a player to the ArrayList of players
     * @param player
     */
    public void addPlayer(WhackAMolePlayer player){
        players.add(player) ;
    }

    /**
     * Returns whether the mole is up when it whacked
     * @param mole_num the moles number
     */
    public boolean isValid(int mole_num){
        return game.isValid(mole_num) ;
    }

    /**
     * Sets the given mole to the boolean of whether or not it was whacked
     * @param mole_num the number of the mole
     * @param bool whether the mole was whacked or not
     */
    public void setWhacked(int mole_num, boolean bool){
        game.setWhacked(mole_num, bool);
    }

    /**
     * Returns whether the game is active or not
     * @return
     */
    public boolean isActive(){
        return active ;
    }

    /**
     * Puts the given mole up for each player
     * @param mole_num the mole number
     */
    public void moleUp(int mole_num){
        System.out.println("Mole Up at " + mole_num);
        for(WhackAMolePlayer player: players){
            player.moleUp(mole_num);
        }
    }

    /**
     * Puts down the given mole for each player
     * @param mole_num the mole number
     */
    public void moleDown(int mole_num){
        System.out.println("Mole Down at " + mole_num);
        for(WhackAMolePlayer player: players){
            player.moleDown(mole_num);
        }
    }

    /**
     * Returns a string of each players score with a space in between like: 1 0 3 5
     * @return score
     */
    public String getScore(){
        String score = "" ;
        for(WhackAMolePlayer player: players){
            score = score + player.getScore() + " " ;
        }
        return score ;
    }

    /**
     * returns if the player thread should start closing the sockets
     */
    public boolean closePlayers(){
        return closePlayers;
    }

    /**
     * The main run method. It starts the timer, starts the game, and checks the scores
     */
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
        int tied_check = 0 ;
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
