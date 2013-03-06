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
public class Lifter {

    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;
    boolean switchOverride;

    Lifter(DoubleSolenoid lifterSolenoid,
            DigitalInput leftLimit, DigitalInput rightLimit) {
        this.lifterSolenoid = lifterSolenoid;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
        enableSwitch();
    }

    void disableSwitch() {
        switchOverride = true;
    }

    void enableSwitch() {
        switchOverride = false;
    }

    void raise() {
        lifterSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    void lower() {
        lifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    /*
    void release() {
        lifterSolenoid.set(DoubleSolenoid.Value.kOff);
    }
    */

    boolean isOnPyramid() {
        return leftOnPyramid() && rightOnPyramid();
    }

    boolean leftOnPyramid() {
        if (!switchOverride) {
            return leftLimit.get();
        } else {
            return false;
        }
    }

    boolean rightOnPyramid() {
        if (!switchOverride) {
            return rightLimit.get();
        } else {
            return false;
        }
    }
}
