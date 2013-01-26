/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author joe
 */
public class LowerRollers {
    
    // Competition
    CANJaguar2 frontJag;
    CANJaguar2 rearJag;
    
    // Practice
//    Jaguar frontJag;
//    Jaguar rearJag;
    
    double fastSpeed = 1;
    double mediumSpeed = 1;
    double slowSpeed = .25;
    
    boolean rearEnabled = true;
    boolean frontEnabled = true;
    
    boolean backDriveRear = false;
    boolean backDriveFront = false;
    
    double rearBackdriveTime = 0;
    double frontBackdriveTime = 0;
    
    LowerRollers(int frontAddress, int rearAddress) {
        // Competition
        frontJag = new CANJaguar2(frontAddress);
        rearJag = new CANJaguar2(rearAddress);
        
        frontJag.setVoltageMode();
        rearJag.setVoltageMode();
        
        // Practice
//        frontJag = new Jaguar(frontAddress);
//        rearJag = new Jaguar(rearAddress);
        
        DriverStation.getInstance().setDigitalOut(1, true);
        DriverStation.getInstance().setDigitalOut(2, true);
    }
    
    public void start() {
        rearEnabled = true;
        frontEnabled = true;
    }
    
    public void stop() {
        rearEnabled = false;
        frontEnabled = false;
    }
    
    public void toggleFront() {
        if(frontEnabled) {
            frontEnabled = false;
            frontBackdriveTime = .5 + Timer.getFPGATimestamp();
            DriverStation.getInstance().setDigitalOut(1, false);
        } else {
            frontEnabled = true;
        }
    }
    
    public void toggleRear() {
        if(rearEnabled) {
            rearEnabled = false;
            rearBackdriveTime = .5 + Timer.getFPGATimestamp();
            DriverStation.getInstance().setDigitalOut(2, false);
        } else {
            rearEnabled = true;
            DriverStation.getInstance().setDigitalOut(2, true);
        }
    }
    
    public void update() {
        
        if(frontBackdriveTime > Timer.getFPGATimestamp()) {
            if(backDriveFront) {
                frontJag.set(mediumSpeed);
            } else {
                frontJag.set(slowSpeed);
            }
            DriverStation.getInstance().setDigitalOut(1, false);
        } else if(frontEnabled) {
            backDriveFront = false;
            frontJag.set(-fastSpeed);
            DriverStation.getInstance().setDigitalOut(1, true);
        } else {
            backDriveFront = false;
            frontJag.set(0);
            DriverStation.getInstance().setDigitalOut(1, false);
        }
        
        if(rearBackdriveTime > Timer.getFPGATimestamp()) {
            if(backDriveRear) {
                rearJag.set(-mediumSpeed);
            } else {
                rearJag.set(-slowSpeed);
            }
            DriverStation.getInstance().setDigitalOut(2, false);
        } else if(rearEnabled) {
            backDriveRear = false;
            rearJag.set(fastSpeed);
            DriverStation.getInstance().setDigitalOut(2, true);
        } else {
            backDriveRear = false;
            rearJag.set(0);
            DriverStation.getInstance().setDigitalOut(2, false);
        }
    }
    
    public void backDriveFront() {
        backDriveFront = true;
        frontBackdriveTime = .5 + Timer.getFPGATimestamp();
    }
    
    public void backDriveRear() {
        backDriveRear = true;
        rearBackdriveTime = .5 + Timer.getFPGATimestamp();
    }
    
}
