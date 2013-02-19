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
public class ShooterAndCollectorReverse extends CommandGroup {
    
    public ShooterAndCollectorReverse() {
        addParallel(new ShooterCollect());
        addParallel(new CollectorReverse());
    }
}
