/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author freshplum
 */
public class Lifters {

    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;
    boolean switchOverride;

    Lifters(DoubleSolenoid lifterSolenoid,
            DigitalInput leftLimit, DigitalInput rightLimit) {
        this.lifterSolenoid = lifterSolenoid;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
        enableSwitch();
    }

    //Disables limit swtiches
    void disableSwitch() {
        switchOverride = true;
    }

    //Re-enables limit switches
    void enableSwitch() {
        switchOverride = false;
    }
    
    //Raises the lifters
    void raise() {
        lifterSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    
    //Lowers the lifters
    void lower() {
        lifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    //Returns true if both of the limit swtiches are hit
    boolean isOnPyramid() {
        return leftOnPyramid() && rightOnPyramid();
    }

    //Returns true if left limit swtich is hit
    boolean leftOnPyramid() {
        if (!switchOverride) {
            return leftLimit.get();
        } else {
            return false;
        }
    }

    //REturns tru eif right limit swtich is hit
    boolean rightOnPyramid() {
        if (!switchOverride) {
            return rightLimit.get();
        } else {
            return false;
        }
    }
}