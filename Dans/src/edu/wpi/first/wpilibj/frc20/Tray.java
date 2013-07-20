/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.frc20;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
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
    double beltShootSpeed = -.7;//Was -.7
    double shooterSetRate = 12820.5;
    final int kIndexerCyclesAfterTrayMoved = 110;
    final int kIndexerCyclesBeforeTrayMoved = 30;
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
    TrayLifter cylinder;
    Latch latch;
    Indexer indexer;

    //Constructor for entire tray; calls other constructors
    Tray(Talon shooterTalon, Talon beltTalon, Talon collectorTalon,
         DoubleSolenoid traySolenoid, DoubleSolenoid latchSolenoid,
         DoubleSolenoid indexerSolenoid, Counter shooterEncoder) {

        flywheel = new Flywheel(shooterTalon, shooterEncoder);
        belt = new Belt(beltTalon);
        collector = new Collector(collectorTalon);

        cylinder = new TrayLifter(traySolenoid);
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
     * Sets tray up in test mode. Turns everything off, locks the stopper/latch,
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
        /*System.out.print("State ");
        if(blocker.isDown()){
            System.out.println("Down");
        }else if(blocker.isUp()){
            System.out.println("Up");
        }else if(blocker.isMovingDown()){
            System.out.println("Moving Down");
        }else if(blocker.isMovingUp()){
            System.out.println("Moving Up");
        }*/
        flywheel.update();
        if (isTrayMoving) {
            numCycles += 1;
            if (numCycles < kIndexerCyclesBeforeTrayMoved) {
                /*
                 * if (trayDirection == kTrayUp) { indexer.push(); } else {
                 * numCycles = kIndexerCyclesBeforeTrayMoved;
                 *
                 * }
                 */
                indexer.push();
            } else if (numCycles < kIndexerCyclesAfterTrayMoved) {
                if (trayDirection == kTrayUp) {
                    cylinder.raise();
                    isTrayMoving = false;
                } else if (trayDirection == kTrayDown) {
                    cylinder.lower();
                }
            } else {
                indexer.pull();
                isTrayMoving = false;
            }
        }
        if (trayDirection == kTrayUp) {
            setShooterSpeedTrayUp();
        } else {
            setShooterSpeedTrayDown();
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
    
    boolean isUp() {
        return trayDirection == kTrayUp;
    }
    
    boolean isDown() {
        return trayDirection == kTrayDown;
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
        //System.out.println("Belt Off");
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
            belt.setShootOn();
            collector.setOn();
        }
        if (flywheel.atSpeed()) {
            
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
    void notShoot(int cyclesToWait) {
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
        if (numCycles == cyclesToWait) {
            finishShooting();
        }
        flywheel.resetDiscsShot();
    }

    void notShoot() {
        notShoot(50);
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

    double flywheelEncoderDistance() {
        return flywheel.getEncoderDistance();
    }

    void setShooterSpeedTrayUp() {
        flywheel.setTrayUpSpeed();
    }

    void setShooterRate(double newRate) {
        shooterSetRate = newRate;
    }

    void setShooterRate() {
        shooterSetRate = 0;
    }

    void setShooterSpeedTrayDown() {
        flywheel.setTrayDownSpeed();
    }

    double getShooterPWM() {
        return flywheel.getPWM();
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

        void setShootOn() {
            beltTalon.set(beltShootSpeed);
        }

        void setOff() {
            beltTalon.set(0);
        }

        void setReverse() {
            beltTalon.set(-kBeltSpeed);
        }

        boolean getOn() {
            return (beltTalon.get() == kBeltSpeed);
        }

        boolean getOff() {
            return (beltTalon.get() == 0);
        }

        boolean getReverse() {
            return (beltTalon.get() == -kBeltSpeed);
        }
    }

    //Private class to represent flywheel
    private class Flywheel {

        final int kFlywheelLowSpeedThreshold = 800;
        final int kFlywheelLowCyclesBeforeShot = 5;
        Talon flywheelTalon;
        Counter encoder;
        int numCyclesBelow;
        int discsShot;
        boolean on = true;
        boolean driverOff = false;
        boolean lowBattery = false;
        boolean wasWorking = true;
        boolean isDown = true;
        final int kNumDataPoints = 10;
        double[] dataPoints = new double[kNumDataPoints];
        int dataPointIndex = 0;

        Flywheel(Talon flywheelTalon, Counter encoder) {
            this.flywheelTalon = flywheelTalon;
            this.encoder = encoder;
            for (int i = 0; i < kNumDataPoints; i++) {
                dataPoints[i] = i;
            }
        }

        void update() {
            encoderWatchdog();

            if (driverOff) {
                flywheelTalon.set(0);
                return;
            }
            System.out.println("E: " + getEncoderRate());
            //For now, use PWM based on battery voltage.
            double pwm = -Voltage.voltageToPWM(shooterVoltage);

            if (pwm > 1.0) {
                pwm = 1.0;
                lowBattery = true;
            } else if (pwm < -1.0) {
                pwm = -1.0;
                lowBattery = true;
            }


            //Verifying that we've been below for at least 5 cycles before we say we shot the disc
            if (/*
                     * speedSensor2.get()
                     */getEncoderRate() < kFlywheelLowSpeedThreshold) {
                numCyclesBelow++;
            } else {
                numCyclesBelow = 0;
            }

            if (numCyclesBelow == kFlywheelLowCyclesBeforeShot) {
                discsShot++;
            }

            //

            //Bang-bang controller to use later.
            if (isEncoderWorking()) {
                if (getEncoderRate() > shooterSetRate) {
                    flywheelTalon.set(0);
                    return;
                }
                if (!isDown) {
                    flywheelTalon.set(-.85);
                } else {
                    flywheelTalon.set(-1);
                }
            } else {
                beltShootSpeed = -.65;
                flywheelTalon.set(pwm);
            }



        }

        void resetDiscsShot() {
            discsShot = 0;
        }

        boolean atSpeed() {
            return true;
            //return encoder.getRate() > kShooterSetSpeed;
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
            return 1 / encoder.getPeriod();
        }

        double getEncoderDistance() {
            return encoder.get();
        }

        void setTrayDownSpeed() {
            isDown = true;
            shooterVoltage = 8.4;
            shooterSetRate = 12820.5 * .95;
            //shooterSetRate = 9090;
        }

        void setTrayUpSpeed() {
            isDown = false;
            shooterVoltage = 5.3;
            shooterSetRate = (((8196.7 - 1000) * .95) * .945);
            //shooterSetRate = 5319;
        }

        public double getPWM() {
            return flywheelTalon.get();
        }

        boolean isEncoderWorking() {
            for (int i = 1; i < kNumDataPoints; i++) {
                if (dataPoints[i] != dataPoints[0]) {
                    return true;
                }
            }
            System.out.println("ENCODER NOT WORKING!");
            return false;
        }

        void encoderWatchdog() {
            dataPoints[dataPointIndex] = getEncoderDistance();
            dataPointIndex = (dataPointIndex + 1) % kNumDataPoints;
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
