/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author brennonbrimhall
 */
public class Rollers extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Jaguar frontRollersJag = new Jaguar(RobotMap.frontRollersJagPort);
    Jaguar backRollersJag = new Jaguar(RobotMap.backRollersJagPort);


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void frontRollers(double speed){
        frontRollersJag.set(speed);
    }

    public void backRollers(double speed){
        backRollersJag.set(speed);
    }

    public void bothRollers(double speed){
        frontRollersJag.set(speed);
        backRollersJag.set(speed);
    }

    public void stopRollers(){
        frontRollersJag.set(0);
        backRollersJag.set(0);
    }
}