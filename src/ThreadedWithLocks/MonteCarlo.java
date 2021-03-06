package ThreadedWithLocks;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarlo implements Runnable {
    private ThreadLocalRandom rand;
    private int count = 0;
    private SharedStates state;
    
    public MonteCarlo(int count, SharedStates state) {
        this.count = count;
        this.state = state;
    }
    
    private boolean inCircle(double x, double y) {
        if(x*x+y*y < 1) return true;
        return false;
    }
    
    public void run() {
        for(int a = 0; a < count; a++) {
            double x = rand.current().nextDouble();
            double y = rand.current().nextDouble();
            
            if(inCircle(x,y)) state.incInner();
            state.incOuter();
        }
    }
}