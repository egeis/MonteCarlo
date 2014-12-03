package ThreadedWithLocks;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.DecimalFormat;
import java.util.concurrent.locks.ReentrantLock;

public class SharedStates {
    private int inner, outer = 0;
    public int getInner() { return inner; }
    public int getOuter() { return outer; }
  
    private ReentrantLock lockInner = new ReentrantLock();
    private ReentrantLock lockOuter = new ReentrantLock();
        
    public void incInner() {
        lockInner.lock();
        try {
            inner++;
        } finally {
            lockInner.unlock();
        }
    }
    
    public void incOuter() {
        lockOuter.lock();
        try {
            outer++;
        } finally {
            lockOuter.unlock();
        }
    }
    
    public double getPI() {
        return (4.0*( (double) inner / (double) outer ));
    }
    
    public void report() {
       double pi = getPI();
       DecimalFormat df = new DecimalFormat("#.#####");
       
       System.out.println("****Monte Carlo Method Sequential****");
       System.out.println("Approximate value for pi: "+pi);
       System.out.println("Difference to exact value of pi: "+df.format(pi-Math.PI));
       System.out.println("Error: (approx-exact)/exact: "+df.format(pi/Math.PI) );
    }
}