package common;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import static common.WAMProtocol.WELCOME;

public class WhackAMolePlayer implements Closeable {
    private Socket socket ;

    private Scanner scanner ;

    private PrintStream printStream ;

    public WhackAMolePlayer(Socket socket) {
        this.socket = socket ;
        try {
            scanner = new Scanner(socket.getInputStream()) ;
            printStream = new PrintStream(socket.getOutputStream()) ;
        }
        catch(IOException e){
            System.out.println("Player unable to be created!");
        }
    }

    public void connect(int row, int column){
        printStream.println(WELCOME + " " + row + " " + column);
    }

    //ADD IN SEND REQUESTS TO CLIENT

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
