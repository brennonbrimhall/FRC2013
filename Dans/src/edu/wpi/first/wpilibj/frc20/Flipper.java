/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Driver
 */
public class Flipper {
    
    Relay flipper;
    Tray tray;
    
    final int retracted = 0;
    final int retracting = 1;
    final int extended = 2;
    
    int state = retracted;
    
    int cycleCounter;
    
    public Flipper(Relay flipper, Tray tray){
        this.flipper = flipper;
        this.tray = tray;
    }
    
    public void extend() {
        flipper.set(Relay.Value.kReverse);
        state = extended;
        tray.beltShootSpeed = -.6;
        //cycleCounter = 0;
    }

    public void retract() {
        flipper.set(Relay.Value.kForward);
        state = retracted;
        tray.beltShootSpeed = -.95;
        //cycleCounter = 0;
    }
    
    public void update() {
        if(isExtended()){
            flipper.set(Relay.Value.kReverse);
        }else if(isRetracted()){
            flipper.set(Relay.Value.kForward);
        }
    }
    
    public boolean isExtended() {
        return flipper.get() == Relay.Value.kReverse;
    }
    
    public boolean isRetracted() {
        return flipper.get() == Relay.Value.kForward;
    }

}
