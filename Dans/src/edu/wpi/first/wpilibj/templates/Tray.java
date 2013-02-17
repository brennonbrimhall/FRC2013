/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author freshplum
 */
public class Tray {

    private class Collector {

        Talon collectorTalon;
        double collectorSpeed = 1.0;

        Collector(Talon collectorTalon) {
            this.collectorTalon = collectorTalon;
        }

        void setOn() {
            collectorTalon.set(collectorSpeed);
        }

        void setOff() {
            collectorTalon.set(0);
        }

        void setReverse() {
            collectorTalon.set(-collectorSpeed);
        }
    }

    private class Belt {

        Talon beltTalon;
        final double beltSpeed = 1.0;

        Belt(Talon beltTalon) {
            this.beltTalon = beltTalon;
        }

        void setOn() {
            beltTalon.set(beltSpeed);
        }

        void setOff() {
            beltTalon.set(0);
        }

        void setReverse() {
            beltTalon.set(-beltSpeed);
        }
    }

    private class Flywheel {

        Talon flywheelTalon;
        Encoder encoder;
        final double shooterSetSpeed = 1000;
        boolean on = true;

        Flywheel(Talon flywheelTalon, Encoder encoder) {
            this.flywheelTalon = flywheelTalon;
            this.encoder = encoder;
        }

        void update() {
            if (!on) {
                flywheelTalon.set(0);
                return;
            }
            if (encoder.getRate() > shooterSetSpeed) {
                flywheelTalon.set(0);
                return;
            }
            flywheelTalon.set(1);
        }
        
        boolean atSpeed() {
            return encoder.getRate() > shooterSetSpeed;
        }
    }

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

    boolean isShooting;
    Flywheel flywheel;
    Belt belt;
    Collector collector;
    TrayLifter lifter;
    Latch latch;
    Indexer indexer;

    Tray(Talon shooterTalon, Talon beltTalon, Talon collectorTalon,
            DoubleSolenoid traySolenoid, DoubleSolenoid latchSolenoid,
            DoubleSolenoid indexerSolenoid, Encoder shooterEncoder) {

        flywheel = new Flywheel(shooterTalon, shooterEncoder);
        belt = new Belt(beltTalon);
        collector = new Collector(collectorTalon);

        lifter = new TrayLifter(traySolenoid);
        latch = new Latch(latchSolenoid);
        indexer = new Indexer(indexerSolenoid);
        
        isShooting = false;
    }

    void raise() {
        lifter.raise();
    }

    void lower() {
        lifter.lower();
    }

    void release() {
        lifter.release();
    }
    
    void collectorOn() {
        if(!isShooting) {
            collector.setOn();
        }
    }
    
    void collectorOff() {
        if(!isShooting) {
            collector.setOff();
        }
    }
    
    void beltOn() {
        if(!isShooting) {
            belt.setOn();
        }
    }
    
    void beltOff() {
        if(!isShooting) {
            belt.setOff();
        }
    }
    
    void startShooting() {
        if(!isShooting) {
            latch.release();
            indexer.pull();
        }
        isShooting = true;
        
        if(flywheel.atSpeed()) {
            belt.setOn();
            collector.setOn();
        } else {
            belt.setOff();
            collector.setOff();
        }
    }
    
    void stopShooting() {
        if(!isShooting) {
            return;
        }
        indexer.push();
        isShooting = false;
    }
    
    void resetIndexer() {
        indexer.pull();
        latch.lock();
    }
}
