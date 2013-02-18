/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.Robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc20.Robot.RobotMap;

/**
 *
 * @author Driver
 */
public class Indexer extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    DoubleSolenoid stopperCylinder = RobotMap.indexerstopperCylinder;
    DoubleSolenoid kickerCylinder = RobotMap.indexerkickerCylinder;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void stopperOut(){
        stopperCylinder.set(DoubleSolenoid.Value.kForward);
    }
    
    public void stopperIn(){
        stopperCylinder.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void stopperOff(){
        stopperCylinder.set(DoubleSolenoid.Value.kOff);
    }
    
    public void kickerOut(){
        kickerCylinder.set(DoubleSolenoid.Value.kForward);
    }
    
    public void kickerIn(){
        kickerCylinder.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void kickerOff(){
        kickerCylinder.set(DoubleSolenoid.Value.kOff);
    }
}