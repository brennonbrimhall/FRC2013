/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 *
 * @author Driver
 */
public class Voltage {
    static double voltageToPWM(double voltage) {
        return voltage / DriverStation.getInstance().getBatteryVoltage();
    }
    
    static double PWMToVoltage(double pwm) {
        return pwm * DriverStation.getInstance().getBatteryVoltage();
    }
}
