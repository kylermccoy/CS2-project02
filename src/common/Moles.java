package common;

import java.util.Random;

public class Moles extends Thread {

    private int number ;
    public boolean isUp ;

    private WhackAMoleGame game ;

    public Moles(int number, WhackAMoleGame game) {
        this.number = number ;
        this.game = game ;
        this.isUp = false ;
    }

    public int getNumber(){
        return this.number ;
    }

    public void run(){
        int uptime ;
        int downtime ;
        Random rand = new Random() ;
        while(game.isActive()) {
            uptime = rand.nextInt(2) + 3 ;
            while(uptime > 0){
                this.isUp = true ;
                try{
                    sleep(1000) ;
                }
                catch (InterruptedException e){
                    System.out.println("Sleeping Error!");
                }
                uptime-- ;
            }
            downtime = rand.nextInt(8) + 2 ;
            while(downtime > 0){
                this.isUp = false ;
                try{
                    sleep(1000) ;
                }
                catch (InterruptedException e){
                    System.out.println("Sleeping Error!");
                }
            }
        }
    }
}