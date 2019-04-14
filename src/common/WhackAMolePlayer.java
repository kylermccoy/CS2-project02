package common;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static common.WAMProtocol.*;

/**
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */
public class WhackAMolePlayer extends Thread implements Closeable {
    private Socket socket ;

    private Scanner scanner ;

    private PrintStream printStream ;

    private int player_number ;
    private int score ;
    private WhackAMoleGame game ;

    /**
     * The main run method for the player. It runs while the boolean of whether or not the players sockets should close
     * is false. It then checks whether the protocol given is whack and either whacks the mole and adds points, or
     * subtracts points from the player.
     */
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

    /**
     * The constructor for the player
     * @param socket the socket given
     * @param player_num the number of the player
     * @param game the game
     */
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

    /**
     * Returns the score of the player
     * @return
     */
    public int getScore(){
        return this.score ;
    }

    /**
     * Add two points two the players score
     */
    public void addPoints(){
        this.score += 2 ;
    }

    /**
     * Subtracts 1 point from the players score
     */
    public void subPoints(){
        this.score-- ;
    }

    /**
     * The connect protocol that sends the message WELCOME the row, column, number of players, and player number
     * @param row rows of the game
     * @param column columns of the game
     * @param num_players the number of players in the game
     * @param player_num the number of the player
     */
    public void connect(int row, int column, int num_players, int player_num){
        printStream.println(WELCOME + " " + row + " " + column + " " + num_players + " " + player_num);
    }

    /**
     * The mole up protocol that tells which mole to put up
     * @param mole_num the mole number
     */
    public void moleUp(int mole_num){
        printStream.println(MOLE_UP + " " + mole_num) ;
    }

    /**
     * The mole down protocol
     * @param mole_num
     */
    public void moleDown(int mole_num){
        printStream.println(MOLE_DOWN + " " + mole_num) ;
        printStream.println(SCORE + " " + game.getScore());
        System.out.println(game.getScore());
    }


    /**
     * The game won protocol
     */
    public void gameWon(){
        printStream.println(GAME_WON) ;
    }

    /**
     * The game lost protocol
     */
    public void gameLost(){
        printStream.println(GAME_LOST) ;
    }

    /**
     * The game tied protocol
     */
    public void gameTied(){
        printStream.println(GAME_TIED);
    }

    /**
     * The Error protocol
     * @param err
     */
    public void error(String err){
        printStream.println(ERROR + " " + err);
        close();
    }

    /**
     * Closes the socket of the player
     */
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
