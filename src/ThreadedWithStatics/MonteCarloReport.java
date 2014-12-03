/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ThreadedWithStatics;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author D14
 */
public class MonteCarloReport {
        
    public MonteCarloReport() throws InterruptedException {
        this(1000000);
    }
    
    public MonteCarloReport( int count) throws InterruptedException {       
       double pi = 0;
       long start;
       long end;
        
       System.out.println("****Monte Carlo Method Sequential****");
       MonteCarlo mc = new MonteCarlo();
      
       //Run and Time
       start = System.currentTimeMillis();
       mc.findPi(count);
       end = System.currentTimeMillis();
       
       //Output
       pi = mc.PI();
       DecimalFormat df = new DecimalFormat("#.#####");
       
       System.out.println("Approximate value for pi: "+pi);
       System.out.println("Difference to exact value of pi: "+df.format(pi-Math.PI));
       System.out.println("Error: (approx-exact)/exact: "+df.format(pi/Math.PI) );
       System.out.println("Time: " + (end - start) + "ms");
       
       //Question 2
       //Write a parallel Java program to estimate PI using the Monte-Carlo method. , 
       System.out.println("****Monte Carlo Method Multi/Threading****");
       
       //Selecting a number of threads based on the available number of processors.
       int threads = Runtime.getRuntime().availableProcessors();
       
       pi = 0;
       start = end = 0;
       count = Math.round(count / threads);
       
       ExecutorService executor = Executors.newFixedThreadPool(threads);
       Runnable workers[] = new Runnable[threads];
       
       //Create Threads
       for(int a = 0; a < threads; a++) {
           workers[a] = new MonteCarloThreaded( count );
       }
       
       //Execute Threads and Time
       start = System.currentTimeMillis();
       for(int a = 0; a < threads; a++) {
           executor.execute(workers[a]);
       }
       
       executor.shutdown();
       while(!executor.isTerminated()){}
       end = System.currentTimeMillis();
       
       //Output Results
       pi = MonteCarloThreaded.PI();   
       
       System.out.println("Approximate value for pi: "+pi);
       System.out.println("Difference to exact value of pi: "+df.format(pi-Math.PI));
       System.out.println("Error: (approx-exact)/exact: "+df.format(pi/Math.PI) );
       System.out.println("Time: " + (end - start) + "ms");
    }   
}
