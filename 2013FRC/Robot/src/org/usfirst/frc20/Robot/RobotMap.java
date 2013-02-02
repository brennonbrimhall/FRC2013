// RobotBuilder Version: 0.0.2
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc20.Robot;
    
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType; import edu.wpi.first.wpilibj.Encoder.PIDSourceParameter;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import java.util.Vector;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static SpeedController drivetrainleftFront;
    public static SpeedController drivetrainleftBack;
    public static SpeedController drivetrainrightFront;
    public static SpeedController drivetrainrightBack;
    public static RobotDrive drivetrainRobotDrive;
    public static Gyro drivetraingyro;
    public static Encoder drivetrainleftEncoder;
    public static Encoder drivetrainrightEncoder;
    public static DoubleSolenoid trayanglerCylinder;
    public static SpeedController collectorbeltMotor;
    public static SpeedController collectorrollerMotor;
    public static DoubleSolenoid lifterrightLifterCylinder;
    public static DoubleSolenoid lifterleftLifterCylinder;
    public static DigitalInput lifterleftLimitSwitch;
    public static DigitalInput lifterrightLimitSwitch;
    public static Encoder shooterencoder;
    public static SpeedController shooterflywheel;
    public static DoubleSolenoid shooterindexerCylinder;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        drivetrainleftFront = new Talon(1, 1);
	LiveWindow.addActuator("Drivetrain", "leftFront", (Talon) drivetrainleftFront);
        
        drivetrainleftBack = new Victor(1, 2);
	LiveWindow.addActuator("Drivetrain", "leftBack", (Victor) drivetrainleftBack);
        
        drivetrainrightFront = new Victor(1, 3);
	LiveWindow.addActuator("Drivetrain", "rightFront", (Victor) drivetrainrightFront);
        
        drivetrainrightBack = new Victor(1, 4);
	LiveWindow.addActuator("Drivetrain", "rightBack", (Victor) drivetrainrightBack);
        
        drivetrainRobotDrive = new RobotDrive(drivetrainleftFront, drivetrainleftBack,
              drivetrainrightFront, drivetrainrightBack);
	
        drivetrainRobotDrive.setSafetyEnabled(true);
        drivetrainRobotDrive.setExpiration(0.1);
        drivetrainRobotDrive.setSensitivity(0.5);
        drivetrainRobotDrive.setMaxOutput(1.0);
        drivetraingyro = new Gyro(1, 1);
	LiveWindow.addSensor("Drivetrain", "gyro", drivetraingyro);
        drivetraingyro.setSensitivity(1.25);
        drivetrainleftEncoder = new Encoder(1, 3, 1, 4, false, EncodingType.k4X);
	LiveWindow.addSensor("Drivetrain", "leftEncoder", drivetrainleftEncoder);
        drivetrainleftEncoder.setDistancePerPulse(1.0);
        drivetrainleftEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);
        drivetrainleftEncoder.start();
        drivetrainrightEncoder = new Encoder(1, 5, 1, 6, false, EncodingType.k4X);
	LiveWindow.addSensor("Drivetrain", "rightEncoder", drivetrainrightEncoder);
        drivetrainrightEncoder.setDistancePerPulse(1.0);
        drivetrainrightEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);
        drivetrainrightEncoder.start();
        trayanglerCylinder = new DoubleSolenoid(1, 7, 8);      
	
        
        collectorbeltMotor = new Talon(1, 6);
	LiveWindow.addActuator("Collector", "beltMotor", (Talon) collectorbeltMotor);
        
        collectorrollerMotor = new Talon(1, 7);
	LiveWindow.addActuator("Collector", "rollerMotor", (Talon) collectorrollerMotor);
        
        lifterrightLifterCylinder = new DoubleSolenoid(1, 3, 4);      
	
        
        lifterleftLifterCylinder = new DoubleSolenoid(1, 5, 6);      
	
        
        lifterleftLimitSwitch = new DigitalInput(1, 7);
	LiveWindow.addSensor("Lifter", "leftLimitSwitch", lifterleftLimitSwitch);
        
        lifterrightLimitSwitch = new DigitalInput(1, 8);
	LiveWindow.addSensor("Lifter", "rightLimitSwitch", lifterrightLimitSwitch);
        
        shooterencoder = new Encoder(1, 1, 1, 2, false, EncodingType.k4X);
	LiveWindow.addSensor("Shooter", "encoder", shooterencoder);
        shooterencoder.setDistancePerPulse(1.0);
        shooterencoder.setPIDSourceParameter(PIDSourceParameter.kRate);
        shooterencoder.start();
        shooterflywheel = new Talon(1, 5);
	LiveWindow.addActuator("Shooter", "flywheel", (Talon) shooterflywheel);
        
        shooterindexerCylinder = new DoubleSolenoid(1, 1, 2);      
	
        
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}