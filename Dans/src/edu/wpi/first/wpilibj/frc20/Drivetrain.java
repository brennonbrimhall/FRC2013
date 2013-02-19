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

    final double kMaxPyramidSpeed = .2;
    
    //Deadband for speed function.  Talons have a 4% deadband threshold built in.
    final double deadbandThreshold = 0.06; //6%
    
    Talon frontLeft;
    Talon backLeft;
    Talon frontRight;
    Talon backRight;
    double leftSpeed;
    double rightSpeed;

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
        leftSpeed = -speed + turn;
        rightSpeed = speed + turn;
        driveMotors();
    }

    void safeArcadeDrive(double speed, double turn, boolean leftHit, boolean rightHit) {
        leftSpeed = -speed + turn;
        rightSpeed = speed + turn;
        if (leftHit && leftSpeed < -kMaxPyramidSpeed) {
            leftSpeed = -kMaxPyramidSpeed;
        }
        if (rightHit && rightSpeed > kMaxPyramidSpeed) {
            rightSpeed = kMaxPyramidSpeed;
        }
        driveMotors();
    }

    void safeCheesyDrive(double speed, double turn, boolean leftHit, boolean rightHit, boolean turnInPlace) {
        if (turnInPlace) {
            safeArcadeDrive(0, speedFunction(turn), leftHit, rightHit);
        } else {
            safeArcadeDrive(speedFunction(speed), -speedFunction(speed) * turn, leftHit, rightHit);
        }
    }

    double speedFunction(double x) {
        //(x^3+x)/2*sign(x)
        //To increase curving, up the powers of either of the terms.  Start with
        //x^3 to x^5, increasing by a power of two each time.  (Going by one isn't
        //very obvious.  If more curving is needed by x^9, then maybe change x 
        //to x^2 or more.  If you add a third or fourth term, increase the divisor 
        //accordingly.

        //int sign = 0; //Defaulting to no movement if not within deadband.

        /*if (x > deadbandThreshold) {
            sign = 1; //You're positive and out of deadband
        } else if (x < -deadbandThreshold) {
            sign = -1; //You;re negative and out of deadband
        }*/

        return ((x * x * x * x * x) + (x)) / 2;
    }
}
