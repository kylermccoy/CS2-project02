package client.gui;

import common.WAMProtocol;
import common.WhackAMoleBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WhackAMoleNetworkClient {
    private Socket clientSocket;
    private Scanner networkIn;
    private PrintStream networkOut;
    private WhackAMoleBoard board;
    private boolean loopcontrol;
    private int rows;
    private int cols;
    private int numberOfPlayers;
    private int playerNumber;

    public WhackAMoleNetworkClient(String host, int port, WhackAMoleBoard board){
        try{
            this.clientSocket = new Socket(host, port);
            this.networkIn = new Scanner(clientSocket.getInputStream());
            this.networkOut = new PrintStream(clientSocket.getOutputStream());
            this.board = board;
            String response = networkIn.nextLine();
            String[] tokens = response.split(" ");
            if(tokens[0].equals(WAMProtocol.WELCOME)){
                this.rows = Integer.parseInt(tokens[1]);
                this.cols = Integer.parseInt(tokens[2]);
                this.numberOfPlayers = Integer.parseInt(tokens[3]);
                this.playerNumber = Integer.parseInt(tokens[4]);
            }
            else{
                throw new IOException();
            }

        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public ArrayList<Integer> getRowsCols(){
        ArrayList<Integer> rowsColumns =  new ArrayList<Integer>(2);
        rowsColumns.add(this.rows,this.cols);
        return rowsColumns;
    }

    private synchronized boolean gameContinue(){return this.loopcontrol;}

    private synchronized void stop(){this.loopcontrol = false;}


}
