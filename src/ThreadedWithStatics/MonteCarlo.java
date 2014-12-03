/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ThreadedWithStatics;

import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author D14
 */
public class MonteCarlo {
    private Random rand = new Random();
    private int circle = 0;
    private int square = 0;
    
    private boolean inCircle(double x, double y) {
        if(x*x+y*y < 1) return true;
        return false;
    }
    
    public void findPi(int count) {
        for(int a = 0; a < count; a++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            
            if(inCircle(x,y)) circle++;
            square++;
        }
    }
    
    public double PI() {
        return (4.0*( (double) circle / (double) square ));
    }
}