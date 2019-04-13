package common;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;

public class WhackAMolePlayer extends Thread implements Closeable {
    private Socket socket ;

    private Scanner scanner ;

    private PrintStream printStream ;

    private int player_number ;
    private int score ;
    private WhackAMoleGame game ;

    public void run(){
        while(!game.closePlayers()) {
            try {
                String response = scanner.nextLine();
                String[] tokens = response.split(" ");
                switch (tokens[0]) {
                    case WAMProtocol.WHACK:
                        if (game.isValid(Integer.parseInt(tokens[1]))) {
                            addPoints();
                            game.setWhacked(Integer.parseInt(tokens[1]),true);
                        } else {
                            subPoints();
                        }
                        moleDown(Integer.parseInt(tokens[1]));
                        break;
                }
            }
            catch (NoSuchElementException e){
                this.close();
            }
        }
    }

    public WhackAMolePlayer(Socket socket, int player_num, WhackAMoleGame game) {
        score = 0 ;
        this.game = game ;
        this.socket = socket ;
        this.player_number = player_num ;
        try {
            scanner = new Scanner(socket.getInputStream()) ;
            printStream = new PrintStream(socket.getOutputStream()) ;
        }
        catch(IOException e){
            System.out.println("Player unable to be created!");
        }
    }

    public int getScore(){
        return this.score ;
    }

    public void addPoints(){
        this.score += 2 ;
    }

    public void subPoints(){
        this.score-- ;
    }

    public void connect(int row, int column, int num_players, int player_num){
        printStream.println(WELCOME + " " + row + " " + column + " " + num_players + " " + player_num);
    }

    public void moleUp(int mole_num){
        printStream.println(MOLE_UP + " " + mole_num) ;
    }

    public void moleDown(int mole_num){
        printStream.println(MOLE_DOWN + " " + mole_num) ;
        printStream.println(SCORE + " " + game.getScore());
        System.out.println(game.getScore());
    }


    public void gameWon(){
        printStream.println(GAME_WON) ;
    }

    public void gameLost(){
        printStream.println(GAME_LOST) ;
    }

    public void gameTied(){
        printStream.println(GAME_TIED);
    }

    public void error(String err){
        printStream.println(ERROR + " " + err);
        close();
    }

    @Override
    public void close() {
        try{
            socket.close();
        }
        catch(IOException e){
            System.out.println("Failed close!");
        }
    }
}
