/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author freshplum
 */
public class Tray {

    //Constants
    final int kTrayUp = 1;
    final int kTrayNeutral = 0;
    final int kTrayDown = -1;
    final double kCollectorSpeed = -1.0;
    final double kBeltSpeed = -1.0;
    final double kShooterSetSpeed = 1000;
    final double kShooterVoltage = 8.4;
    public final int kIndexerCyclesAfterTrayMoved = 300;
    public final int kIndexerCyclesBeforeTrayMoved = 100;
    public final int kNumCyclesAfterShooting = 50;
    //Variables for Tray logic
    boolean isShooting;
    boolean isTrayMoving;
    boolean isLastDiscFired;
    int numCycles;
    int trayDirection;
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

        belt.setOn();
        collector.setOn();
        lifter.raise();
        latch.lock();
        indexer.push();
        isShooting = false;
        isTrayMoving = false;
        trayDirection = kTrayDown;
        numCycles = 0;




    }

    void update() {
        flywheel.update();
        if (isTrayMoving) {
            numCycles += 1;
            if (numCycles < kIndexerCyclesBeforeTrayMoved) {
                indexer.push();
            } else if (numCycles < kIndexerCyclesAfterTrayMoved) {
                if (trayDirection == kTrayUp) {
                    lifter.raise();
                } else if (trayDirection == kTrayDown) {
                    lifter.lower();
                } else if (trayDirection == kTrayNeutral) {
                    lifter.release();
                }
            } else {
                indexer.pull();
                isTrayMoving = false;
            }
        }
    }

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

    void lower() {
        if (isShooting) {
            return;
        }
        if (trayDirection == kTrayUp) {
            trayDirection = kTrayDown;
            isTrayMoving = true;
            //lifter.lower();
            numCycles = 0;
        } else if (trayDirection == kTrayNeutral) {
            trayDirection = kTrayDown;
            isTrayMoving = true;
            lifter.lower();
        }
    }

    void release() {
        if (isShooting) {
            return;
        }
        if (trayDirection == kTrayUp) {
            trayDirection = kTrayNeutral;
            isTrayMoving = true;
            //lifter.release();
            numCycles = 0;
        } else if (trayDirection == kTrayDown) {
            trayDirection = kTrayNeutral;
            isTrayMoving = true;
            lifter.release();
        }
    }

    void collectorOn() {
        if (!isShooting) {
            collector.setOn();
        }
    }

    void collectorOff() {
        if (!isShooting) {
            collector.setOff();
        }
    }

    void collectorReverse() {
        if (!isShooting) {
            collector.setReverse();
        }
    }

    void beltOn() {
        if (!isShooting) {
            belt.setOn();
        }
    }

    void beltOff() {
        if (!isShooting) {
            belt.setOff();
        }
    }
    
    void beltReverse() {
        if (!isShooting) {
            belt.setReverse();
        }
    }

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
            belt.setOn();
            collector.setOn();
        } else {
            belt.setOff();
            collector.setOff();
        }
        if (flywheel.discsShot == 3) {
            notShoot();
        }
    }

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

    private void finishShooting() {
        indexer.pull();
        latch.lock();
        isShooting = false;
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

        void setOff() {
            beltTalon.set(0);
        }

        void setReverse() {
            beltTalon.set(-kBeltSpeed);
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
        //DigitalInput speedSensor;
        //DigitalInput speedSensor2;

        Flywheel(Talon flywheelTalon, Encoder encoder) {
            this.flywheelTalon = flywheelTalon;
            this.encoder = encoder;
            //speedSensor = new DigitalInput(6);
            //speedSensor2 = new DigitalInput(7);
        }

        void update() {
            System.out.println(encoder.getRate());
            //For now, use PWM based on battery voltage.
            double pwm = -kShooterVoltage / DriverStation.getInstance().getBatteryVoltage();
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, Double.toString(pwm));
            if (pwm > 1.0) {
                pwm = 1.0;
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Low battery!");
            } else if (pwm < -1.0) {
                pwm = -1.0;
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, "Low battery!");
            }
            
            DriverStationLCD.getInstance().updateLCD();

            //Verifying that we've been below for at least 5 cycles before we say we shot the disc
            if (/*speedSensor2.get()*/ encoder.getRate()<kFlywheelLowSpeedThreshold) {
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
            return true; /*speedSensor.get();*/
            //return encoder.getRate() > shooterSetSpeed;
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
    }
}
