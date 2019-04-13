package client.gui;

import common.WAMProtocol;
import common.WhackAMoleBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WhackAMoleNetworkClient {

    private Socket clientSocket ;
    private Scanner scanner ;
    private PrintStream printStream ;
    private WhackAMoleBoard board ;

    private boolean go ;

    private int columns ;
    private int rows ;
    private int total_players ;
    private int game_time_seconds ;

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
                rows = Integer.parseInt(tokens[2]) ;
                columns = Integer.parseInt(tokens[3]) ;
                total_players = Integer.parseInt(tokens[4]) ;
                game_time_seconds = Integer.parseInt(tokens[5]) ;
            }
            this.board = new WhackAMoleBoard(rows, columns) ;
            System.out.println("Connected to server!");
            go = true ;
        }
        catch (IOException e){
            System.out.println("Cannot connect to server!");
        }
    }

    public boolean goodToGo(){
        return this.go ;
    }

    public void stop(){
        this.go = false ;
    }

    public void startListener(){
        new Thread(()->this.run()).start();
    }

    public void close(){
        try{
            this.clientSocket.close();
        }
        catch (IOException e){
            System.out.println("Failed to close client network!");
        }
    }

    public void run(){
        while(this.goodToGo()){
            try{
                String response = scanner.nextLine() ;
                String[] tokens = response.split(" ") ;
                switch(tokens[0]){
                    case WAMProtocol.MOLE_UP:
                        this.board.moleUp(Integer.parseInt(tokens[1])) ;
                        break ;
                    case WAMProtocol.MOLE_DOWN:
                        this.board.moleDown(Integer.parseInt(tokens[1])) ;
                        break ;
                    case WAMProtocol.WHACK:
                        this.board.whack(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2])) ;
                        break;
                    case WAMProtocol.ERROR:
                }
            }
            catch (NoSuchElementException e){
                System.out.println("Lost connection to server!");
                this.stop();
            }
        }
    }
}
