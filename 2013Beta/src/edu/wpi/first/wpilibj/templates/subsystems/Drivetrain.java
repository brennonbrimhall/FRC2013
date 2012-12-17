/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author brennonbrimhall
 */
public class Drivetrain extends Subsystem {
    Jaguar frontLeftJag = new Jaguar(RobotMap.frontLeftJagPort);
    Jaguar frontRightJag = new Jaguar(RobotMap.frontRightJagPort);
    Jaguar backLeftJag = new Jaguar(RobotMap.backLeftJagPort);
    Jaguar backRightJag = new Jaguar(RobotMap.backRightJagPort);
    
    RobotDrive drive = new RobotDrive(frontLeftJag, backLeftJag, frontRightJag, backRightJag);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    
    public void drive(double outputMagnitude, double curve){
        drive.drive(outputMagnitude, curve);
    }
    
    public void arcadeDrive(Joystick stick){
        drive.arcadeDrive(stick);
    }
    
    public void tankDrive(Joystick leftStick, Joystick rightStick){
        drive.tankDrive(leftStick, rightStick);
    }
    
    public void stop(){
        drive.drive(0, 0);
    }
}