/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Driver
 */
public class Blocker {

    //Constants to track state
    static final int isUp = 2;
    static final int isMovingUp = 1;
    static final int isMovingDown = -1;
    static final int isDown = -2;
    //Timeouts
    static final int ticksDown = 50;
    //State variables
    //private int state;
    private int ticker;
    //Members
    private Talon blocker;
    private Tray tray;

    public Blocker(Tray tray, Talon blocker, DigitalInput upperLimitSwitch, DigitalInput lowerLimitSwitch) {
        this.blocker = blocker;
        this.tray = tray;
        //this.upperLimitSwitch = upperLimitSwitch;
        //this.lowerLimitSwitch = lowerLimitSwitch;
    }

    public void update(double speed, boolean override) {
        if(override){
            blocker.set(speed);
        }else {
            blocker.set(0);
        }
    }
}
