/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 * @author joe
 */
public class DriveTrain {
    
    
    // COMPETITION
    
    CANJaguar2 leftJagMaster;
    CANJaguar2 leftJagSlave;
    
    CANJaguar2 rightJagMaster;
    CANJaguar2 rightJagSlave;
    
    double basePos = 0;
    
    Encoder driveEncoder;
    
    
    // PRACTICE BOT
//    Jaguar leftJagMaster;
//    Jaguar leftJagSlave;
    
//    Jaguar rightJagMaster;
//    Jaguar rightJagSlave;
    
    DriveTrain(int leftMasterAddress, int leftSlaveAddress, 
               int rightMasterAddress, int rightSlaveAddress) {
        
        // Competition
        leftJagMaster = new CANJaguar2(leftMasterAddress);
        leftJagSlave = new CANJaguar2(leftSlaveAddress);
        
        rightJagMaster = new CANJaguar2(rightMasterAddress);
        rightJagSlave = new CANJaguar2(rightSlaveAddress);
        
        leftJagMaster.setVoltageMode();
        leftJagSlave.setVoltageMode();
        
        rightJagMaster.setVoltageMode();
        rightJagSlave.setVoltageMode();
        
        basePos = leftJagMaster.getPosition() * 360;
        
        driveEncoder = new Encoder(8,9,true);
        driveEncoder.setDistancePerPulse(1);
        driveEncoder.reset();
        driveEncoder.start();
        
        // Practice
//        leftJagMaster = new Jaguar(leftMasterAddress);
//        leftJagSlave = new Jaguar(leftSlaveAddress);
        
//        rightJagMaster = new Jaguar(rightMasterAddress);
//        rightJagSlave = new Jaguar(rightSlaveAddress);
    }
    
    public void drive(double throttle, double turn) {
        double leftSpeed = throttle + 2 * turn;
        double rightSpeed = -throttle + 2 * turn;
        
        leftJagMaster.set(leftSpeed);
        leftJagSlave.set(leftSpeed);

        rightJagMaster.set(rightSpeed);
        rightJagSlave.set(rightSpeed);
        
    }
    
    public double getPosition(boolean canBus) {
        if(canBus) {
            return leftJagMaster.getPosition() * 360 - basePos;
        } else {
            return driveEncoder.getDistance();
        }
    }
    
}
