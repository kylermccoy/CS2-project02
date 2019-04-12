package server;

import common.WhackAMoleBoard;
import common.WhackAMoleGame;
import common.WhackAMolePlayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WAMServer implements Runnable {
    private ServerSocket server ;
    private ArrayList<WhackAMolePlayer> players ;
    private static int rows ;
    private static int columns ;
    private static int num_players ;
    private static int game_time_seconds ;

    public WAMServer(int port) {
        try {
            server = new ServerSocket(port) ;
        }
        catch(IOException e){
            System.out.println("Server could not be created!") ;
        }
    }

    public static void main(String[] args) {
        if(args.length != 5){
            System.out.println("Usage: java WAMServer <game-port> <rows> <columns> <players> <game-duration-seconds>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]) ;
        WAMServer server = new WAMServer(port) ;

        rows = Integer.parseInt(args[1]) ;
        columns = Integer.parseInt(args[2]) ;
        num_players = Integer.parseInt(args[3]) ;
        game_time_seconds = Integer.parseInt(args[4]) ;

        server.run() ;
    }

    @Override
    public void run() {
        try {
            players = new ArrayList() ;
            int i = 0 ;
            while(num_players > 0){
                Socket playerSocket = server.accept() ;
                WhackAMolePlayer player = new WhackAMolePlayer(playerSocket) ;
                player.connect(rows,columns) ;
                System.out.println("Player #" + i + " connected.") ;
                i++ ;
                num_players-- ;
                players.add(player) ;
            }
            WhackAMoleGame game = new WhackAMoleGame(players) ;
            new Thread(game).run() ;
        }
        catch(IOException e){
            System.out.println("Something has gone horribly wrong!") ;
            e.printStackTrace();
        }
    }
}
