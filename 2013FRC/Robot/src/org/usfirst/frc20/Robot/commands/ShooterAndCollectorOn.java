/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.Robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Driver
 */
public class ShooterAndCollectorOn extends CommandGroup {
    
    public ShooterAndCollectorOn() {
        addParallel(new ShooterOn());
        addParallel(new CollectorOn());
    }
}
