/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.ShooterStop;

/**
 *
 * @author brennonbrimhall
 */
public class Shooter extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    Jaguar topJag = new Jaguar(RobotMap.topShooterJagPort);
    Jaguar bottomJag = new Jaguar(RobotMap.bottomShooterJagPort);
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ShooterStop());
    }
    
    public void topJag(double speed){
        topJag.set(speed);
    }
    
    public void bottomJag(double speed){
        bottomJag.set(speed);
    }
    
    public void setSpeeds(double topJagSpeed, double bottomJagSpeed){
        topJag.set(topJagSpeed);
        bottomJag.set(bottomJagSpeed);
    }
}