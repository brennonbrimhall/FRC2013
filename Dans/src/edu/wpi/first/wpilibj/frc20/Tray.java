/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author freshplum
 */
public class Tray {

    //Constants
    final int kTrayUp = 1;
    final int kTrayDown = -1;
    final double kCollectorSpeed = -1.0;
    final double kBeltSpeed = -1.0;
    final double kBeltShootSpeed = -.5;
    final double kShooterSetSpeed = 1000;
    final int kIndexerCyclesAfterTrayMoved = 300;
    final int kIndexerCyclesBeforeTrayMoved = 100;
    final int kNumCyclesAfterShooting = 50;
    
    //Variables for Tray logic
    boolean isShooting;
    boolean isTrayMoving;
    boolean isLastDiscFired;
    int numCycles;
    int trayDirection;
    double shooterVoltage = 8.4;
    
    //Variables to hold private classes
    Flywheel flywheel;
    Belt belt;
    Collector collector;
    TrayLifter lifter;
    Latch latch;
    Indexer indexer;

    //Constructor for entire tray; calls other constructors
    Tray(Talon shooterTalon, Talon beltTalon, Talon collectorTalon,
            DoubleSolenoid traySolenoid, DoubleSolenoid latchSolenoid,
            DoubleSolenoid indexerSolenoid, Encoder shooterEncoder) {

        flywheel = new Flywheel(shooterTalon, shooterEncoder);
        belt = new Belt(beltTalon);
        collector = new Collector(collectorTalon);

        lifter = new TrayLifter(traySolenoid);
        latch = new Latch(latchSolenoid);
        indexer = new Indexer(indexerSolenoid);


    }

    void reset() {
        numCycles = 0;
        isTrayMoving = false;
        isShooting = false;
        flywheel.setOn();
        latch.lock();
        belt.setOff();
        collector.setOn();
        trayDirection = kTrayUp;
        indexer.push();
        lower();
    }
    
    /**
     * Sets tray up in test mode.  Turns everything off, locks the stopper/latch,
     * and then raises the tray.
     */
    void test() {
        allOff();
        latch.lock();
        raise();
    }
    
    /**
     * Sets the values of motors and actuators based on global values
     */
    void update() {
        flywheel.update();
        if (isTrayMoving) {
            numCycles += 1;
            if (numCycles < kIndexerCyclesBeforeTrayMoved) {
                if (trayDirection == kTrayUp) {
                    indexer.push();
                } else {
                    numCycles = kIndexerCyclesBeforeTrayMoved;

                }
            } else if (numCycles < kIndexerCyclesAfterTrayMoved) {
                if (trayDirection == kTrayUp) {
                    lifter.raise();
                    isTrayMoving = false;
                } else if (trayDirection == kTrayDown) {
                    lifter.lower();
                }
            } else {
                indexer.pull();
                isTrayMoving = false;
            }
        }
    }
    
    /**
     * Raises the tray.
     */
    void raise() {
        if (isShooting) {
            return;
        }
        if (trayDirection != kTrayUp) {
            isTrayMoving = true;
            trayDirection = kTrayUp;
            //lifter.raise();
            numCycles = 0;
        }
    }

    /**
     * Lowers the tray.
     */
    void lower() {
        if (isShooting) {
            return;
        }
        if (trayDirection == kTrayUp) {
            trayDirection = kTrayDown;
            isTrayMoving = true;
            numCycles = 0;
        }
    }

    /**
     * Turns the collector on.
     */
    void collectorOn() {
        if (!isShooting) {
            collector.setOn();
        }
    }
    
    /**
     * Turns the collector off.
     */
    void collectorOff() {
        if (!isShooting) {
            collector.setOff();
        }
    }
    
    /**
     * Turns collector in reverse to backdrive disks in case of jamming.
     */
    void collectorReverse() {
        if (!isShooting) {
            collector.setReverse();
        }
    }

    /**
     * Turns belt on.
     */
    void beltOn() {
        if (!isShooting) {
            belt.setOn();
        }
    }
    
    /**
     * Turns belt off.
     */
    void beltOff() {
        if (!isShooting) {
            belt.setOff();
        }
    }
    
