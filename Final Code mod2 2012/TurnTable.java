/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author joe
 */
public class TurnTable {
    
    CANJaguar2 turnJag;
    
    double rearPos = 0;
    double arcDist = 8.55;
    
    double degreesPerTurn = 210/arcDist;
    
    boolean overriden = false;
    boolean zeroed = false;
    
    double position = 0;
    
//    double trim = 0;
    
    TurnTable(int turnTableAddress) {
        turnJag = new CANJaguar2(turnTableAddress);
        // Competition
//        turnJag.setPositionMode(360, -550, -.04, 0);
        
        // Practice
        turnJag.setVoltageMode();
//        turnJag.setPositionMode(360, -550, -.2, 0);
    }
    
    public void setVoltage(double percentage) {
        if(!overriden) {
            turnJag.set(percentage);
//            System.out.println("Setting percentage to " + percentage);
        }
    }
    
    public boolean atPosition() {
        if(!overriden) return true;
        
        return Math.abs((position) - turnJag.getPosition()) < .2 && zeroed;
    }
    
    public double getX() {
        return turnJag.getX();
    }
    
    public void override() {
        if(!overriden) {
            turnJag.set(0);
            turnJag.setPositionMode(360, -550, -.2, 0);
            turnJag.enableControl();
            overriden = true;
        }
    }
    
    public void unOverride() {
        if(overriden) {
            turnJag.setVoltageMode();
            overriden = false;
        }
    }
    
    public void update() {
        
        
        if( !zeroed && overriden) {
            turnJag.set(1000);
        } else if(overriden) {
            position = 105 / degreesPerTurn + rearPos;
            turnJag.set(position /*+ trim*/);
        }
        
//        System.out.println(turnJag.getPosition());
//        System.out.println(rearPos);
        
        if(clockwiseLimitHit()) {
//            System.out.println("Forward Hit TurnTable");
            rearPos = turnJag.getPosition();
            zeroed = true;
//            System.out.println("CLOCKWISE");
        }
        if(counterClockwiseLimitHit()) {
//            System.out.println("Rear Hit TurnTable " + rearPos);
            rearPos = turnJag.getPosition() - arcDist;
            zeroed = true;
//            System.out.println("COUNTERCLOCKWISE");
        }
//        System.out.println(turnJag.getPosition());
    }
    
    public double getPosition() {
        return turnJag.getPosition();
    }
    
    public boolean clockwiseLimitHit() {
        return turnJag.frontLimitHit();
    }
    
    public boolean counterClockwiseLimitHit() {
        return turnJag.rearLimitHit();
    }
    
//    public void setTrim(double newTrim) {
//        trim = newTrim;
//    }
    
}
