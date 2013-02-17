/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    // Controllers for the drivers to use
    XboxController driverStick;
    XboxController operatorStick;
    
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
    int cyclesSinceShooting;
    
    
    
    // The drivetrain
    Drivetrain drivetrain;
    Talon fl;
    Talon bl;
    Talon fr;
    Talon br;
    
    // Lifter
    Lifter lifter;
    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        driverStick = new XboxController(1);
        operatorStick = new XboxController(2);
        
        compressor = new Compressor(1,1);
        compressor.start();

        fl = new Talon(1);
        bl = new Talon(2);
        fr = new Talon(3);
        br = new Talon(4);


        drivetrain = new Drivetrain(fl, bl, fr, br);

        shooterTalon = new Talon(5);
        beltTalon = new Talon(6);
        collectorTalon = new Talon(7);
        traySolenoid = new DoubleSolenoid(1,2);
        latchSolenoid = new DoubleSolenoid(3,4);
        indexerSolenoid = new DoubleSolenoid(5,6);
        shooterEncoder = new Encoder(1,2);
        
        tray = new Tray(shooterTalon,beltTalon,collectorTalon,traySolenoid,latchSolenoid,indexerSolenoid,shooterEncoder);
        
        cyclesSinceShooting = 0;
        
        lifterSolenoid = new DoubleSolenoid(7,8);
        leftLimit = new DigitalInput(2);
        rightLimit = new DigitalInput(3);
        
        lifter = new Lifter(lifterSolenoid,leftLimit,rightLimit);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        // Drive the robzot
        drivetrain.arcadeDrive(driverStick.getLeftY(), driverStick.getRighttX());
        
        // Raise and slower the tray using A and B buttons
        if(operatorStick.getA()) {
            tray.raise();
        }
        if(operatorStick.getB()) {
            tray.lower();
        }
        
        // Raise and lower the lifters using the left bumper and either
        // the limit switches or the right bumper
        if(driverStick.getLeftBumper() && (driverStick.getRightBumper() || (lifter.isOnPyramid()))) {
            lifter.lower();
        }
        if(driverStick.getBack()) {
            lifter.raise();
        }
        if(driverStick.getStart()) {
            lifter.release();
        }
        
        // Request to turn on and off the collector.
        // If we are shooting the requests will be ignored
        if(driverStick.getA()) {
            tray.collectorOn();
        }
        if(driverStick.getB()) {
            tray.collectorOff();
        }
        
        // Shoot!
        if(operatorStick.getLeftBumper() && (operatorStick.getRightBumper() || (lifter.isOnPyramid()))) {
            tray.startShooting();
            cyclesSinceShooting = 0;
        } else {
            tray.stopShooting();
            cyclesSinceShooting += 1;
            if(cyclesSinceShooting == 5) {
                tray.resetIndexer();
            }
        }

    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
}
