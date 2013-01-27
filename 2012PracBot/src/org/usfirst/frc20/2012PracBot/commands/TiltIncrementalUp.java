// RobotBuilder Version: 0.0.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc20.2012PracBot.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc20.2012PracBot.Robot;
/**
 *
 */
public class  TiltIncrementalUp extends Command {
    Tilt tilt;
    boolean finished = false;
    public TiltIncrementalUp() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
	
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.tilt);
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }
    // Called just before this Command runs the first time
    protected void initialize() {
        tilt = Robot.tilt;
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        tilt.tiltUp();
    }
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finshed;
    }
    // Called once after isFinished returns true
    protected void end() {
    }
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        finished = true;
    }
}
