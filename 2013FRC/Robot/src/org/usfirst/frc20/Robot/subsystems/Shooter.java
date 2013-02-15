// RobotBuilder Version: 0.0.2
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc20.Robot.subsystems;
import org.usfirst.frc20.Robot.RobotMap;
import org.usfirst.frc20.Robot.commands.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType; import edu.wpi.first.wpilibj.Encoder.PIDSourceParameter;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc20.Robot.Logger.Logger;
/**
 *
 */
public class Shooter extends Subsystem {
    
    private final double kVoltageOn = 7.8;
    private final double kVoltageOff = 0.0;
    private final double kVoltageCollect = -1.2;
    private final double kRateOn = 2000;
    private final double kRateOff = 0;
    private final double kRateCollect = -100;
    
    Encoder encoder = RobotMap.shooterencoder;
    SpeedController flywheel = RobotMap.shooterflywheel;
    DoubleSolenoid indexerCylinder = RobotMap.shooterindexerCylinder;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ShooterOff());
    }
    
    public void indexerOut(){
        indexerCylinder.set(DoubleSolenoid.Value.kForward);
    }
    
    public void indexerIn(){
        indexerCylinder.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void voltageShooterOn(){
        setShooterVoltage(kVoltageOn);
    }
    
    public void voltageShooterOff(){
        setShooterVoltage(kVoltageOff);
    }
    
    public void voltageShooterCollect(){
        setShooterVoltage(kVoltageCollect);
    }
    
    private void bangBangShooter(double speed){
        //See Ether's whitepaper here: 
        //http://www.chiefdelphi.com/media/papers/2663
        
        if(encoder.getRate()>speed){
            flywheel.set(0);
        }else{
            flywheel.set(1);
        }
    }
    
    public void bangBangShooterOn(){
        bangBangShooter(kRateOn);
    }
    
    public void bangBangShooterOff(){
        bangBangShooter(kRateOff);
    }
    
    public void bangBangShooterCollect(){
        bangBangShooter(kRateCollect);
    }
    
    private void setShooterVoltage(double voltage){
        //Dividing the desired voltage by the battery voltage for speed
        //controller output.  Checking if it is more than 1.0; logging on 
        //DriverStation if it is.
        
        double pwm = voltage/DriverStation.getInstance().getBatteryVoltage();
        
        if(pwm > 1.0){
            pwm = 1.0;
            Logger.userMessageLine1("Battery Low!");
            Logger.userMessageLine3("Insufficient battery");
            Logger.userMessageLine4("voltage to run");
            Logger.userMessageLine5("requested flywheel");
            Logger.userMessageLine6("speed!");
        }else if(pwm < -1.0){
            pwm = -1.0;
            Logger.userMessageLine1("Battery Low!");
            Logger.userMessageLine3("Insufficient battery");
            Logger.userMessageLine4("voltage to run");
            Logger.userMessageLine5("requested flywheel");
            Logger.userMessageLine6("speed!");
        }
        
        flywheel.set(pwm);
    }
}