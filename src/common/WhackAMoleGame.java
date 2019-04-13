package common;

import java.util.ArrayList;

public class WhackAMoleGame implements Runnable {
    private ArrayList<WhackAMolePlayer> players ;
    private int row ;
    private int column ;
    private int game_time ;
    private WhackAMole game ;
    private ArrayList<Integer> player_scores ;

    public WhackAMoleGame(ArrayList players, int row, int column, int game_time){
        player_scores = new ArrayList<>() ;
        this.players = players ;
        this.row = row ;
        this.column = column ;
        this.game_time = game_time ;
        for(WhackAMolePlayer player: this.players){
            player_scores.add(player.getScore()) ;
        }
        game = new WhackAMole(row, column, this) ;
    }

    public boolean isActive(){
        return game.isActive() ;
    }

    public void moleUp(int number){
        for(WhackAMolePlayer player: players){
            player.moleUp(number);
        }
    }

    public void moleDown(int number){
        for(WhackAMolePlayer player: players){
            player.moleDown(number);
        }
    }

    @Override
    public void run(){
        game.startGame(game_time);
        while(isActive()){
            for(WhackAMolePlayer player: players){
                String response = player.getResponse() ;
                String[] tokens = response.split(" ") ;
                switch (tokens[0]){
                    case WAMProtocol.WHACK:
                        if(game.isValid(Integer.parseInt(tokens[1]))){
                            players.get(Integer.parseInt(tokens[2])).addPoints();
                        }else{
                            players.get(Integer.parseInt(tokens[2])).subPoints();
                        }
                        for(WhackAMolePlayer play: players){
                            play.moleDown(Integer.parseInt(tokens[1]));
                            play.score(player_scores);
                        }
                        break;
                }
            }
        }
        System.out.println("Calculating scores!");
        int number_of_best = 1 ;
        int best_score = player_scores.get(0);
        for(int score: player_scores){
            if(score > best_score){
                best_score = score ;
            }
            if(score == best_score){
                number_of_best++ ;
            }
        }
        System.out.println("Sending winning messages!");
        if(number_of_best == 1) {
            int index_best = player_scores.indexOf(best_score);
            for(WhackAMolePlayer player: players){
                if(player.getScore()==best_score){
                    player.gameWon();
                }else{
                    player.gameLost();
                }
            }
        }else{
            for(WhackAMolePlayer player: players){
                if(player.getScore()==best_score){
                    player.gameTied();
                }else{
                    player.gameLost();
                }
            }
        }
        System.out.println("Closing clients!");
        for(WhackAMolePlayer player: players){
            player.close();
        }
        System.exit(0);
    }
}
