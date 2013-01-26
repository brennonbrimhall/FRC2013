package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joe
 */
public class CANJaguar2 {
    
    public CANJaguar jag;
    
    double prevX = 0;
    
    CANJaguar2(int address) {
        try {
            jag = new CANJaguar(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public double getCurrent() {
        double current = 0;
        try {
            current = jag.getOutputCurrent();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return current;
    }
    
    public void setSpeedMode(int ticksPerRev, double p, double i, double d) {
        try {
            Timer.delay(.1);
            jag.changeControlMode(CANJaguar.ControlMode.kSpeed);
            jag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jag.configEncoderCodesPerRev(ticksPerRev);
            jag.setPID(p, i, d);
            jag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setPositionMode(int ticksPerRev, double p, double i, double d) {
        try {
            Timer.delay(.1);
            jag.changeControlMode(CANJaguar.ControlMode.kPosition);
            jag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jag.configEncoderCodesPerRev(ticksPerRev);
            jag.setPID(p, i, d);
            jag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setVoltageMode() {
        try {
            Timer.delay(.1);
            jag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            jag.enableControl();
            jag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
            jag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            jag.configEncoderCodesPerRev(360);
            jag.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setCurrentMode(double p, double i, double d) {
        try {
            Timer.delay(.1);
            jag.changeControlMode(CANJaguar.ControlMode.kCurrent);
            jag.setPID(p, i, d);
            jag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void set(double X) {
//        if(X != prevX) {
            try {
                jag.setX(X);
                prevX = X;
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
    } 
    
    public double getX() {
        double X = -1000;
        
        try {
            X = jag.getX();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        return X;
    }
    
    public double getVoltage() {
        double voltage = 0;
        
        try {
            
            voltage = jag.getOutputVoltage();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
            
        return voltage;
    }
    
    public void enableControl() {
        try {
            jag.enableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enableControl(double initPos) {
        try {
            jag.enableControl(initPos);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void disableControl() {
        try {
            jag.disableControl();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public double getSpeed() {
        double speed = 0;
        
        try {
            speed = jag.getSpeed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return speed;
    }
    
    public double getPosition() {
        double position = 0;
        
        try {
            position = jag.getPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return position;
    }
    
    public boolean frontLimitHit() {
        boolean hit = false;
            
        try {
            hit = !jag.getForwardLimitOK();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
            
        return hit;
    }
    
    public boolean rearLimitHit() {
        boolean hit = false;
            
        try {
            hit = !jag.getReverseLimitOK();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
            
        return hit;
    }
    
    
}
