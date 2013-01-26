/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    
    
    
    LowerRollers lowerRollers;
    UpperRollers upperRollers;
    DriveTrain driveTrain;
    
    XboxController xbox;
    JoyStick2 controlStick;
    
    ShooterRollers shooterRollers;
    TurnTable turnTable;
    
    CowCatcher cowCatcher;
    
    boolean cowCatcherDown = false;
    
    boolean c10Prev = false;
    boolean c11Prev = false;
    
    Stinger stinger;
    
    public void robotInit() {
                // COMPETITION ROBOT
        
        lowerRollers = new LowerRollers(7,4);
        upperRollers = new UpperRollers(8,9);
        driveTrain = new DriveTrain(6,5,2,3);
        
        shooterRollers = new ShooterRollers(5,6);
//        shooterRollers = new ShooterRollers(13,14);
        turnTable = new TurnTable(11);
        
        
        // PRACTICE ROBOT
//        lowerRollers = new LowerRollers(7,3);
//        upperRollers = new UpperRollers(8,6);
//        driveTrain = new DriveTrain(2,100,5,40);
//        
//        shooterRollers = new ShooterRollers(10,9);
//        turnTable = new TurnTable(2);
        
        xbox = new XboxController(1);
        controlStick = new JoyStick2(2);
        
        stinger = new Stinger(1);
        
        // Competition
        cowCatcher = new CowCatcher(1,2,1,3);
        
        // Practice
//        cowCatcher = new CowCatcher(3,1,2,4);
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        
        if(DriverStation.getInstance().getDigitalIn(8)) {
            double startTime = Timer.getFPGATimestamp() + DriverStation.getInstance().getAnalogIn(3) * 3;
            double time2 = startTime + DriverStation.getInstance().getAnalogIn(4) * 3;
            lowerRollers.stop();
            while(isEnabled() && isAutonomous()) {
//                shooterRollers.setSpeed(1000 * DriverStation.getInstance().getAnalogIn(1), 1000 * DriverStation.getInstance().getAnalogIn(2));
                shooterRollers.setSpeed(600, 3250);
                if(startTime > Timer.getFPGATimestamp()) {
                    
                } else if(time2 > Timer.getFPGATimestamp()) {
                    upperRollers.start();
                } else {
                    lowerRollers.start();
                }
                lowerRollers.update();
                
            }
        } else {
        
            double forwardEncoderTicks = 3500;
//            if(DriverStation.getInstance().getDigitalIn(1)) {
//                forwardEncoderTicks = 4500;
//            }
            boolean drivingForward = true;
            cowCatcherDown = false;


            lowerRollers.stop();
            lowerRollers.update();
            upperRollers.stop();

            boolean usingCanBus = true;

            double startTime = Timer.getFPGATimestamp() + DriverStation.getInstance().getAnalogIn(1) * 3;

            double firstDelay  = DriverStation.getInstance().getAnalogIn(2) * 3;
            double secondDelay = DriverStation.getInstance().getAnalogIn(3) * 3 + firstDelay;

            while(isEnabled() && isAutonomous()) {
                if(startTime > Timer.getFPGATimestamp()) {
                    cowCatcherDown = true;
                    System.out.println("1");
                } else if(startTime + firstDelay > Timer.getFPGATimestamp()) {
                    cowCatcherDown = true;
                    lowerRollers.backDriveFront();
                    lowerRollers.backDriveRear();
                    System.out.println("2");
                } else if(startTime + secondDelay > Timer.getFPGATimestamp()) {
                    cowCatcherDown = true;
                    lowerRollers.backDriveFront();
                    lowerRollers.backDriveRear();
                    upperRollers.backDrive();
                    System.out.println("3");
                } else if(drivingForward && driveTrain.getPosition(usingCanBus) /*driveEncoder.getDistance()*/ < forwardEncoderTicks) {
                    cowCatcherDown = true;
                    
                    upperRollers.stop();
                    lowerRollers.start();
                    double speed = driveTrain.getPosition(usingCanBus) /*driveEncoder.getDistance()*/ / forwardEncoderTicks;
                    speed = Math.min(speed,1);
                    speed = Math.max(0,speed);
                    if (speed > .5) {
                        speed = 1-speed;
                    }
                    speed *= 2;
                    speed += .2;
                    speed = Math.min(speed,1);
                    speed = Math.max(0,speed);
                    driveTrain.drive(-speed,0);
                    System.out.println("4");
                } else if(drivingForward) {
                    lowerRollers.start();
                    upperRollers.start();
                    shooterRollers.setSpeed(500,1000);
    //                turnTable.unOverride();
    //                cowCatcher.setState(true);
                    drivingForward = false;
                    driveTrain.drive(0, 0);
    //                    driveEncoder.reset();
    //                    System.out.println("2");
                    System.out.println("5");
                }

                cowCatcher.setDown();
                cowCatcher.update();
                lowerRollers.update();
            }
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        
        lowerRollers.start();
//        shooterRollers.setSpeed(1000*DriverStation.getInstance().getAnalogIn(1),1000*DriverStation.getInstance().getAnalogIn(2));
        shooterRollers.setSpeed(1000,4000);
        while (isEnabled() && isOperatorControl()){
            
            //line added jb below
            shooterRollers.setTopSpeed((((DriverStation.getInstance().getAnalogIn(4)) / 5.) * 0.75));
            
            //    shooterRollers.setTopSpeed(controlStick.getThrottle());
            //end of new line
            if(xbox.leftJoyLeftRight() > .5) {
                stinger.open();
            } else {
                stinger.close();
            }
            
            if(controlStick.getButton(8)) {
                shooterRollers.setSpeed(1000,4000);
            } else if(controlStick.getButton(9)) {
                shooterRollers.setSpeed(0,0);
            } else if(controlStick.getButton(5)) {
                shooterRollers.setSpeed(10000,10000);
            }
            
            if(controlStick.getButton(1)) {
                upperRollers.start();
            } else if(controlStick.getButton(3) || controlStick.getButton(4)) {
                upperRollers.backDrive();
            } else {
                upperRollers.stop();
            }
            
            if(xbox.bButton()) {
                driveTrain.drive(0,0);
            } else {
                if(xbox.xButton()) {
                    driveTrain.drive(-xbox.rightJoyUpDown()/2,0);
                } else {
                    driveTrain.drive(-xbox.rightJoyUpDown(),xbox.triggers());
                }
            }
            
            if(xbox.dPadLeftRight() > .5) {
                cowCatcherDown = true;
            } else if(xbox.dPadLeftRight() < -.5) {
                cowCatcherDown = false;
            }
            
            if(controlStick.getButton(2)) {
                turnTable.setVoltage(-controlStick.getAxis(1));
            } else {
                turnTable.setVoltage(0);
            }
            
            if(cowCatcherDown) {
//                turnTable.override();
//                if( turnTable.atPosition() ) {
                    cowCatcher.setDown();
//                }
            } else {
                cowCatcher.setUp();
//                if(cowCatcher.isUp()) {
//                    turnTable.unOverride();
//                }
            }
            
            // Button 10 toggles rear roller
            if(controlStick.getButton(10) && !c10Prev) {
                lowerRollers.toggleRear();
            }
            
            // Button 11 toggles rear roller
            if(controlStick.getButton(11) && !c11Prev) {
                lowerRollers.toggleFront();
            }
            
            // Button 6 backdrives the front roller
            if(controlStick.getButton(6)) {
                lowerRollers.backDriveFront();
            }
            
            // Button 7 backdrives the rear roller
            if(controlStick.getButton(7)) {
                lowerRollers.backDriveRear();
            }
            
            if(controlStick.getButton(4)) {
                lowerRollers.backDriveFront();
                lowerRollers.backDriveRear();
                upperRollers.backDrive();
            }
            
            c10Prev = controlStick.getButton(10);
            c11Prev = controlStick.getButton(11);
            
            turnTable.update();
            cowCatcher.update();
            lowerRollers.update();
        }
    }
  
}
