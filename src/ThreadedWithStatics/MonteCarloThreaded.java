/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThreadedWithStatics;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MonteCarloThreaded implements Runnable {
    private Random rand = new Random();
       
    private int count = 0;
    private static AtomicInteger circle = new AtomicInteger();
    private static AtomicInteger square = new AtomicInteger();
    
    public MonteCarloThreaded() {
        this(10000);
    }
    
    public MonteCarloThreaded(int count) {
        this.count = count;
    }
    
    private boolean inCircle(double x, double y) {
        if(x*x+y*y < 1) return true;
        return false;
    }
   
    public void run() {
        for(int a = 0; a < count; a++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            
            if(inCircle(x,y)) circle.getAndIncrement();
            square.getAndIncrement();
        }
    }
    
    public static double PI() {
        return (4.0*(circle.get())/square.get());
    }
    
}
