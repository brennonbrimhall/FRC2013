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
    
    public Flipper(Relay flipper){
        this.flipper = flipper;
    }
    
    public void extend() {
        flipper.set(Relay.Value.kForward);
    }

    public void retract() {
        flipper.set(Relay.Value.kReverse);
    }
    
    public boolean isExtended() {
        return flipper.get() == Relay.Value.kForward;
    }
    
    public boolean isRetracted() {
        return flipper.get() == Relay.Value.kReverse;
    }

}