    /**
     * Turns belt in reverse to backdrive disks in case of jamming.
     */
    void beltReverse() {
        if (!isShooting) {
            belt.setReverse();
        }
    }
    
    /**
     * Turns everything on the tray off.
     */
    void allOff() {
        belt.setOff();
        collector.setOff();
        flywheel.setOff();
    }

    /**
     * Turns the flywheel on.
     */
    void shooterOn() {
        flywheel.setOn();
    }

    /**
     * Shoots the first three frisbees.
     */
    void shoot() {
        if (isTrayMoving) {
            return;
        }
        if (!isShooting) {
            latch.release();
            indexer.pull();
            isLastDiscFired = false;
            isShooting = true;
        }
        if (flywheel.atSpeed()) {
            belt.setShootOn();
            collector.setOn();
        } else {
            belt.setOff();
            collector.setOff();
        }
        if (flywheel.discsShot == 3) {
            notShoot();
            belt.setOn();
        }
    }

    /**
     * Fires last frisbee with the indexer.
     */
    void notShoot() {
        if (isTrayMoving) {
            return;
        }
        if (isLastDiscFired) {
            numCycles++;
        }
        if (!isShooting) {
            return;
        }
        belt.setOn();
        if (flywheel.atSpeed()) {
            indexer.push();
            if (!isLastDiscFired) {
                numCycles = 0;
            }
            isLastDiscFired = true;
        }
        if (numCycles == 50) {
            finishShooting();
        }
        flywheel.resetDiscsShot();
    }

    /**
     * Callback to clean up after shooting.
     */
    private void finishShooting() {
        indexer.pull();
        latch.lock();
        isShooting = false;
    }
    
    boolean isCollectorOn() {
        return collector.isOn();
    }
    
    boolean isCollectorReverse() {
        return collector.isReverse();
    }
    
    boolean isBeltOn() {
        return belt.getOn();
    }
    
    boolean isBeltOff() {
        return belt.getOff();
    }
    
    boolean isBeltReverse() {
        return belt.getReverse();
    }
    
    boolean isFlywheelLowBattery() {
        return flywheel.lowBattery;
    }
    
    boolean isLatchOut() {
        return latch.isLocked();
    }
    
    boolean isLatchIn() {
        return latch.isReleased();
    }
    
    boolean isIndexerIn() {
        return indexer.isPushing();
    }
    
    boolean isIndexerOut() {
        return indexer.isPulling();
    }
    
    double flywheelEncoderRate() {
        return flywheel.getEncoderRate();
    }
    
    void setShooterVoltage(double newVoltage){
        flywheel.setVoltage(newVoltage);
    }
    
    void setShooterVoltage(){
        flywheel.setVoltage();
    }

    //Private class to represent Collector
    private class Collector {

        Talon collectorTalon;

        Collector(Talon collectorTalon) {
            this.collectorTalon = collectorTalon;
        }

        void setOn() {
            collectorTalon.set(kCollectorSpeed);
        }

        void setOff() {
            collectorTalon.set(0);
        }

        void setReverse() {
            collectorTalon.set(-kCollectorSpeed);
        }
        
        boolean isOn() {
            return (collectorTalon.get() == kCollectorSpeed);
        }
        
        boolean isReverse() {
            return (collectorTalon.get() == -kCollectorSpeed);
        }
    }

    //Private class to represent belt
    private class Belt {

        Talon beltTalon;

        Belt(Talon beltTalon) {
            this.beltTalon = beltTalon;
        }

        void setOn() {
            beltTalon.set(kBeltSpeed);
        }
        
        void setShootOn(){
            beltTalon.set(kBeltShootSpeed);
        }

        void setOff() {
            beltTalon.set(0);
        }

        void setReverse() {
            beltTalon.set(-kBeltSpeed);
        }
        
        boolean getOn() {
            return (beltTalon.get()==kBeltSpeed);
        }
        
        boolean getOff() {
            return (beltTalon.get()==0);
        }
        
        boolean getReverse() {
            return (beltTalon.get()==-kBeltSpeed);
        }
    }

    //Private class to represent flywheel
    private class Flywheel {

