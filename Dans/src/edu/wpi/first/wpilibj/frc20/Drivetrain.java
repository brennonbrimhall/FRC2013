/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;

/**
 *
 * @author freshplum
 */
public class Drivetrain {
    //Maximum PWM for "Safe" drive functions
    final double kMaxPyramidSpeed = .1;
    final double kWheelDiamaterInInches = 8;
    final double kRobotWidthInInches = 23.5;
    
    //Global variables for speed values
    double leftSpeed;
    double rightSpeed;
    
    //Globals for heading
    double initialLeft = 0;
    double initialRight = 0;
    double heading = 0;
    
    //Drive CIMs
    Talon frontLeft;
    Talon backLeft;
    Talon frontRight;
    Talon backRight;
    
    //Sensors
    Gyro gyro;
    Encoder leftEncoder;
    Encoder rightEncoder;

    Drivetrain(Talon frontLeft, Talon backLeft, Talon frontRight, Talon backRight, Gyro gyro) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
        this.gyro = gyro;
    }
    
    Drivetrain(Talon frontLeft, Talon backLeft, Talon frontRight, Talon backRight, Gyro gyro, Encoder leftEncoder, Encoder rightEncoder) {
        this.frontLeft = frontLeft;
        this.backLeft = backLeft;
        this.frontRight = frontRight;
        this.backRight = backRight;
        this.gyro = gyro;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.rightEncoder.start();
        this.leftEncoder.start();
        
        this.rightEncoder.setDistancePerPulse(kWheelDiamaterInInches*Math.PI/360);
        this.leftEncoder.setDistancePerPulse(kWheelDiamaterInInches*Math.PI/360);
    }
    
    double getRightDistance() {
        return -rightEncoder.getDistance();
    }
    
    double getLeftDistance() {
        return leftEncoder.getDistance();
    }
    
    void resetRightDistance() {
        rightEncoder.reset();
    }
    
    void resetLeftDistance() {
        leftEncoder.reset();
    }
    
    void resetDistance(){
        rightEncoder.reset();
        leftEncoder.reset();
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
        if (leftHit && leftSpeed > kMaxPyramidSpeed) {
            leftSpeed = kMaxPyramidSpeed;
        }
        if (rightHit && rightSpeed < -kMaxPyramidSpeed) {
            rightSpeed = -kMaxPyramidSpeed;
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
    
    double getHeading(){
        return heading;
    }
    
    double speedFunction(double x) {
        //In simple terms: |(x^3+x)|/2*sign(x)
        
        //To increase curving, up the powers of either of the terms.  Start with
        //x^3 to x^5, increasing by a power of two or one each time.
        //If more curving is needed by x^9, then maybe change x to x^2 or more. 
        //If you add a third or fourth term, increase the divisor accordingly.

        return heaviside(x) * Math.abs(((MathUtils.pow(x, 5)) + (x)) / 2);
    }

    void resetGyro() {
        gyro.reset();
    }
    
    void updateHeading() {
        double newRight = getRightDistance();
        double newLeft = getLeftDistance();
        
        double deltaRight = newRight - initialRight;
        initialRight = newRight;
        
        double deltaLeft = newLeft - initialLeft;
        initialLeft = newLeft;
        
        double deltaTheta = (deltaRight-deltaLeft)/kRobotWidthInInches;
        deltaTheta = deltaTheta*180/Math.PI;
        heading += deltaTheta;
        
        getLowestExpressionForAngle(heading);
    }
    
    void getLowestExpressionForAngle(double angleInDegrees){
        if(angleInDegrees >= 360){
            angleInDegrees = angleInDegrees-360;
        }else if(angleInDegrees <= 0){
            angleInDegrees = angleInDegrees+360;
        }
    }
    
    void resetHeading(){
        heading = 0;
    }
    
    void pivot(double speed) {
        leftSpeed = speed;
        rightSpeed = speed;
        driveMotors();
    }   
}
