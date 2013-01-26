package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStation;
/**
 * @author Scott
 */
public class Print {
    
    private static DriverStationLCD.Line[] a = {DriverStationLCD.Line.kUser2, 
            DriverStationLCD.Line.kUser3, DriverStationLCD.Line.kUser4, 
            DriverStationLCD.Line.kUser5, DriverStationLCD.Line.kUser6,
            DriverStationLCD.Line.kMain6};

    
    public static void println(String info, int line){
        if (line > 6 || line < 1)
            System.out.println("Wrong line Stupid");
        else
            DriverStationLCD.getInstance().println(a[line - 1], 1, info);
    }
    
    public static void update(){
        DriverStationLCD.getInstance().updateLCD();
    }
}