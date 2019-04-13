package common;

import java.util.Random;

public class Moles extends Thread {

    private int number ;
    private boolean isUp ;

    private WhackAMoleGame game ;

    public Moles(int number, WhackAMoleGame game) {
        this.number = number ;
        this.game = game ;
        this.isUp = false ;
    }

    public boolean isUp(){
        return isUp ;
    }

    public int getNumber(){
        return this.number ;
    }

    public void run(){
        int uptime ;
        int downtime ;
        int rando ;
        Random rand = new Random() ;
        while(game.isActive()) {
            rando = rand.nextInt(2);
            if(rando==1) {
                uptime = rand.nextInt(2) + 3;
                this.game.moleUp(number);
                while (uptime > 0) {
                    this.isUp = true;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Sleeping Error!");
                    }
                    uptime--;
                }
            }else {
                downtime = rand.nextInt(8) + 2;
                this.game.moleDown(number);
                while (downtime > 0) {
                    this.isUp = false;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Sleeping Error!");
                    }
                }
            }
        }
    }
}
