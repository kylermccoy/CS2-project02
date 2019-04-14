package client.gui;

import common.WAMProtocol;
import common.WhackAMoleBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The client side of the Whack a mole game. Each player gets its own connection to the server.
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */

public class WhackAMoleNetworkClient {

    private Socket clientSocket ;
    private Scanner scanner ;
    private PrintStream printStream ;
    private WhackAMoleBoard board ;

    private boolean go ;

    private int columns ;
    private int rows ;
    private int total_players ;
    private int player_num ;

    /**
     * Hook the client up with the Whack a mole server only if it is running and waiting for
     * players to connect. Only works if the first message that comes from the server is WELCOME
     *
     * @param host the name of the host running the server program
     * @param port the port of the server socket on which the server is listening
     */
    public WhackAMoleNetworkClient(String host, int port){
        try{
            this.clientSocket = new Socket(host, port) ;
            this.scanner = new Scanner(clientSocket.getInputStream()) ;
            this.printStream = new PrintStream(clientSocket.getOutputStream()) ;
            String welcome_response = scanner.nextLine() ;
            if(!welcome_response.startsWith(WAMProtocol.WELCOME)){
                System.out.println("ERROR IN SERVER RESPONSE!");
            }else{
                String[] tokens = welcome_response.split(" ") ;
                rows = Integer.parseInt(tokens[1]) ;
                columns = Integer.parseInt(tokens[2]) ;
                total_players = Integer.parseInt(tokens[3]) ;
                player_num = Integer.parseInt(tokens[4]) ;
            }
            this.board = new WhackAMoleBoard(rows, columns) ;
            System.out.println("Connected to server!");
            go = true ;
        }
        catch (IOException e){
            System.out.println("Cannot connect to server!");
        }
    }

    /**
     * Returns the board of Whack a mole
     * @return
     */
    public WhackAMoleBoard getBoard(){
        return this.board ;
    }

    /**
     * Returns the number of players
     */
    public int getTotal_players(){
        return total_players ;
    }

    /**
     * Checks to see if the main loop is good to continue or not
     * @return
     */
    public synchronized boolean goodToGo(){
        return this.go ;
    }

    /**
     * Called from the GUI when it is ready to start receiving messages from the server
     */
    public void startListener(){
        new Thread(() ->this.run()).start();
    }

    /**
     * stops the loop
     */
    public void stop(){
        this.go = false ;
    }

    /**
     * Closes the client connection
     */
    public void close(){
        try{
            this.clientSocket.close();
        }
        catch (IOException e){
            System.out.println("Failed to close client network!");
        }
        this.board.close();
    }

    /**
     * Sends which player hit the mole
     * @param id
     */
    public void sendWhack(int id){
        this.printStream.println(WAMProtocol.WHACK + " " + id + " " + this.player_num);
    }

    /**
     * Runs the main client loop.
     */
    private void run(){
        while(this.goodToGo()){
            try{
                String request = this.scanner.next();
                String response = scanner.nextLine() ;
                String[] tokens = response.trim().split(" ") ;
                System.out.println(Arrays.toString(tokens)+ "MEMES");
                switch(request){
                    case WAMProtocol.MOLE_UP:
                        this.board.moleUp(Integer.parseInt(tokens[0])) ;
                        break ;
                    case WAMProtocol.MOLE_DOWN:
                        this.board.moleDown(Integer.parseInt(tokens[0])) ;
                        String scores = this.scanner.next();
                        if(scores.equals(WAMProtocol.SCORE)){
                            String[] meme = scanner.nextLine().trim().split(" ");
                            this.board.score(meme);
                        }
                        break ;
                    case WAMProtocol.ERROR:
                        close();
                        break;
                    case WAMProtocol.GAME_LOST:
                        this.board.gameLost();
                        this.stop();
                        break;
                    case WAMProtocol.GAME_WON:
                        this.board.gameWon();
                        this.stop();
                        break;
                    case WAMProtocol.GAME_TIED:
                        this.board.gameTied();
                        this.stop();
                        break;
                    case WAMProtocol.SCORE:
                        System.out.println("FUCK UP");
                        this.board.score(tokens);
                        break;
                }
            }
            catch (NoSuchElementException e){
            }
        }
    }
}
