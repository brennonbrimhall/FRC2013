/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;  // This line added by Joe C. on 06/24/2012

public class ShooterRollers {
//
//    CANJaguar2 topJag;
//    CANJaguar2 bottomJag;

    Jaguar topJag;
    Jaguar bottomJag;

    double topTargetSpeed = .7;
    double topCurrentSpeed = .7;

    double bottomTargetSpeed = .7;
    double bottomCurrentSpeed = .7;

    double rampRate = 150;

    double topAverageSpeed = 0;
    double bottomAverageSpeed = 0;

    double topPrevSpeed = 0;
    double bottomPrevSpeed = 0;

    double timeForSet = 0;

    ShooterRollers(int topAddress, int bottomAddress) {
        topJag = new Jaguar(topAddress);
        bottomJag = new Jaguar(bottomAddress);
//        topJag = new CANJaguar2(topAddress);
//        bottomJag = new CANJaguar2(bottomAddress);
//
//        topJag.setSpeedMode(360, 1.4, .004, 0);
//        bottomJag.setSpeedMode(360, 1.4, .004, 0);
    }

    public void speedyUpdate() {
        topCurrentSpeed = topTargetSpeed;
        bottomCurrentSpeed = bottomTargetSpeed;
    }

    public void refresh() {
        if(timeForSet < Timer.getFPGATimestamp()) {
//            topJag.setSpeedMode(360, 1.4, .004, 0);
//            bottomJag.setSpeedMode(360, 1.4, .004, 0);
            timeForSet = Timer.getFPGATimestamp() + 1;
        }
    }

    public void update() {
//        System.out.println("Top:    " + topTargetSpeed + ", " + topCurrentSpeed + ", " + topJag.getSpeed());
//        System.out.println("Bottom: " + topTargetSpeed + ", " + topCurrentSpeed + ", " + topJag.getSpeed());
//        System.out.println("Current: " + topJag.getCurrent() + " , " + bottomJag.getCurrent());
        if(topTargetSpeed - topCurrentSpeed > rampRate) {
            topCurrentSpeed += rampRate;
        } else if(topTargetSpeed - topCurrentSpeed < -rampRate) {
            topCurrentSpeed -= rampRate;
        } else {
            topCurrentSpeed = topTargetSpeed;
        }

        if(bottomTargetSpeed - bottomCurrentSpeed > rampRate) {
            bottomCurrentSpeed += rampRate;
        } else if(bottomTargetSpeed - bottomCurrentSpeed < -rampRate) {
            bottomCurrentSpeed -= rampRate;
        } else {
            bottomCurrentSpeed = bottomTargetSpeed;
        }

//        topJag.setX(topCurrentSpeed);
//        bottomJag.setX(bottomCurrentSpeed);

//        topAverageSpeed = .5*topAverageSpeed + .5*topJag.getSpeed();
//        bottomAverageSpeed = .5*bottomAverageSpeed + .5*bottomJag.getSpeed();

        topPrevSpeed = topJag.getSpeed();
        bottomPrevSpeed = bottomJag.getSpeed();

        topJag.set(topTargetSpeed);
        bottomJag.set(bottomTargetSpeed);

    }

    public void setSpeed(int topRpm, int bottomRpm) {
        setTopSpeed(topRpm);
        setBottomSpeed(bottomRpm);
    }

/*  Joe C. replaced the following 5 routines -- see below -- on 06/24/2012
    public void start() {
        topTargetSpeed = -.7;
        bottomTargetSpeed = .7;
    }

    public void startHalf() {
        topTargetSpeed = -.1;
        bottomTargetSpeed = .2;
    }

    public void stop() {
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;
    }

    public void setTopSpeed(int rpm) {
//        topTargetSpeed = -rpm;
//        topTargetSpeed = -.7;
    }

    public void setBottomSpeed(int rpm) {
//        bottomTargetSpeed = rpm;
//        bottomTargetSpeed = .7;
    }
*/

// Note: The slider (@AnalogIn(4)) has a range of 0 to 5.  We treat that as 0% to 100% of full speed.
// Note: Oops.  For now, we will limit it to 75% of full speed.

    public void start() {
        bottomTargetSpeed = ((DriverStation.getInstance().getAnalogIn(4)) / 5.) * 0.75;
        topTargetSpeed = -bottomTargetSpeed;
    }

    public void startHalf() {
        bottomTargetSpeed = ((DriverStation.getInstance().getAnalogIn(4)) / 5.) * 0.75;
        topTargetSpeed = -bottomTargetSpeed;
    }

    public void stop() {
        topTargetSpeed = 0;
        bottomTargetSpeed = 0;
    }

    public void setTopSpeed(int rpm) {
        bottomTargetSpeed = ((DriverStation.getInstance().getAnalogIn(4)) / 5.) * 0.75;
        topTargetSpeed = -bottomTargetSpeed;
    }

    public void setBottomSpeed(int rpm) {
        bottomTargetSpeed = ((DriverStation.getInstance().getAnalogIn(4)) / 5.) * 0.75;
        topTargetSpeed = -bottomTargetSpeed;
    }

//    public void stop() {
//        setTopSpeed(0);
//        setBottomSpeed(0);
//    }

    public double getTopSpeed() {
        return -topJag.getSpeed();
    }

    public double getBottomSpeed() {
        return bottomJag.getSpeed();
    }

    public boolean atSpeed() {
        // TODO: Tighten these based on how good the averaging works
        return /*(Math.abs(topPrevSpeed - topTargetSpeed) < 50 && Math.abs(bottomPrevSpeed - bottomTargetSpeed) < 50) ||*/
//                Math.abs(topJag.getSpeed() - topTargetSpeed) < 50 && Math.abs(bottomJag.getSpeed() - bottomTargetSpeed) < 50;
                true;
    }

    public double getTopTarget() {
        return topTargetSpeed;
    }

    public double getBottomTarget() {
        return bottomTargetSpeed;
    }

}
