/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.Robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc20.Robot.Robot;

/**
 *
 * @author Driver
 */
public class IndexerKickerIn extends Command {
    
    public IndexerKickerIn() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        
        requires(Robot.indexer);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.indexer.kickerIn();
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
