/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author Driver
 */
public class setJAG {
    public setJAG() {
        
    }
 public CANJaguar speedJAG(CANJaguar canJaguar,int port,double P,double I, double D, int rev, int typeEncoder){
      try {
               canJaguar = new CANJaguar(port,CANJaguar.ControlMode.kSpeed);
               if (typeEncoder == 1) {
                canJaguar.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
               }
               else canJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
               canJaguar.setPID(P, I, D);
               canJaguar.configEncoderCodesPerRev(rev);
               canJaguar.enableControl(0.0);
        }
            catch ( CANNotInitializedException canE1) {
                System.out.print(canE1.toString());
                }
            catch  (CANTimeoutException canE2) {
                System.out.print(canE2.toString());
            }   
     return canJaguar;
 }
 // typeEncoder = 1 kEncoder
 // typeEncoder = 2 kEncoder
 
 public CANJaguar positionJAG(CANJaguar canJaguar,int port,double P,double I, double D, int rev, int typeEncoder){
      try {
               canJaguar = new CANJaguar(port,CANJaguar.ControlMode.kSpeed);
               if (typeEncoder == 1) {
                canJaguar.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
               }
            else canJaguar.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
               
               canJaguar.setPID(P, I, D);
               canJaguar.configEncoderCodesPerRev(rev);
               canJaguar.enableControl(0.0);
        }
            catch ( CANNotInitializedException canE1) {
                System.out.print(canE1.toString());
                }
            catch  (CANTimeoutException canE2) {
                System.out.print(canE2.toString());
            }   
     return canJaguar;
 }
 public CANJaguar voltageJAG(CANJaguar canJaguar,int port){
         try {
               canJaguar = new CANJaguar(port,CANJaguar.ControlMode.kVoltage);
               canJaguar.enableControl(0.0);
     }
            catch ( CANNotInitializedException canE1) {
                System.out.print(canE1.toString());
                }
            catch  (Exception canE2) {
                System.out.print(canE2.toString());
            }   
         return canJaguar;
     }
 
 public void setX(double outputValue, CANJaguar jag){
     try {
         jag.setX(outputValue);
     } catch (CANTimeoutException e) {
         System.out.println(e.toString() + "\n"
                 + "Have a Nice Day!");
     }
 }
}   
    

