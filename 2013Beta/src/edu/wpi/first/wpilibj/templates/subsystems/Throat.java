/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.ThroatStop;

/**
 *
 * @author brennonbrimhall
 */
public class Throat extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    private static final double throatForwardSpeed = .75;
    private static final double throatBackwardSpeed = -.75;
    Jaguar throatJag = new Jaguar(RobotMap.throatJagPort);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ThroatStop());
    }
    
    public void setSpeed(double speed){
        throatJag.set(speed);
    }
    
    public void driveForward(){
        throatJag.set(throatForwardSpeed);
    }
    
    public void driveBackward(){
        throatJag.set(throatBackwardSpeed);
    }
    
    public void stop(){
        throatJag.set(0.0);
    }
}