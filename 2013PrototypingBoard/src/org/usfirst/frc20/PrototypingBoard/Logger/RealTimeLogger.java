/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.PrototypingBoard.Logger;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Date;

/**
 *
 * @author Driver
 */
public class RealTimeLogger extends Logger{
    Date initDate;
    long initTime;
    
    public RealTimeLogger(){
        initDate = new Date();
        initTime = initDate.getTime();
        
    }

    public void logln(String message) {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "]    " + message);
    }

    public void logValue(String value, double number) {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "]    " + value + ":    " + number);
    }

    public void logValue(String value, int number) {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "]    " + value + ":    " + number);
    }

    public void logValue(String value, long number) {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "]    " + value + ":    " + number);
    }

    public void logValue(String value, short number) {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "]    " + value + ":    " + number);
    }

    public void logValue(String value, byte number) {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "]    " + value + ":    " + number);
    }

    public void logImportant(String message) {
        Date date = new Date();
        System.out.println("-----------------------------------------------");
        System.out.println("[" + (date.getTime() - initTime) + "] " + message.toUpperCase());
        System.out.println("-----------------------------------------------");
    }

    public void logAutoInit() {
        Date date = new Date();
        System.out.println("-----------------------------------------------");
        System.out.println("[" + (date.getTime() - initTime) + "] INITIALIZING AUTO.");
        System.out.println("-----------------------------------------------");
        userMessage("Auto Initialized.");
    }

    public void logTeleopInit() {
        Date date = new Date();
        System.out.println("-----------------------------------------------");
        System.out.println("[" + (date.getTime() - initTime) + "] INITIALIZING TELEOP.");
        System.out.println("-----------------------------------------------");
        userMessage("Telop Initialized.");
    }

    public void logAutoPeriodic() {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "] Excecuting AutoPeriodic():");
    }

    public void logTelopPeriodic() {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "] Excecuting TelopPeriodic():");
    }

    public void logTestPeriodic() {
        Date date = new Date();
        System.out.println("[" + (date.getTime() - initTime) + "] Excecuting TestPeriodic():");
    }
    
    public static void userMessage(String message){
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
}
