/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    //Watchdog to send statuses to the Drivers
    StatusPrinter statusPrinter;
    // Controllers for the drivers to use
    XboxController driverStick;
    LogitechDualActionController operatorStick;
    // Compressor!
    Compressor compressor;
    // Shooter, Tray, and Collector
    Tray tray;
    Talon shooterTalon;
    Talon beltTalon;
    Talon collectorTalon;
    DoubleSolenoid traySolenoid;
    DoubleSolenoid latchSolenoid;
    DoubleSolenoid indexerSolenoid;
    Encoder shooterEncoder;
    // We will reset the indexer and lock a certain number of cycles after the
    // shooting ends, keep track of how long it has been here
    int cycleCounter;
    // The drivetrain
    Drivetrain drivetrain;
    Talon fl;
    Talon bl;
    Talon fr;
    Talon br;
    Gyro gyro;
    // Lifter
    Lifter lifter;
    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;
    
    Relay leftLight;
    Relay rightLight;
    //frame led lights
    Relay bottomLeft;
    Relay bottomRight;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public void init() {
        tray.reset();
        lifter.raise();
        cycleCounter = 0;
        drivetrain.resetGyro();
        
    }
    
    public void robotInit() {
        driverStick = new XboxController(1);
        operatorStick = new LogitechDualActionController(2);

        compressor = new Compressor(1, 1);
        compressor.start();

        fl = new Talon(1);
        bl = new Talon(2);
        fr = new Talon(3);
        br = new Talon(4);

        gyro = new Gyro(1);

        drivetrain = new Drivetrain(fl, bl, fr, br, gyro);

        shooterTalon = new Talon(5);
        beltTalon = new Talon(6);
        collectorTalon = new Talon(7);
        traySolenoid = new DoubleSolenoid(1, 2);
        latchSolenoid = new DoubleSolenoid(3, 4);
        indexerSolenoid = new DoubleSolenoid(5, 6);
        shooterEncoder = new Encoder(4, 5);
        shooterEncoder.start();

        tray = new Tray(shooterTalon, beltTalon, collectorTalon, traySolenoid, latchSolenoid, indexerSolenoid, shooterEncoder);
        statusPrinter = new StatusPrinter(tray);

        cycleCounter = 0;

        lifterSolenoid = new DoubleSolenoid(7, 8);
        leftLimit = new DigitalInput(2);
        rightLimit = new DigitalInput(3);

        lifter = new Lifter(lifterSolenoid, leftLimit, rightLimit);
        
        leftLight = new Relay(2);
        rightLight = new Relay(3);
        //bottom frame led lights
        bottomLeft = new Relay(0);
        bottomRight = new Relay(0);
    }
    
    public void autonomousInit() {
        init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        cycleCounter += 1;
        if (cycleCounter < 100) {
            
        } else
        if (cycleCounter < 400) {
            if(!lifter.isOnPyramid()) {
                drivetrain.safeArcadeDrive(-.1, 0, lifter.leftOnPyramid(), lifter.rightOnPyramid());
            } else {
                drivetrain.safeArcadeDrive(0, 0, lifter.leftOnPyramid(), lifter.rightOnPyramid());
            }
        } else if (cycleCounter < 500) {
            drivetrain.safeArcadeDrive(0, 0, lifter.leftOnPyramid(), lifter.rightOnPyramid());
            tray.shoot();
        } else {
            drivetrain.safeArcadeDrive(0, 0, lifter.leftOnPyramid(), lifter.rightOnPyramid());
            tray.notShoot();
        }
        tray.update();
        statusPrinter.printStatuses();

    }

    public void teleopInit() {
        init();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //led frame lights on
        bottomLeft.set(Relay.Value.kForward);
        bottomRight.set(Relay.Value.kForward);
        // Turn the lights on and off
        if(lifter.leftOnPyramid()) {
            leftLight.set(Relay.Value.kForward);
        } else {
            leftLight.set(Relay.Value.kOff);
        }
        if(lifter.rightOnPyramid()) {
            rightLight.set(Relay.Value.kForward);
        } else {
            rightLight.set(Relay.Value.kOff);
        }
        

        // Drive the robot
        drivetrain.safeCheesyDrive(driverStick.getLeftY(), driverStick.getRighttX(),driverStick.getAnalogTriggers(),
                    lifter.leftOnPyramid(),lifter.rightOnPyramid());

        // Raise and slower the tray using A and B buttons
        if (operatorStick.getButton(6)) {
            tray.raise();
        }
        if (operatorStick.getButton(8)) {
            tray.lower();
        }
        if (operatorStick.getButton(4)){
            tray.collectorOn();
        }
        if (operatorStick.getButton(3) || operatorStick.getButton(1)){
            tray.collectorOff();
        }
        if (operatorStick.getButton(2)){
            tray.collectorReverse();
        }
        if (operatorStick.dPadUp()){
            tray.beltOn();
        }
        if (operatorStick.dPadLeft() || operatorStick.dPadRight()){
            tray.beltOff();
        }
        if (operatorStick.dPadDown()){
            tray.beltReverse();
        }
        if(operatorStick.getButton(9)) {
            tray.allOff();
        }
        if(operatorStick.getButton(10)) {
            tray.shooterOn();
        }

        // Raise and lower the lifters using the left bumper and either
        // the limit switches or the right bumper
        if (driverStick.getLeftBumper() && (driverStick.getRightBumper() || (lifter.isOnPyramid()))) {
            lifter.lower();
        }
        if (driverStick.getB()) {
            lifter.raise();
        }
        if(driverStick.getX()) {
            lifter.disableSwitch();
        }
        if(driverStick.getY()) {
            lifter.enableSwitch();
        }

        // Shoot!
        if (operatorStick.getButton(5) && (operatorStick.getButton(7) || (lifter.isOnPyramid()))) {
            tray.shoot();
            //cycleCounter = 0;
        } else {
            tray.notShoot();/*
            cycleCounter += 1;
            if (cycleCounter == 50) {
                tray.finishShooting();
            }*/
        }
        tray.update();
        statusPrinter.printStatuses();

    }

    /**
     * This function is called periodically during test mode
     */
    public void testInit() {
        compressor.start();
    }
    
    public void testPeriodic() {
        statusPrinter.printStatuses();
    }
    
    public void disabledInit(){
        System.out.println("ROBOT DISABLED / Bobby, Phil: Done loading code.");
    }
}