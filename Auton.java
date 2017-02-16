package org.firstinspires.ftc.teamcode;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Auton {

    public static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // AndyMark 40s
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                        (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double     DRIVE_SPEED             = 0.35;
    public static final double     TURN_SPEED              = 0.4;
    public static final double     CLOSE_SPEED             = 0.6;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    public static final int LED_CHANNEL = 5;

    public static final double THRESHOLD = 1.3;

    public static double average(ArrayList<Integer> arr) {
        try {
            double sum = 0;
            for (int i : arr) {
                sum += i;
            }
            return sum / arr.size();
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
