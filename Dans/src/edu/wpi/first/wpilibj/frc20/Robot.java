/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
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
    Counter shooterEncoder;
    
    //Blocker
    Blocker blocker;
    Talon blockerTalon;
    DigitalInput blockerUpSwitch;
    DigitalInput blockerDownSwitch;
    
    //Drivetrain
    Drivetrain drivetrain;
    Talon fl;
    Talon bl;
    Talon fr;
    Talon br;
    Gyro gyro;
    Encoder leftDriveEncoder;
    Encoder rightDriveEncoder;
    Relay shifterRelay;
    
    //Lifter
    Lifters lifters;
    DoubleSolenoid lifterSolenoid;
    DigitalInput leftLimit;
    DigitalInput rightLimit;
    Relay leftLight;
    Relay rightLight;
    
    //Flipper
    Flipper flipper;
    Relay flipperRelay;
    
    //Autonomous Mode Constants and Variables
    final int kTwoDiskNoMoveAuto = 1;
    final int kTwoDiskAuto = 2;
    final int kThreeDiskAuto = 3;
    final int kFourDiskAuto = 4;
    final int kCornerLeftAuto = 5;
    final int kCornerRightAuto = 6;
    final int kMiddleLeftAuto = 7;
    final int kMiddleRightAuto = 8;
    int autoMode;
    boolean leftSide;
    
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
        drivetrain.resetLeftDistance();
        drivetrain.resetHeading();
        
    }

    public void robotInit() {
        driverStick = new LogitechGamepadController(1);
        operatorStick = new LogitechDualActionController(2);

        compressor = new Compressor(1, 1);
        //compressor.start();
        compressor.stop();

        fl = new Talon(1);
        bl = new Talon(2);
        fr = new Talon(3);
        br = new Talon(4);

        gyro = new Gyro(1);

        leftDriveEncoder = new Encoder(6, 7);
        rightDriveEncoder = new Encoder (4, 5);
        
        shifterRelay = new Relay(8);

        drivetrain = new Drivetrain(fl, bl, fr, br, gyro, leftDriveEncoder, rightDriveEncoder, shifterRelay);

        shooterTalon = new Talon(5);
        beltTalon = new Talon(6);
        collectorTalon = new Talon(7);
        traySolenoid = new DoubleSolenoid(1, 2);
        latchSolenoid = new DoubleSolenoid(3, 4);
        indexerSolenoid = new DoubleSolenoid(5, 6);
        shooterEncoder = new Counter(13);
        shooterEncoder.start();
        
        blockerTalon = new Talon(8);
        blockerUpSwitch = new DigitalInput(9);
        blockerDownSwitch = new DigitalInput(8);

        tray = new Tray(shooterTalon, beltTalon, collectorTalon, traySolenoid, 
            latchSolenoid, indexerSolenoid, shooterEncoder);
        
        blocker = new Blocker(tray, blockerTalon, blockerUpSwitch, blockerDownSwitch);
        
        statusPrinter = new StatusPrinter(tray, drivetrain);

        cycleCounter = 0;

        lifterSolenoid = new DoubleSolenoid(7, 8);
        leftLimit = new DigitalInput(2);
        rightLimit = new DigitalInput(3);

        lifters = new Lifters(lifterSolenoid, leftLimit, rightLimit);

        leftLight = new Relay(2);
        rightLight = new Relay(3);
        
        flipperRelay = new Relay(7);
        flipper = new Flipper(flipperRelay);
    }

    public void autonomousInit() {
        //Lowering tray; setting up for all auto modes
        init();
        drivetrain.heading = 0;
        drivetrain.arcadeDrive(0, 0);
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
            case kTwoDiskNoMoveAuto:
                twoDiskNoMoveAutonomousPeriodic();
                break;
            case kTwoDiskAuto:
                twoDiskAutonomousPeriodic();
                break;
            case kThreeDiskAuto:
                threeDiskAutonomousPeriodic();
                break;
            case kFourDiskAuto:
                fourDiskAutonomousPeriodic();
                break;
            case kCornerLeftAuto:
                //leftSide = true;
                cornerShotLeftAutonomousPeriodic();
                break;
            case kCornerRightAuto:
                //leftSide = false;
                cornerShotRightAutonomousPeriodic();
                break;
            case kMiddleLeftAuto:
                //middleShotLeftAndTurnAutonomousPeriodic();
                break;
            case kMiddleRightAuto:
                //middleShotRightAndTurnAutonomousPeriodic();
                break;
        }
        
        statusPrinter.printStatuses();

        //Updating tray and drivetrain
        tray.update();
        drivetrain.updateHeading();
        blocker.update(0, false);
    }

    private void decideAutonomous() {
        //Setting two disk auto if DIO 2 is on
        if(DriverStation.getInstance().getDigitalIn(1)){
            autoMode = kTwoDiskNoMoveAuto; 
        }else if (DriverStation.getInstance().getDigitalIn(2)) {
            autoMode = kTwoDiskAuto;
            //Setting three disk auto if DIO 3 is on
        } else if (DriverStation.getInstance().getDigitalIn(3)) {
            autoMode = kThreeDiskAuto;
            //Setting four disk auto if DIO 4 is on
        } else if (DriverStation.getInstance().getDigitalIn(4)) {
            autoMode = kFourDiskAuto;
            //Defaulting to no autonomous if no DIO is on
        } else if (DriverStation.getInstance().getDigitalIn(5)) {
            autoMode = kCornerLeftAuto;
        } else if (DriverStation.getInstance().getDigitalIn(6)) {
            autoMode = kCornerRightAuto;
        } else if (DriverStation.getInstance().getDigitalIn(7)) {
            autoMode = kMiddleLeftAuto;
        } else if (DriverStation.getInstance().getDigitalIn(8)) {
            autoMode = kMiddleRightAuto;
        } else {
            autoMode = kTwoDiskNoMoveAuto;
        }
    }
    
    private void twoDiskNoMoveAutonomousPeriodic(){
        if (cycleCounter < 25) {
            //Waiting for tray to come down, etc.
        }else if (cycleCounter < 250) {
            //Shooting first disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.shoot();
        }else if (cycleCounter < 300) {
            tray.notShoot();
        }else if (cycleCounter < 375) {
            tray.shoot();
        }else if (cycleCounter < 500) {
            tray.notShoot();
        }else if (cycleCounter < 550) {
            tray.shoot();
        }else if (cycleCounter < 700) {
            tray.notShoot();
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
        } else if (cycleCounter < 250) {
            //Shooting first disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.shoot();
        } else {
            //Shooting second disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.notShoot();
        }
    }

    private void threeDiskAutonomousPeriodic() {
        /*if (threeDiskAutoState == kThreeDiskAutoReverse) {
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
        }*/
        
        if(cycleCounter < 25){
            //Do nothing
        }else if(cycleCounter < 155){
            drivetrain.safeArcadeDrive(Voltage.voltageToPWM(4), 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
        }else if(cycleCounter < 300){
            if(drivetrain.heading < 42){
                drivetrain.pivot(-.6);
            }else{
                drivetrain.pivot(0);
            }
        }else if(cycleCounter < 350){
            //drivetrain.safeArcadeDrive(Voltage.voltageToPWM(2), 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
        }else if(cycleCounter < 550){
            drivetrain.arcadeDrive(0, 0);
            tray.shoot();
        }else if(cycleCounter < 650) {
            drivetrain.arcadeDrive(0, 0);
            tray.notShoot();
        }else{
            drivetrain.arcadeDrive(0, 0);
        }
    }

    private void fourDiskAutonomousPeriodic() {
        compressor.stop();
        //System.out.println(drivetrain.getRightDistance());
        //Lower tray
        
        if (cycleCounter < 25) {
            //Waiting for tray to lower and turning on collection
            drivetrain.arcadeDrive(0, 0);
            tray.beltOn();
            tray.collectorOn();
            System.out.println(cycleCounter+": Initing");
        //Moving forwards
        } else if (cycleCounter < 30) {
            drivetrain.arcadeDrive(Voltage.voltageToPWM(11*-.6), 0);
            
            System.out.println(cycleCounter+": Moving Forwards");
        } else if (cycleCounter < 145) {
            drivetrain.arcadeDrive(Voltage.voltageToPWM(11*-.22), 0);
            drivetrain.gyro.reset();
            drivetrain.resetRightDistance();
            System.out.println(cycleCounter+": Collecting");
        //Turning
        }else if (cycleCounter < 210){
            drivetrain.arcadeDrive(Voltage.voltageToPWM(8*-.22), 0);
            tray.beltOff();
            tray.collectorOff();
            drivetrain.resetHeading();
        } else if (cycleCounter < (285)) {
            tray.beltOff();
            tray.collectorOff();
            if (drivetrain.heading > -115){
                drivetrain.pivot(Voltage.voltageToPWM(11*.7));
            }else{
                drivetrain.pivot(0);
            }
        }else if (cycleCounter < 330){
            drivetrain.arcadeDrive(Voltage.voltageToPWM(11*-.62), 0);
            tray.beltOff();
            System.out.println(cycleCounter+": Backing Up");
        } else if (cycleCounter < 345){
            //drivetrain.arcadeDrive(0, 0);
            //if (lifters.isOnPyramid()){
                drivetrain.arcadeDrive(Voltage.voltageToPWM(11*-.1),0);
            //}else{
                //drivetrain.arcadeDrive(0, 0);
            //}
            
            System.out.println(cycleCounter+": Backing Up");
        } else if (cycleCounter < 390){
            drivetrain.safeArcadeDrive(Voltage.voltageToPWM(11*-.2), 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            System.out.println(cycleCounter+": Backing Up");
        } else if (cycleCounter < 500) {
            drivetrain.arcadeDrive(0, 0);
            tray.shoot();
            System.out.println(cycleCounter+": Shooting");
        } else if (cycleCounter < 555){
                tray.notShoot();
                System.out.println(cycleCounter+": Not-Shooting");
        }else if (cycleCounter < 615) {
            tray.shoot();
            System.out.println(cycleCounter+": Shooting2");
        } else {
            tray.notShoot(20);
            System.out.println(cycleCounter+": Not-Shooting2");
        }
        
    }
    
    void cornerShotLeftAutonomousPeriodic() {
        if(cycleCounter < 25){
            //Do nothing
        }else if(cycleCounter < 235){
            tray.shoot();
        }else if(cycleCounter < 300){
            tray.notShoot();
        }else if(cycleCounter < 380) {
            drivetrain.arcadeDrive(-.6, 0);
        }else if(cycleCounter < 400) {
            drivetrain.arcadeDrive(-.5, .1);
            drivetrain.resetHeading();
            drivetrain.heading = 0;
        }else if(cycleCounter < 460) {
            tray.beltOff();
            tray.collectorOff();
                if(drivetrain.heading < 40){
                    drivetrain.pivot(-.5);
                }else{
                    drivetrain.pivot(0);
                }    
        }else if(cycleCounter < 540) {
            drivetrain.arcadeDrive(-.2, 0);
            //System.out.println("Turining Belt Off");
            tray.beltOff();
            //System.out.println("Turned Belt off");
            tray.collectorOff();
            drivetrain.resetHeading();
        }else if(cycleCounter < 600) {
            tray.beltOff();
            tray.collectorOff();
                if(drivetrain.heading < 20){
                    drivetrain.pivot(-.5);
                }else{
                    drivetrain.pivot(0);
                }

        }else if(cycleCounter >= 600) {
            drivetrain.arcadeDrive(0,0);
        }
    }
    
    void cornerShotRightAutonomousPeriodic(){
        if(cycleCounter < 25){
            //Do nothing
        }else if(cycleCounter < 235){
            tray.shoot();
        }else if(cycleCounter < 300){
            tray.notShoot();
        }else if(cycleCounter < 380) {
            drivetrain.arcadeDrive(-.5, 0);
        }else if(cycleCounter < 400) {
            drivetrain.arcadeDrive(-.5, 0);
            drivetrain.resetHeading();
            drivetrain.heading = 0;
        }else if(cycleCounter < 460) {
            tray.beltOff();
            tray.collectorOff();
            
                if(drivetrain.heading > -40){
                    drivetrain.pivot(.5);
                }else{
                    drivetrain.pivot(0);
                }
            
        }else if(cycleCounter < 540) {
            drivetrain.arcadeDrive(-.2, 0);
            //System.out.println("Turining Belt Off");
            tray.beltOff();
            //System.out.println("Turned Belt off");
            tray.collectorOff();
            drivetrain.resetHeading();
        }else if(cycleCounter < 600) {
            tray.beltOff();
            tray.collectorOff();
            
                if(drivetrain.heading < -20){
                    drivetrain.pivot(.5);
                }else{
                    drivetrain.pivot(0);
                }
            
        }else if(cycleCounter >= 600) {
            drivetrain.arcadeDrive(0,0);
        }
    }
    
    void middleShotLeftAndTurnAutonomousPeriodic() {
        if (cycleCounter < 25) {
            //Waiting for tray to come down, etc.
        } else if (cycleCounter < 75) {
            //Backing up to pyramid
            if(lifters.isOnPyramid()){
                //System.out.println("On Pyramid");
            }else{
                //System.out.println("Not on Pyramid");
            }
            
            if (!lifters.isOnPyramid()) {
                //System.out.println("Driving Backwards");
                drivetrain.safeArcadeDrive(-.15, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            } else {
                drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            }
        } else if (cycleCounter < 200) {
            //Shooting first disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.shoot();
        } else if (cycleCounter < 250) {
            //Shooting second disk
            drivetrain.safeArcadeDrive(0, 0, lifters.leftOnPyramid(), lifters.rightOnPyramid());
            tray.notShoot();
        } else if (cycleCounter < 320) {
            drivetrain.arcadeDrive(.4, 0);
            drivetrain.resetHeading();
        }else if (cycleCounter < 325) {
            drivetrain.arcadeDrive(0, 0);
        } else if (cycleCounter < 395) {
            if(drivetrain.heading > -90){
                drivetrain.pivot(.2*(390-cycleCounter));
            }else{
                drivetrain.pivot(0);
            }
        }else if (cycleCounter < 460) {
            drivetrain.arcadeDrive(-.6, 0);
            drivetrain.resetHeading();
        }else if (cycleCounter < 505) {
            if(drivetrain.heading < 70){
                drivetrain.pivot(-.45);
            }else{
                drivetrain.pivot(0);
            }
        }else if (cycleCounter < 625) {
            drivetrain.arcadeDrive(-.65, 0);
            drivetrain.resetHeading();
            //System.out.println("Moving");
        }else if (cycleCounter < 705) {
            if(drivetrain.heading < 50){
                drivetrain.pivot(-.3);
            }else{
                drivetrain.pivot(0);
            }
            //System.out.println("Pivoting");
        } else {
            drivetrain.arcadeDrive(0, 0);
            //System.out.println("Stopping");
        }
    }
    
    void middleShotRightAndTurnAutonomousPeriodic() {
        
    }

    public void teleopInit() {
        init();
        compressor.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //System.out.println("E: "+tray.getShooterPWM());
        //System.out.println("S: "+tray.shooterSetRate);
        // Turn the lights on and off
        lights();
        //tray.setShooterVoltage(DriverStation.getInstance().getAnalogIn(1)*12/5);
        driveOI();
        shiftingOI();
        trayRaiseOI();
        collectorOI();
        beltOI();
        liftersOI();
        blockerOI();
        
        allOffOI();
        shooterOnOI();
        limitSwitchesOI();
        shootOI();
        flipperOI();
        
        //Updating tray and drivetrain
        tray.update();
        drivetrain.updateHeading();
        
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
        if (driverStick.getX()) {
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
        if (driverStick.getStart()) {
            lifters.disableSwitch();
        }
        
        //Enabling limit switches 
        if (driverStick.getBack()) {
            lifters.enableSwitch();
        }
    }
    
    private void shiftingOI(){
        if(driverStick.getA() && !driverStick.getB()){
            drivetrain.highGear();
        }else if(driverStick.getB() && !driverStick.getA()){
            drivetrain.lowGear();
        }else if (driverStick.getA() && driverStick.getB()){
            drivetrain.lowGear();
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
    
    private void blockerOI(){
        blocker.update(-operatorStick.getRightY(), operatorStick.getRightPressed());
        /*if(operatorStick.getLeftPressed()){
            blocker.override();
        }
        
        if(operatorStick.getRightPressed()){
            blocker.notOverride();
        }
        
        if (operatorStick.getRightY() < -.8){
            blocker.raise();
        }else if (operatorStick.getRightY() > .8){
            blocker.lower();
        }*/
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
        
        shiftingOI();
        blockerOI();
        
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
    
    private void flipperOI() {
        if(operatorStick.getLeftY() > -.5){
            flipper.extend();
        }else{
            flipper.retract();
        }
    }
    
    public void disabledPeriodic(){
        statusPrinter.printStatuses();
        drivetrain.updateHeading();
    }
}