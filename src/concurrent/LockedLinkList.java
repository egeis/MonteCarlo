package concurrent;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Richard Coan
 */
public class LockedLinkList<E> {

    private List<E> list = new LinkedList<E>();
    private SpinLock _sp = new SpinLock();

    public E remove() {        
        E e;
        try {
            _sp.lock(Thread.currentThread().getId());
            e = list.remove(0);
        } finally {
            _sp.unlock(Thread.currentThread().getId());
        }
        return e;
    }

    public boolean add(E e) {
        boolean a = false;
        try {
            _sp.lock(Thread.currentThread().getId());
            a = list.add(e);
        } finally {
            _sp.unlock(Thread.currentThread().getId());
        }
        return a;
    }

    public int size() {
        int a = 0;
        try {
            _sp.lock(Thread.currentThread().getId());
            a = list.size();
        } finally {
            _sp.unlock(Thread.currentThread().getId());
        }
        return a;
    } 
    
    public static void main(String[] args) throws InterruptedException {
        LockedLinkList<Integer> numbers = new LockedLinkList<Integer>();
        int count = 500000;
        
        //A shared testing object.
        class Test {
            public int total = 0;
        }
        Test sum = new Test();
        
        //Generate Testing Threads
        Thread t1;
        t1 = new Thread( new Runnable() {
            public void run() {
                for(int a = 0; a < count; a++) {
                    numbers.add(1);
                }
            }
        });
        
        Thread t2;
        t2 = new Thread( new Runnable() {
            public void run() {
                for(int a = 0; a < count; a++) {
                    numbers.add(1);
                }
            }
        });
        
        Thread t3;
        t3 = new Thread( new Runnable() {
            public void run() {
                for(int a = 0; a < 100; a++) {          //Spins may still occur;
                    while(numbers.size() > 0) {
                        sum.total += numbers.remove();
                        a = 0;                   //Reset Counter if add occured.
                    }
                }
            }
        });
        
        long end = 0;
        long start = System.currentTimeMillis();
        
        //Starts the threads.
        t1.start();
        t2.start();
        t3.start();
                        
        //Waits for the threads to be completed.
        t1.join();
        t2.join();
        t3.join();
        
        end = System.currentTimeMillis();
                
        //Outputs the result.
        System.out.println("Size of List:"+numbers.size());
        System.out.println("Sum of count ("+(count * 2)+") total is: " + sum.total);
        System.out.println("The algorithm took: " + (end - start) + " ms");
    }
}