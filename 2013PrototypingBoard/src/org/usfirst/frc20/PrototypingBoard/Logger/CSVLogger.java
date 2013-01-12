/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.PrototypingBoard.Logger;

import edu.wpi.first.wpilibj.DriverStationLCD;
import java.util.Date;

/**
 *
 * @author Driver
 */
public class CSVLogger extends Logger {

    Date initDate;
    long initTime;
    char lineSeperator;

    public CSVLogger(char newline) {
        initDate = new Date();
        initTime = initDate.getTime();
        lineSeperator = newline;
    }

    public void logln(String message) {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + "," + message + lineSeperator);
    }

    public void logValue(String value, double number) {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + "," + value + "," + number + lineSeperator);
    }

    public void logValue(String value, int number) {
        Date date = new Date();
       System.out.print((date.getTime() - initTime) + "," + value + "," + number + lineSeperator);
    }

    public void logValue(String value, long number) {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + "," + value + "," + number + lineSeperator);
    }

    public void logValue(String value, short number) {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + "," + value + "," + number + lineSeperator);
    }

    public void logValue(String value, byte number) {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + "," + value + "," + number + lineSeperator);
    }

    public void logImportant(String message) {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + "," + message.toUpperCase() + lineSeperator);
    }

    public void logAutoInit() {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + ",INITIALIZING AUTO" + lineSeperator);
        userMessage("Auto Initialized.");
    }

    public void logTeleopInit() {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + ",INITIALIZING TELOP" + lineSeperator);
        userMessage("Telop Initialized.");
    }

    public void logAutoPeriodic() {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + ",Excecuting AutoPeriodic" + lineSeperator);
    }

    public void logTelopPeriodic() {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + ",Excecuting TelopPeriodic" + lineSeperator);
    }

    public void logTestPeriodic() {
        Date date = new Date();
        System.out.print((date.getTime() - initTime) + ",Excecuting TestPeriodic" + lineSeperator);
    }

    public static void userMessage(String message) {
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, "                     ");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, message);
        DriverStationLCD.getInstance().updateLCD();
    }
}