        final int kFlywheelLowSpeedThreshold = 800;
        final int kFlywheelLowCyclesBeforeShot = 5;
        Talon flywheelTalon;
        Encoder encoder;
        int numCyclesBelow;
        int discsShot;
        boolean on = true;
        boolean driverOff = false;
        boolean lowBattery = false;

        Flywheel(Talon flywheelTalon, Encoder encoder) {
            this.flywheelTalon = flywheelTalon;
            this.encoder = encoder;
        }

        void update() {
            if (driverOff) {
                flywheelTalon.set(0);
                return;
            }
            System.out.println(encoder.getRate());
            //For now, use PWM based on battery voltage.
            double pwm = -shooterVoltage / DriverStation.getInstance().getBatteryVoltage();
            
            if (pwm > 1.0) {
                pwm = 1.0;
                lowBattery = true;
            } else if (pwm < -1.0) {
                pwm = -1.0;
                lowBattery = true;
            }


            //Verifying that we've been below for at least 5 cycles before we say we shot the disc
            if (/*speedSensor2.get()*/encoder.getRate() < kFlywheelLowSpeedThreshold) {
                numCyclesBelow++;
            } else {
                numCyclesBelow = 0;
            }

            if (numCyclesBelow == kFlywheelLowCyclesBeforeShot) {
                discsShot++;
            }

            flywheelTalon.set(pwm);

            //Bang-bang controller to use later.
            /*
             if (!on) {
             flywheelTalon.set(0);
             return;
             }
             if (encoder.getRate() > shooterSetSpeed) {
             flywheelTalon.set(0);
             return;
             }
             flywheelTalon.set(1);*/
        }

        void resetDiscsShot() {
            discsShot = 0;
        }

        boolean atSpeed() {
            return true;
            //return encoder.getRate() > shooterSetSpeed;
        }

        void setOn() {
            driverOff = false;
        }

        void setOff() {
            driverOff = true;
        }
        
        boolean lowBattery() {
            return lowBattery;
        }
        
        double getEncoderRate() {
            return encoder.getRate();
        }
        
        void setVoltage() {
            shooterVoltage = 8.4;
        }
        
        void setVoltage(double newVoltage) {
            shooterVoltage = newVoltage;
        }
    }

    //Private class to represent TrayLifter
    private class TrayLifter {

        DoubleSolenoid sol;

        TrayLifter(DoubleSolenoid sol) {
            this.sol = sol;
        }

        void raise() {
            sol.set(DoubleSolenoid.Value.kForward);
        }

        void lower() {
            sol.set(DoubleSolenoid.Value.kReverse);
        }

        void release() {
            sol.set(DoubleSolenoid.Value.kOff);
        }
        
        boolean isDown() {
            return (sol.get() == DoubleSolenoid.Value.kReverse);
        }
        
        boolean isUp() {
            return (sol.get() == DoubleSolenoid.Value.kForward);
        }
        
        boolean isReleased() {
            return (sol.get() == DoubleSolenoid.Value.kOff);
        }
    }

    //Private class to represent vertical "stopper" cylinder
    private class Latch {

        DoubleSolenoid sol;

        Latch(DoubleSolenoid sol) {
            this.sol = sol;
        }

        void lock() {
            sol.set(DoubleSolenoid.Value.kForward);
        }

        void release() {
            sol.set(DoubleSolenoid.Value.kReverse);
        }
        
        boolean isLocked() {
            return (sol.get() == DoubleSolenoid.Value.kForward);
        }
        
        boolean isReleased() {
            return (sol.get() == DoubleSolenoid.Value.kReverse);
        }
    }

    //Private class to represent horizontal/wooden indexer cylinder
    private class Indexer {

        DoubleSolenoid sol;

        Indexer(DoubleSolenoid sol) {
            this.sol = sol;
        }

        void push() {
            sol.set(DoubleSolenoid.Value.kForward);
        }

        void pull() {
            sol.set(DoubleSolenoid.Value.kReverse);

        }
        
        boolean isPushing() {
            return (sol.get() == DoubleSolenoid.Value.kForward);
        }
        
        boolean isPulling() {
            return (sol.get() == DoubleSolenoid.Value.kReverse);
        }
    }
}
