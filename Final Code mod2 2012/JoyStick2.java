/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author joe
 */
public class JoyStick2 {
    
    Joystick joy;
    
    JoyStick2(int address) {
        joy = new Joystick(address);
    }
    
    public double getAxis(int axis) {
        return joy.getRawAxis(axis);
    }
    
    public boolean getButton(int button) {
        return joy.getRawButton(button);
    }
    
    public double getThrottle(){
        return joy.getThrottle();
    }
    
}
