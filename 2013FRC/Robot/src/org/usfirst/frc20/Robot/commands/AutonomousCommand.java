/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.Robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc20.Robot.Robot;

/**
 *
 * @author Dan
 */
public class AutonomousCommand extends Command {
    public AutonomousCommand(){
        requires(Robot.collector);
        requires(Robot.drivetrain);
        requires(Robot.indexer);
        requires(Robot.lifter);
        requires(Robot.tray);
        requires(Robot.shooter);
    }
    protected void initialize() {
        
    }

    protected void execute() {
        
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
