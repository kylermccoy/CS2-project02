package server;


import common.WhackAMoleGame;
import common.WhackAMolePlayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @Author Kyle McCoy
 * @Author Alex Cooley
 */
public class WAMServer implements Runnable {
    private ServerSocket server ;
    private ArrayList<WhackAMolePlayer> players ;
    private static int rows ;
    private static int columns ;
    private static int num_players ;
    private static int game_time_seconds ;

    /**
     * Creates the server
     * @param port
     */
    public WAMServer(int port) {
        try {
            server = new ServerSocket(port) ;
        }
        catch(IOException e){
            System.out.println("Server could not be created!") ;
        }
    }

    /**
     * Sets the given command line arguments to the variables and runs the server
     * @param args
     */
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

    /**
     * Runs the server, it accepts all the player sockets, sends the correct data to each player and starts the game.
     */
    @Override
    public void run() {
        try {
            System.out.println("Server started!");
            players = new ArrayList() ;
            int i = 1 ;
            int num_player = num_players ;
            System.out.println("Game created!");
            WhackAMoleGame game = new WhackAMoleGame(rows, columns, game_time_seconds) ;
            while(num_player > 0){
                Socket playerSocket = server.accept() ;
                WhackAMolePlayer player = new WhackAMolePlayer(playerSocket, i, game) ;
                player.connect(rows, columns, num_players, i) ;
                System.out.println("Player #" + i + " connected.") ;
                i++ ;
                num_player-- ;
                players.add(player) ;
                player.start() ;
                game.addPlayer(player) ;
            }
            System.out.println("Game starting!");
            Thread thread = new Thread(game) ;
            thread.run();
        }
        catch(IOException e){
            System.out.println("Something has gone horribly wrong!") ;
            e.printStackTrace();
        }
    }
}
