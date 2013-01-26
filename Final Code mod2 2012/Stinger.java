/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author Driver
 */
public class Stinger {
    
    Servo servo;
    
    double closedVal = 0;
    double openVal = 1;
    
    Stinger(int servoPort) {
        servo = new Servo(servoPort);
    }
    
    public void open() {
//        System.out.println("OPEN");
        servo.set(closedVal);
    }
    
    public void close() {
//        System.out.println("CLOSE");
        servo.set(openVal);
    }
    
}
