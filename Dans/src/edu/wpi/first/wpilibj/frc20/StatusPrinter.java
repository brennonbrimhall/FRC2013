/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * This class periodically prints different values to the Driver Station
 * User Message box.
 * @author Brennon Brimhall
 */
public class StatusPrinter {

    Tray tray;
    Drivetrain drivetrain;

    StatusPrinter(Tray t, Drivetrain d) {
        tray = t;
        drivetrain = d;
    }

    public void printStatuses() {
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, 1, "Heading: "+drivetrain.getHeading());

        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Flywheel: " + tray.flywheelEncoderRate());

        if (tray.isBeltOn()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Belt: On     ");
        } else if (tray.isBeltReverse()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Belt: Reverse");
        } else if (tray.isBeltOff()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Belt: Off    ");
        } else {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, "Belt: ?      ");
        }
        
        if(tray.isCollectorOn()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Collector: On     ");
        }else if(tray.isCollectorReverse()){
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Collector: Reverse");
        }else{
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, 1, "Collector: Off    ");
        }
        
        //if(tray.isIndexerOut()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "Left:" + drivetrain.getLeftDistance());
        
        /* }else if(tray.isIndexerIn()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "Indexer: In ");
        }else{
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "Indexer: ?  ");
        }*/
        
        //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, 1, "Right Encoder: "+drivetrain.getRightDistance());
        
        //if(tray.isLatchOut()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "Right: "+drivetrain.getRightDistance());
        /*}else if(tray.isLatchIn()) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "Stopper: In ");
        }else {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "Stopper:   ?");
        }*/

        //DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "Left Encoder: "+drivetrain.getLeftDistance());
        
        DriverStationLCD.getInstance().updateLCD();
    }
}
