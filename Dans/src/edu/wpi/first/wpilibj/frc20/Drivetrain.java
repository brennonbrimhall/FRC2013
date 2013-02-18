/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.RobotDrive;
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
    
    double leftSpeed;
    double rightSpeed;
    
    final double kMaxPyramidSpeed = .3;

    Drivetrain(Talon frontLeft, Talon backLeft, Talon frontRight, Talon backRight) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
    }
    
    private void driveMotors() {
        frontLeft.set(leftSpeed);
        backLeft.set(leftSpeed);
        frontRight.set(rightSpeed);
        backRight.set(rightSpeed);
    }

    void arcadeDrive(double speed, double turn) {
        leftSpeed = speed-turn;
        rightSpeed = -speed-turn;
        driveMotors();
    }

    void safeArcadeDrive(double speed, double turn, boolean leftHit, boolean rightHit) {
        leftSpeed = speed-turn;
        rightSpeed = -speed-turn;
        if(leftHit && leftSpeed < -kMaxPyramidSpeed) {
            leftSpeed = -kMaxPyramidSpeed;
        }
        if(rightHit && rightSpeed > kMaxPyramidSpeed) {
            rightSpeed = kMaxPyramidSpeed;
        }
        driveMotors();
    }
    /*
     void cheesyDrive(double speed, double turn, boolean turnInPlace) {
     if (turnInPlace) {
     drive.arcadeDrive(0, turn);
     } else {
     drive.arcadeDrive(speed, speed * turn);
     }
     }
     */
}
