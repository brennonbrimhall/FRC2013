/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author freshplum
 */
public class Lifter {
    
    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;
    
    Lifter(DoubleSolenoid lifterSolenoid,
           DigitalInput leftLimit, DigitalInput rightLimit) {
        this.lifterSolenoid = lifterSolenoid;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
    }
    
    void raise() {
        lifterSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    
    void lower() {
        lifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    
    void release() {
        lifterSolenoid.set(DoubleSolenoid.Value.kOff);
    }
    
    boolean isOnPyramid() {
        return leftLimit.get() && rightLimit.get();
    }
    
    boolean leftOnPyramid() {
        return leftLimit.get();
    }
    
    boolean rightOnPyramid() {
        return rightLimit.get();
    }
    
}
