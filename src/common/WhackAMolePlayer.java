package common;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static common.WAMProtocol.*;

public class WhackAMolePlayer implements Closeable {
    private Socket socket ;

    private Scanner scanner ;

    private PrintStream printStream ;

    private int player_number ;

    public WhackAMolePlayer(Socket socket, int player_num) {
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

    public void connect(int row, int column, int num_players, int player_num){
        printStream.println(WELCOME + " " + row + " " + column + " " + num_players + " " + player_num);
    }

    public void moleUp(int mole_num){
        printStream.println(MOLE_UP + " " + mole_num) ;
    }

    public void moleDown(int mole_num){
        printStream.println(MOLE_DOWN + " " + mole_num) ;
    }

    public void whack(int mole_num){
        printStream.println(WHACK + " " + mole_num + " " + this.player_number) ;
    }

    public void score(ArrayList<Integer> player_scores){
        String scores = "" ;
        for(int score: player_scores){
            scores = scores + " " + score;
        }
        printStream.println(SCORE + scores) ;
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
