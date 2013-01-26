/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author joe
 */
public class UpperRollers {
    
    // Competition
    CANJaguar2 leftJag;
    CANJaguar2 rightJag;
    
    // Practice
//    Jaguar leftJag;
//    Jaguar rightJag;
    
    UpperRollers(int leftAddress, int rightAddress) {
        // Competition
        leftJag = new CANJaguar2(leftAddress);
        rightJag = new CANJaguar2(rightAddress);
        
        leftJag.setVoltageMode();
        rightJag.setVoltageMode();
        
        // Practice
//        leftJag = new Jaguar(leftAddress);
//        rightJag = new Jaguar(rightAddress);
    }
    
    public void start() {
        leftJag.set(-1);
        rightJag.set(1);
    }
    
    public void stop() {
        leftJag.set(0);
        rightJag.set(0);
    }
    
    public void backDrive() {
        leftJag.set(1);
        rightJag.set(-1);
    }
}
