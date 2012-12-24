// RobotBuilder Version: 0.0.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc20.Robot.subsystems;
import org.usfirst.frc20.Robot.RobotMap;
import org.usfirst.frc20.Robot.commands.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 *
 */
public class Drivetrain extends Subsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    Jaguar rightForwardJag = RobotMap.drivetrainrightForwardJag;
    Jaguar rightBackwardJag = RobotMap.drivetrainrightBackwardJag;
    Jaguar leftBackwardJag = RobotMap.drivetrainleftBackwardJag;
    Jaguar leftForwardJag = RobotMap.drivetrainleftForwardJag;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    RobotDrive drive = new RobotDrive(rightForwardJag, rightBackwardJag, leftBackwardJag, leftForwardJag);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new DrivetrainArcadeDrive());
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void tankDrive(Joystick leftStick, Joystick rightStick){
        drive.tankDrive(leftStick, rightStick);
    }
    
    public void arcadeDrive(Joystick stick){
        drive.arcadeDrive(stick);
    }
    
    public void drive(double outputMagnitude, double curve){
        drive.drive(outputMagnitude, curve);
    }
    
    public void stop(){
        drive.drive(0, 0);
    }
}
