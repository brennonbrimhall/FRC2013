/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Joystick;

/**
<<<<<<< HEAD
 *
 * @author freshplum
=======
 * Describes a Logitech Dual Action Controller.  This type of controller is used
 * for operator control, and it simplifies and abstracts the raw joystick.
 * @author Brennon Brimhall
>>>>>>> origin/master
 */
public class LogitechDualActionController {
    
    Joystick stick;
    
    LogitechDualActionController(int pos) {
        stick = new Joystick(pos);
    }
    
    boolean getButton(int buttonNum) {
        return stick.getRawButton(buttonNum);
    }
    
    boolean dPadLeft() {
        return stick.getRawAxis(5) < -.5;
    }
    
    boolean dPadRight() {
        return stick.getRawAxis(5) > .5;
    }
    
    boolean dPadDown() {
        return stick.getRawAxis(6) > .5;
    }
    
    boolean dPadUp() {
        return stick.getRawAxis(6) < -.5;
    }
    
    double getLeftX() {
        return stick.getRawAxis(1);
    }
    
    double getLeftY() {
        return stick.getRawAxis(2);
    }
    
    double getRighttX() {
        return stick.getRawAxis(3);
    }
    
    double getRightY() {
        return stick.getRawAxis(4);
    }
}

