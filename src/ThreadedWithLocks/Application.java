package ThreadedWithLocks;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author D14
 */
public class Application {
    
    private Application() {}
    
    public static void main(String[] args) throws InterruptedException {
       long start, end;
       int count = 1000000;
       int threads = Runtime.getRuntime().availableProcessors();
       SharedStates state;
       ExecutorService executor;
       
       /**Sequential
        */
       executor = Executors.newFixedThreadPool(threads);
       start = end = 0;
       state = new SharedStates();
       Runnable workers[] = new Runnable[threads];
       workers[0] = new MonteCarlo(count, state);
             
       start = System.currentTimeMillis();
       executor.execute(workers[0]);       
       executor.shutdown();
       while(!executor.isTerminated()){}
       end = System.currentTimeMillis();
       
       state.report();
       System.out.println("Time: " + (end - start) + "ms");
       
       /**Parallel
        */
       executor = Executors.newFixedThreadPool(threads);
       start = end = 0;
       state = new SharedStates();
       for(int a = 0; a < threads; a++) {
           workers[a] = new MonteCarlo( Math.round(count/threads), state);
       }
       
       start = System.currentTimeMillis();
       for(int a = 0; a < threads; a++) {
           executor.execute(workers[a]);
       }
       executor.shutdown();
       while(!executor.isTerminated()){}
       end = System.currentTimeMillis();
       
       state.report();
       System.out.println("Time: " + (end - start) + "ms");
    }
}
