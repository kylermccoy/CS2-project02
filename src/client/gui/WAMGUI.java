package client.gui;

import common.WhackAMoleBoard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class WAMGUI extends Application implements Observer<WhackAMoleBoard>{

    private WhackAMoleBoard game ;
    private WhackAMoleNetworkClient client ;
    private ArrayList<Label> labels ;
    private ArrayList<Button> buttons ;

    @Override
    public void init(){
        try{
            List<String> args = getParameters().getRaw() ;
            String host = args.get(0) ;
            int port = Integer.parseInt(args.get(1)) ;

            this.client = new WhackAMoleNetworkClient(host, port) ;
            this.game.addObserver(this) ;
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }
    }

    public void start(Stage stage) {
        buttons = new ArrayList<>() ;
        GridPane gridPane = new GridPane() ;

    }

    @Override
    public void stop(){
        this.game.close() ;
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
