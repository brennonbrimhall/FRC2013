/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.Robot.Logger;

import edu.wpi.first.wpilibj.DriverStationLCD;


public class Logger {
    
    public static void userMessageLine1(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public static void userMessageLine2(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public static void userMessageLine3(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public static void userMessageLine4(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public static void userMessageLine5(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
    
    public static void userMessageLine6(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
}
