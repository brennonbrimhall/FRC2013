/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author freshplum
 */
public class Drivetrain {
    Talon frontLeft;
    Talon backLeft;
    Talon frontRight;
    Talon backRight;
    
    
    Drivetrain(Talon frontLeft, Talon backLeft, Talon frontRight, Talon backRight) {
        this.frontLeft  = frontLeft;
        this.backLeft   = backLeft;
        this.frontRight = frontRight;
        this.backRight  = backRight;
    }
    
    void arcadeDrive(double speed, double turn) {
        frontLeft.set(speed + turn);
        backLeft.set(speed + turn);
        
        frontRight.set(-speed+turn);
        backRight.set(-speed+turn);
    }
}
