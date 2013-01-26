/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author Driver
 */
public class CowCatcher {
    
    Relay motor;
    
    DigitalInput lowerLimit;
    DigitalInput upperLimit;
    
    double timeLatch = 0;
    
    Servo latch;
    
    boolean state = false;  // Up = false, Down = true
    
    CowCatcher(int relayAddress, int lowerLimitAddress, int upperLimitAddress, int servoAddress) {
        motor = new Relay(relayAddress);
        lowerLimit = new DigitalInput(lowerLimitAddress);
        upperLimit = new DigitalInput(upperLimitAddress);
        latch = new Servo(servoAddress);
        latch.set(.35);
    }
    
    public void update() {
        
//        if(upperLimit.get() || lowerLimit.get()) {
//            motor.set(Relay.Value.kOff);
//        }
        
        if(state && !lowerLimit.get()) {
            motor.set(Relay.Value.kForward);
        } else if(!state && !upperLimit.get()) {
            if(timeLatch < Timer.getFPGATimestamp()) {
                motor.set(Relay.Value.kReverse);
            }
        } else {
            motor.set(Relay.Value.kOff);
            if(state && lowerLimit.get()) {
                latch.set(.8);
//                latch.set(DriverStation.getInstance().getAnalogIn(1));
//                System.out.println("SET 2");
            }
        }
    }
    
    public void setUp() {
        state = false;
        if(timeLatch < Timer.getFPGATimestamp() - .5) {
            timeLatch = Timer.getFPGATimestamp() + .2;
        }
        latch.set(.35);
//        latch.set(DriverStation.getInstance().getAnalogIn(2));
//        System.out.println("SET 1");
    }
    
    public void setDown() {
        state = true;
    }
    
    public boolean isUp() {
        return upperLimit.get();
    }
    
    public boolean isDown() {
        return lowerLimit.get();
    }
    
    public boolean getState() {
        return state;
    }
    
    public boolean atPosition() {
        return (state && lowerLimit.get()) || (!state && upperLimit.get());
    }
    
}
