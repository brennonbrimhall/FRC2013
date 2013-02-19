/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author freshplum
 */
public class XboxController {
    
    Joystick stick;
    
    XboxController(int pos) {
        stick = new Joystick(pos);
    }
    
    boolean getA() {
        return stick.getRawButton(1);
    }
    
    boolean getB() {
        return stick.getRawButton(2);
    }
    
    boolean getX() {
        return stick.getRawButton(3);
    }
    
    boolean getY() {
        return stick.getRawButton(4);
    }
    
    boolean getLeftBumper() {
        return stick.getRawButton(5);
    }
    
    boolean getRightBumper() {
        return stick.getRawButton(6);
    }
    
    boolean getBack() {
        return stick.getRawButton(7);
    }
    
    boolean getStart() {
        return stick.getRawButton(8);
    }
    
    double getLeftX() {
        return stick.getRawAxis(1);
    }
    
    double getLeftY() {
        return stick.getRawAxis(2);
    }
    
    double getRighttX() {
        return stick.getRawAxis(4);
    }
    
    double getRightY() {
        return stick.getRawAxis(5);
    }
    
    double getAnalogTriggers() {
        return stick.getRawAxis(3);
    }
}
