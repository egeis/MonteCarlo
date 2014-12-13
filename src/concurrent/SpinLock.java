package concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A lock type that causes a thread that attempting to acquire it to wait, 
 * or spin, until the lock is available.  The thread is kept alive in this 
 * manner but prevent from continuing to the next step until the lock is acquired.
 * <p>
 * A spin lock requires a single atomic or synchronized call to a boolean, 
 * to prevent multiple threads from acquiring the lock at the same time.  Using
 * an AtomicBoolean the lock can be checked and set at the same time using the
 * <b>compareAndSet()</b> operator which returns the boolean comparison result and sets
 * the indicated value if the comparison is <b>true</b>.
 * 
 * @author Richard Coan
 */
public class SpinLock {
    //private AtomicBoolean _isLocked = new AtomicBoolean();  //Lock State
    private AtomicLong _isLocked = new AtomicLong();
    
    /**
     * Initializes lock and global variables.
     */
    public SpinLock() {
        _isLocked.set(0);
    }
    
    /**
     * Attempts to acquire the lock by first spinning and trying the lock.
     * On success the loop is broken on failure the loop continues indefinitely.
.    */
    
    public void lock(long key) {        
        //TODO: Loop the spin a finite number of tries before issuing a wait().
        while(true) {
            if(tryLock(key)) { return; }
        }
    }
    
    /**
     * Unlocks the lock state by performing a <b>compareAndSet()</b> on
     * the lock state setting the lock to <b>false</b> if the lock was set.
     * 
     * @throws IllegalStateException    Thrown when attempting to unlock and unlocked lock.
     */
    public void unlock(long key) {
        //TODO: Perform a NotifyAll() after reseting the lock.
        if(!_isLocked.compareAndSet(key, 0)) {
            throw new IllegalStateException("ERROR:  An unlocked lock may not be unlocked!");
        }
    }
    
    /**
     * Returns the whether or not the lock was successful by comparing if the locks
     * state is set to "false" IE. (false == false) and then setting the locked
     * state to true if the comparison is true. If not false is returned.
     * 
     * @return  <b>true</b> if the lock was unlocked;
     *          <b>false</b> otherwise.
     */
    private boolean tryLock(long key) {
        return _isLocked.compareAndSet(0, key);
    }
    
    /**
     * Testing Method
     * 
     * @param   args    Arguments included during initialization.
     * @throws  InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        
        //A shared testing object.
        class Test {
            public int total = 0;
        }
                
        Test sum = new Test();
        
        //Initialize count and a new lock.
        int count = 10000000;
        SpinLock sp = new SpinLock();
        
        long end = 0;
        long start = System.currentTimeMillis();
        
        //Generate Testing Threads
        Thread t1;
        t1= new Thread( new Runnable() {
            public void run() {
                for(int a = 0; a < count; a++) {
                    try {
                        sp.lock(Thread.currentThread().getId());      //Spins the lock until acquired.
                        sum.total += 1; //A simple counter.
                    } finally {
                        sp.unlock(Thread.currentThread().getId());    //Frees the lock to be (re)acquired.
                    }
                }
            }
        });
        
        Thread t2 = new Thread( new Runnable() {
            public void run() {
                for(int a = 0; a < count; a++) {
                    try {
                        sp.lock(Thread.currentThread().getId());
                        sum.total += 1;
                    } finally {
                        sp.unlock(Thread.currentThread().getId());
                    }
                }
            }
        });
        
        Thread t3 = new Thread( new Runnable() {
            public void run() {
                for(int a = 0; a < count; a++) {
                    try {
                        sp.lock(Thread.currentThread().getId());
                        sum.total += 1;
                    } finally {
                        sp.unlock(Thread.currentThread().getId());
                    }
                }
            }
        });
        
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
        System.out.println("Sum of count ("+(count * 3)+") is: "+sum.total);
        System.out.println("The algorithm took: " + (end - start) + " ms");
    }
}