/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.usfirst.frc20.PrototypingBoard.Logger;

/**
 *
 * @author Driver
 */
public abstract class Logger {

    public void logln(String message) {
    }

    public void logValue(String value, double number) {
    }

    public void logValue(String value, int number) {
    }

    public void logValue(String value, long number) {
    }

    public void logValue(String value, short number) {
    }

    public void logValue(String value, byte number) {
    }

    public void logImportant(String message) {
    }

    public void logAutoInit() {
    }

    public void logTeleopInit() {
    }

    public void logAutoPeriodic() {
    }

    public void logTelopPeriodic() {
    }

    public void logTestPeriodic() {
    }
    
    public static void userMessage(String message){
    }
}
