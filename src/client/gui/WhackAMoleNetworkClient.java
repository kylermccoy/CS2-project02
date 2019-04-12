package client.gui;

import common.WhackAMoleBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class WhackAMoleNetworkClient {
    private Socket clientSocket;
    private Scanner networkIn;
    private PrintStream networkOut;
    private WhackAMoleBoard board;
    private boolean loopcontrol;

    public WhackAMoleNetworkClient(String host, int port, WhackAMoleBoard board){
        try{
            this.clientSocket = new Socket(host, port);
            this.networkIn = new Scanner(clientSocket.getInputStream());
            this.networkOut = new PrintStream(clientSocket.getOutputStream());
            this.board = board;
            //might need to throw if the protocol isnt CONNECT

        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    private synchronized boolean gameContinue(){return this.loopcontrol;}

    private synchronized void stop(){this.loopcontrol = false;}

    //will fix when the board is created
    private void makeMove(){this.board.makeMove();}

}
