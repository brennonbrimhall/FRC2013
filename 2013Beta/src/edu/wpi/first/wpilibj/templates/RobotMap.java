package edu.wpi.first.wpilibj.templates;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    public static final int frontLeftJagPort = 1;
    public static final int frontRightJagPort = 2;
    public static final int backLeftJagPort = 3;
    public static final int backRightJagPort = 4;
    
    public static final int frontRollersJagPort = 5;
    public static final int backRollersJagPort = 6;
    
    public static final int throatJagPort = 7;
    
    public static final int topShooterJagPort = 8;
    public static final int bottomShooterJagPort = 9;
    
    public static final int shooterTopEncoderAChannel = 1;
    public static final int shooterTopEncoderBChannel = 1;
    public static final int shooterBottomEncoderAChannel = 2;
    public static final int shooterBottomEncoderBChannel = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
}
