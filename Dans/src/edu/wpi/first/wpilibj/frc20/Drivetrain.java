/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author freshplum
 */
public class Drivetrain {

    final double kMaxPyramidSpeed = .1;
    //Deadband for speed function.  Talons have a 4% deadband threshold built in.
    final double deadbandThreshold = 0.06; //6%
    Talon frontLeft;
    Talon backLeft;
    Talon frontRight;
    Talon backRight;
    Gyro gyro;
    double leftSpeed;
    double rightSpeed;
    
    Encoder leftEncoder;
    Encoder rightEncoder;

    Drivetrain(Talon frontLeft, Talon backLeft, Talon frontRight, Talon backRight, Gyro gyro) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
        this.gyro = gyro;
    }

    private void driveMotors() {
        frontLeft.set(leftSpeed);
        backLeft.set(leftSpeed);
        frontRight.set(rightSpeed);
        backRight.set(rightSpeed);
        //System.out.println("Gyro: " + gyro.getAngle());
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

    void safeCheesyDrive(double speed, double cheesyTurn, double hardTurn, boolean leftHit, boolean rightHit) {
        double s = speedFunction(speed);
        if (s > 0) {
            safeArcadeDrive(speedFunction(speed), speedFunction(speed) * cheesyTurn - hardTurn, leftHit, rightHit);
        } else {
            safeArcadeDrive(speedFunction(speed), -speedFunction(speed) * cheesyTurn - hardTurn, leftHit, rightHit);
        }
    }

    double heaviside(double x) {
        if (x > 0) {
            return 1;
        }
        if (x < 0) {
            return -1;
        }
        return 0;
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

        return heaviside(x) * Math.abs(((x * x * x * x * x) + (x)) / 2);
    }

    void resetGyro() {
        gyro.reset();
    }
}
