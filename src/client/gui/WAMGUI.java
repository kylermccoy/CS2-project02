package client.gui;

import common.WhackAMoleBoard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JavaFX GUI for the networked Whack a mole game
 *
 * @Author: Alex Cooley
 * @Author: Kyle McCoy
 */

public class WAMGUI extends Application implements Observer<WhackAMoleBoard>{

    private WhackAMoleBoard board ;
    private WhackAMoleNetworkClient client ;
    private Label player_scores ;
    private ArrayList<Button> buttons ;
    private Label player_status ;
    private WhackAMoleBoard.Status status;
    private int row;
    private int col;
    private int num_players;
    private String scores;

    /**
     * Creates a new network client, and game board for the game. Also adds an observer
     */
    @Override
    public void init(){
        try{
            List<String> args = getParameters().getRaw() ;
            String host = args.get(0) ;
            int port = Integer.parseInt(args.get(1)) ;

            this.client = new WhackAMoleNetworkClient(host, port) ;
            this.board = this.client.getBoard() ;
            this.board.addObserver(this) ;
        }
        catch (NullPointerException e){
            System.out.println("Error in connecting to server!");
            System.exit(1);
        }
        catch (NumberFormatException e){
            System.out.println("Server unavailable!");
            System.exit(1);
        }
    }

    /**
     * Constructs the layout for the game, with a client given number of rows and columns. Each space
     * for the mole is a button.
     *
     * @param stage The window that the GUI is rendered in
     */
    public void start(Stage stage) {
        buttons = new ArrayList<>() ;
        GridPane gridPane = new GridPane() ;
        col = this.board.getColumns() ;
        row = this.board.getRows() ;
        int id = 0 ;
        for(int row_loop = 0; row_loop < row; row_loop++){
            for(int col_loop = 0; col_loop < col; col_loop++){
                int temp = id ;
                Button button = new Button() ;
                button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("empty.png"))));
                button.setOnAction(event -> {
                    client.sendWhack(temp);
                });
                button.setPrefSize(50,50) ;
                buttons.add(button) ;
                gridPane.add(button, col_loop, row_loop) ;
                id++ ;
            }
        }
        gridPane.setGridLinesVisible(true) ;
        num_players = this.client.getTotal_players() ;
        scores = "" ;
        for(int i = 0; i < num_players; i++) {
            scores = scores + "P" + i + ": 0  ";
        }
        player_scores = new Label(scores) ;
        player_scores.setStyle("-fx-font: " + 18 + " arial;") ;
        Label score_title = new Label("SCORE") ;
        score_title.setStyle("-fx-font: " + 18 + " arial;") ;
        VBox scoring = new VBox(score_title, player_scores) ;
        BorderPane borderPane = new BorderPane() ;
        borderPane.setTop(scoring) ;
        borderPane.setCenter(gridPane) ;
        player_status = new Label() ;
        borderPane.setBottom(player_status);
        borderPane.setAlignment(scoring, Pos.TOP_CENTER);
        Scene scene = new Scene(borderPane) ;
        stage.setScene(scene) ;
        stage.setTitle("Whack-A-Mole");
        stage.show();
        client.startListener();
    }

    /**
     * GUI is closing, so the network connection and the board is closed.
     */
    @Override
    public void stop(){
        this.board.close() ;
        this.client.close() ;
    }

    /**
     * Updates the GUI including checking to see if the moles are up and down, updates the scores
     * and checks to see if the client won, lost or tied in order to update the status label correctly
     */
    private void refresh(){
        status = board.getStatus();
        int[] moleCheck = this.board.getMolecheck();
        String[] refreshScores = this.board.getScores();

        for(int numMoles = 0; numMoles < (row*col); numMoles++){
            if(moleCheck[numMoles] == 1){
                buttons.get(numMoles).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("mole.png"))));
            }
            else if(moleCheck[numMoles] == 0){
                buttons.get(numMoles).setGraphic(new ImageView(new Image(getClass().getResourceAsStream("empty.png"))));
            }
        }

        if(refreshScores != null){
            int index = 0;
            scores = "";
            for (int i = 0; i < num_players; i++) {
                try {
                    scores = scores + "P" + i + ": " + refreshScores[index] + "  ";
                    index++;

                } catch (NullPointerException e) {
                    System.out.println("WHY GOD!");
                }
            }
            player_scores.setText(scores);
        }

            System.out.println(status);
        if(status != null) {
            switch (status) {
                case TIE:
                    this.player_status.setText("The Game has Tied!");
                    break;
                case I_LOST:
                    this.player_status.setText("You Lost the Game!");
                    break;
                case I_WON:
                    this.player_status.setText("You Won the Game!");
                    break;
            }
        }
    }

    /**
     * Called by the model, whenever there is a state change that needs to be updated by the GUI
     * @param board: whack a mole board
     */
    @Override
    public void update(WhackAMoleBoard board){
        if(Platform.isFxApplicationThread()){
            this.refresh();
        }else{
            Platform.runLater(()->this.refresh());
        }
    }

    /**
     * Main method that expects the host and the port
     *
     * @param args: command line arguments
     */
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Usage: java WAMGUI host port") ;
            System.exit(1) ;
        }else{
            Application.launch(args) ;
        }
    }
}
