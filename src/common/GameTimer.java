package common;

/**
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */
public class GameTimer extends Thread {

    private int game_time ;
    private boolean active ;

    /**
     * Sets the game time
     * @param game_time
     */
    public GameTimer(int game_time){
        this.game_time = game_time ;
    }

    /**
     * Returns whether the game is active
     * @return
     */
    public boolean isActive(){
        return active ;
    }

    /**
     * Starts the game and runs in for the specified amount of time
     */
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
