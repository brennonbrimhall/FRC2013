<<<<<<< HEAD
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
=======
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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
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
    
    //Prints statuses to the driver station
    StatusPrinter statusPrinter;
    
    // Controllers for the drivers to use
    LogitechGamepadController driverStick;
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
    
    //Drivetrain
    Drivetrain drivetrain;
    Talon fl;
    Talon bl;
    Talon fr;
    Talon br;
    Gyro gyro;
    Encoder driveEncoder;
    
    //Lifter
    Lifters lifters;
    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;
    Relay leftLight;
    Relay rightLight;
    
    //Autonomous Mode Constants and Variables
    final int kNoAuto = 1;
    final int kTwoDiskAuto = 2;
    final int kThreeDiskAuto = 3;
    final int kFourDiskAuto = 4;
    int autoMode;
    
    //Three Disk Auto Constants and Variables
    final int kThreeDiskAutoReverse = 1;
    final int kThreeDiskAutoTurn = 2;
    final int kThreeDiskAutoFire = 3;
    final int kThreeDiskAutoWait = 4;
    final double kThreeDiskAutoReverseDistance = 1796 * .7;
    final double kThreeDiskAutoTurnDistance = kThreeDiskAutoReverseDistance - 216 * .2 * .5;
    int threeDiskAutoState;
    
    //Cycle Counter --
    //We will reset the indexer and lock a certain number of cycles after the
    //shooting ends, keep track of how long it has been here.  This global var
    //could exceed its max threshold if the robot has been operational for a 
    //year or so (yes, we did the math).
    int cycleCounter;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void init() {
        tray.reset();
        lifters.raise();
        cycleCounter = 0;
        drivetrain.resetGyro();
        drivetrain.resetRightDistance();
    }

    public void robotInit() {
        driverStick = new LogitechGamepadController(1);
        operatorStick = new LogitechDualActionController(2);

        compressor = new Compressor(1, 1);
        compressor.start();

        fl = new Talon(1);
        bl = new Talon(2);
        fr = new Talon(3);
        br = new Talon(4);

        gyro = new Gyro(1);

        driveEncoder = new Encoder(6, 7);

        drivetrain = new Drivetrain(fl, bl, fr, br, gyro, driveEncoder);

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

        lifters = new Lifters(lifterSolenoid, leftLimit, rightLimit);

        leftLight = new Relay(2);
        rightLight = new Relay(3);

    }

    public void autonomousInit() {
        //Lowering tray; setting up for all auto modes
        init();

        //Setting cycle counter; will use in various auto modes
        cycleCounter = 0;

        //Deciding autonomous mode
        decideAutonomous();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        //Incrementing cycles
        cycleCounter++;

        //Deciding autonomous function call based on autoMode using a switch or
        //break setup to improve speed.
        switch (autoMode) {
            case kTwoDiskAuto:
                twoDiskAutonomousPeriodic();
                break;
            case kThreeDiskAuto:
                threeDiskAutonomousPeriodic();
                break;
            case kFourDiskAuto:
                fourDiskAutonomousPeriodic();
                break;
            case kNoAuto:
                break;
        }

        //Updating tray
        tray.update();
    }

    private void decideAutonomous() {
        //Setting two disk auto if DIO 2 is on
        if (DriverStation.getInstance().getDigitalIn(2)) {
            autoMode = kTwoDiskAuto;
            //Setting three disk auto if DIO 3 is on
        } else if (DriverStation.getInstance().getDigitalIn(3)) {
            autoMode = kThreeDiskAuto;
            //Setting four disk auto if DIO 4 is on
        } else if (DriverStation.getInstance().getDigitalIn(4)) {
            autoMode = kFourDiskAuto;
            //Defaulting to no autonomous if no DIO is on
        } else {
            autoMode = kNoAuto;
        }
    }

    private void twoDiskAutonomousPeriodic() {
        if (cycleCounter < 25) {
            //Waiting for tray to come down, etc.
        } else if (cycleCounter < 75) {
            //Backing up to pyramid
            if (!lifters.isOnPyramid()) {
                drivetrain.safeArcadeDrive(-.15, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            } else {
                drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            }
        } else if (cycleCounter < 200) {
            //Shooting first disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.shoot();
        } else {
            //Shooting second disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.notShoot();
        }
        statusPrinter.printStatuses();
    }

    private void threeDiskAutonomousPeriodic() {
        if (threeDiskAutoState == kThreeDiskAutoReverse) {
            drivetrain.arcadeDrive(.5, 0);
            if (drivetrain.getRightDistance() >= kThreeDiskAutoReverseDistance) {
                threeDiskAutoState = kThreeDiskAutoTurn;
            }
        } else if (threeDiskAutoState == kThreeDiskAutoTurn) {
            drivetrain.arcadeDrive(0, -.4);
            if (drivetrain.getRightDistance() <= kThreeDiskAutoTurnDistance) {
                threeDiskAutoState = kThreeDiskAutoFire;
            }
        } else if (threeDiskAutoState == kThreeDiskAutoFire) {
            drivetrain.arcadeDrive(0, 0);
            tray.shoot();
            if (cycleCounter >= 650) {
                threeDiskAutoState = kThreeDiskAutoWait;
            }
        } else if (threeDiskAutoState == kThreeDiskAutoWait) {
            drivetrain.arcadeDrive(0, 0);
            tray.notShoot();
        }
    }

    private void fourDiskAutonomousPeriodic() {
        //System.out.println(drivetrain.getRightDistance());
        //Lower tray
        if (cycleCounter < 25) {
            //Waiting for tray to lower and turning on collection
            tray.beltOn();
            tray.collectorOn();
        //Moving forwards
        } else if (cycleCounter < 30) {
            drivetrain.arcadeDrive(-.6, 0);
        } else if (cycleCounter < 220) {
            drivetrain.arcadeDrive(-.15, 0);
            drivetrain.gyro.reset();
            drivetrain.resetRightDistance();
        //Turning
        } else if (cycleCounter < (285)) {
            tray.beltOff();
            tray.collectorOff();
            drivetrain.pivot(Voltage.voltageToPWM(6.1));
        } else if (cycleCounter < 370){
            //drivetrain.arcadeDrive(0, 0);
            drivetrain.safeArcadeDrive(-.35, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
        } else if (cycleCounter < 400){
            drivetrain.safeArcadeDrive(-.2, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
        } else if (cycleCounter < 600) {
            tray.shoot();
        } else if (cycleCounter < 700) {
            tray.notShoot();
        }
        
    }

    //Shoot 2
    //Make turn
    //Backup and collect Disks
    //90 degree turn
    //Backup to Pyramid
    //Fire
    public void teleopInit() {
        init();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Turn the lights on and off
        lights();
        
        tray.setShooterVoltage();
        driveOI();
        trayRaiseOI();
        collectorOI();
        beltOI();
        liftersOI();
        
        allOffOI();
        shooterOnOI();
        limitSwitchesOI();
        shootOI();
        
        //Updating tray
        tray.update();
        
        //Updating statuses
        statusPrinter.printStatuses();

    }
    
    private void trayRaiseOI(){
        // Raise and slower the tray using A and B buttons
        if (operatorStick.getButton(6)) {
            tray.raise();
        }
        if (operatorStick.getButton(8)) {
            tray.lower();
        }
    }
    
    private void collectorOI(){
        //Collector on, off, and reverse
        if (operatorStick.getButton(4)) {
            tray.collectorOn();
        }
        if (operatorStick.getButton(3) || operatorStick.getButton(1)) {
            tray.collectorOff();
        }
        if (operatorStick.getButton(2)) {
            tray.collectorReverse();
        }
    }
    
    private void beltOI(){
        //Belt on, off, and reverse
        if (operatorStick.dPadUp()) {
            tray.beltOn();
        }
        if (operatorStick.dPadLeft() || operatorStick.dPadRight()) {
            tray.beltOff();
        }
        if (operatorStick.dPadDown()) {
            tray.beltReverse();
        }
    }
    
    private void liftersOI(){
        //Raise and lower the lifters with left bumper and limit switches
        //with right bumper override.
        if (driverStick.getLeftBumper() && (driverStick.getRightBumper() || (lifters.isOnPyramid()))) {
            lifters.lower();
        }
        if (driverStick.getB()) {
            lifters.raise();
        }
    }
    
    private void driveOI(){
        //Drive the robot
        drivetrain.safeCheesyDrive(driverStick.getLeftY(), 
                driverStick.getRighttX(), driverStick.getAnalogTriggers(),
                lifters.leftOnPyramid(), lifters.rightOnPyramid());
    }
    
    private void shootOI(){
        //Shoot!
        if (operatorStick.getButton(5) && (operatorStick.getButton(7) || (lifters.isOnPyramid()))) {
            tray.shoot();
        } else {
            tray.notShoot();
        }
    }
    
    private void limitSwitchesOI(){
        //Disabling limit switches
        if (driverStick.getX()) {
            lifters.disableSwitch();
        }
        
        //Enabling limit switches 
        if (driverStick.getY()) {
            lifters.enableSwitch();
        }
    }
    
    private void allOffOI(){
        //All off button - turns all tray functions off
        if (operatorStick.getButton(9)) {
            tray.allOff();
        }
    }
    
    private void shooterOnOI(){
        //Turns shooter on
        if (operatorStick.getButton(10)) {
            tray.shooterOn();
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testInit() {
        //Turning off drivetrain
        drivetrain.arcadeDrive(0, 0);
        
        //Starting compressor
        compressor.start();
        
        //Raising lifters
        lifters.raise();
        
        //Turning tray off/into test mode
        tray.test();
    }

    public void testPeriodic() {
        //Turning lights on and off
        lights();
        
        //Limited driver OI
        trayRaiseOI();
        collectorOI();
        beltOI();
        liftersOI();
        allOffOI();
        
        //Printing statuses
        statusPrinter.printStatuses();
        
        //Updating tray
        tray.update();
    }
    
    private void lights(){
        if (lifters.leftOnPyramid()) {
            leftLight.set(Relay.Value.kForward);
        } else {
            leftLight.set(Relay.Value.kOff);
        }
        if (lifters.rightOnPyramid()) {
            rightLight.set(Relay.Value.kForward);
        } else {
            rightLight.set(Relay.Value.kOff);
        }
    }
>>>>>>> origin/master
}