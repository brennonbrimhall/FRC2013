/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.PrototypingBoard.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc20.PrototypingBoard.Robot;

/**
 *
 * @author Driver
 */
public class IncrementVictor extends Command {
    
    public IncrementVictor() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.board.incrementVictorBy5Percent();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
