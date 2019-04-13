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
import java.util.List;

public class WAMGUI extends Application implements Observer<WhackAMoleBoard>{

    private WhackAMoleBoard board ;
    private WhackAMoleNetworkClient client ;
    private Label player_scores ;
    private ArrayList<Button> buttons ;
    private Label player_status ;

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

    public void start(Stage stage) {
        buttons = new ArrayList<>() ;
        GridPane gridPane = new GridPane() ;
        int col = this.board.getColumns() ;
        int row = this.board.getRows() ;
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
        int num_players = this.client.getTotal_players() ;
        String scores = "" ;
        for(int i = 0; i < num_players; i++) {
            scores = scores + "P" + i + ":0 ";
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

    public ArrayList<Button> getButtons(){
        return this.buttons;
    }

    @Override
    public void stop(){
        this.board.close() ;
        this.client.close() ;
    }

    private void refresh(){

    }

    @Override
    public void update(WhackAMoleBoard board){
        if(Platform.isFxApplicationThread()){
            this.refresh();
        }else{
            Platform.runLater(()->this.refresh());
        }
    }

    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Usage: java WAMGUI host port") ;
            System.exit(1) ;
        }else{
            Application.launch(args) ;
        }
    }
}
