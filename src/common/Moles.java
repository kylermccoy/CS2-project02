package common;

import java.util.Random;

/**
 * @Author: Kyle McCoy
 * @Author: Alex Cooley
 */
public class Moles extends Thread {

    private int number ;
    private boolean isUp ;
    private boolean gotwhacked ;

    private WhackAMoleGame game ;

    /**
     * Constructor that creates the moles with its number, the game, whether it is up, and if they got whacked
     * @param number
     * @param game
     */
    public Moles(int number, WhackAMoleGame game) {
        this.number = number ;
        this.game = game ;
        this.isUp = false ;
        this.gotwhacked = false ;
    }

    /**
     * Returns whether the mole is up or not
     * @return
     */
    public boolean isUp(){
        return isUp ;
    }

    /**
     * Returns whether the mole got whacked or not
     * @return
     */
    public boolean gotWhacked(){
        return gotwhacked ;
    }

    /**
     * Sets the whacked parameter to the boolean given
     * @param bool
     */
    public void setWhacked(boolean bool){
        gotwhacked = bool ;
    }

    /**
     * Returns the moles number
     * @return
     */
    public int getNumber(){
        return this.number ;
    }

    /**
     * The run method for the thread. While the game is active, the mole will randomly go up and down for a certain
     * amount of seconds.
     */
    public void run(){
        int uptime ;
        int downtime ;
        int rando ;
        Random rand = new Random() ;
        while(game.isActive()) {
            rando = rand.nextInt(2);
            if (rando == 1) {
                uptime = rand.nextInt(2) + 3;
                this.isUp = true;
                this.game.moleUp(number);
                try {
                    sleep(uptime * 1000);
                } catch (InterruptedException e) {
                    System.out.println("Sleeping Error!");
                }
                downtime = rand.nextInt(8) + 2;
                this.isUp = false;
                this.game.moleDown(number);
                setWhacked(false);
                try {
                    sleep(downtime * 1000);
                } catch (InterruptedException e) {
                    System.out.println("Sleeping Error!");
                }
            }else{
                try {
                    sleep(3 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("Sleeping Error!");
                }
            }
        }
    }
}
