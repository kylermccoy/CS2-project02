package common;

public class GameTimer extends Thread {

    private int game_time ;
    private boolean active ;

    public GameTimer(int game_time){
        this.game_time = game_time ;
    }

    public boolean isActive(){
        return active ;
    }

    public void run(){
        try {
            active = true;
            System.out.println("Starting timer!");
            sleep(game_time * 1000);
            active = false;
            System.out.println("Timer finished!");
        }
        catch(InterruptedException ex){
            System.out.println("Interrupted Exception!");
        }
    }
}
