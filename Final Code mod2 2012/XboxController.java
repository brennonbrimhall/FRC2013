package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

public class XboxController {
    
    Joystick joy1;
    
    public XboxController(){
        joy1 = new Joystick(1);
    }
    
    public XboxController(int port){
        joy1 = new Joystick(port);
    }
    
    public double leftJoyUpDown(){
        return joy1.getRawAxis(2);
    }
    
    public double leftJoyLeftRight(){
        return joy1.getRawAxis(1);
    }
    
    public double triggers(){
        return joy1.getRawAxis(3);
    }
    
    public double rightJoyUpDown(){
        return joy1.getRawAxis(5);
    }
    
    public double rightJoyLeftRight(){
        return joy1.getRawAxis(4);
    }
    
    public double dPadLeftRight(){
        return joy1.getRawAxis(6);
    }
    
    public boolean aButton(){
        return joy1.getRawButton(1);
    }
    
    public boolean bButton(){
        return joy1.getRawButton(2);
    }
    
    public boolean xButton(){
        return joy1.getRawButton(3);
    }
    
    public boolean yButton(){
        return joy1.getRawButton(4);
    }
    
    public boolean leftBumper(){
        return joy1.getRawButton(5);
    }
    
    public boolean rightBumper(){
        return joy1.getRawButton(6);
    }
    
    public boolean back(){
        return joy1.getRawButton(7);
    }
    
    public boolean start(){
        return joy1.getRawButton(8);
    }
}
