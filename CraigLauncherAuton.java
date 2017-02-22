package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.ElapsedTime;


public abstract class CraigLauncherAuton extends LinearOpMode {

    /* -------------- CONSTANTS ---------------- */

    public static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // AndyMark 40s
    public static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                        (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double     DRIVE_SPEED             = 0.35;
    public static final double     TURN_SPEED              = 0.4;
    public static final double     CLOSE_SPEED             = 0.6;

    // For detecting color
    public static final double THRESHOLD = 2;

    /* ----------------------------------------- */


    /* ---------- HARDWARE MAP ------------ */
    protected DcMotor leftFront;
    protected DcMotor leftBack;
    protected DcMotor rightFront;
    protected DcMotor rightBack;

    protected DcMotor shooter;

    protected ColorSensor sensorRGB;
    protected DeviceInterfaceModule cdim;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    public static final int LED_CHANNEL = 5;

    /* ------------------------------------- */

    // Instance vars
    protected ElapsedTime runtime = new ElapsedTime();


    /* ----------- METHODS ------------ */
    @Override
    public abstract void runOpMode() throws InterruptedException;

    public abstract void runRoutine(long delay);


    protected void setup() {
        leftFront = hardwareMap.dcMotor.get("leftFront");
        leftBack = hardwareMap.dcMotor.get("leftBack");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        rightBack = hardwareMap.dcMotor.get("rightBack");

        // shooter = hardwareMap.dcMotor.get("shooter");

        leftFront.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                leftFront.getCurrentPosition(),
                rightFront.getCurrentPosition(),
                leftBack.getCurrentPosition(),
                rightBack.getCurrentPosition());
        telemetry.update();
    }

    protected void setupColor() {
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        sensorRGB = hardwareMap.colorSensor.get("sensor_color");

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, false);
    }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    protected void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = leftFront.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightFrontTarget = rightFront.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLeftBackTarget = leftBack.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightBackTarget = rightBack.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftFront.setTargetPosition(newLeftFrontTarget);
            rightFront.setTargetPosition(newRightFrontTarget);
            leftBack.setTargetPosition(newLeftBackTarget);
            rightBack.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftFront.setPower(Math.abs(speed));
            rightFront.setPower(Math.abs(speed));
            leftBack.setPower(Math.abs(speed));
            rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftFront.isBusy() && rightFront.isBusy() && leftBack.isBusy() && rightBack.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftFrontTarget,  newRightFrontTarget,
                        newLeftBackTarget, newRightBackTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        leftFront.getCurrentPosition(),
                        rightFront.getCurrentPosition(),
                        leftBack.getCurrentPosition(),
                        rightBack.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftBack.setPower(0);
            rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }

    protected void startShoot() {
        shooter.setPower(1);
    }

    protected void stopShoot() {
        shooter.setPower(0);
    }

    protected String beaconDetect() {
        runtime.reset();
        int[] red = new int[2];
        int[] blue = new int[2];

        while (opModeIsActive() && (runtime.seconds() < 2.0))  {

            // convert the RGB values to HSV values.
            // Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);

            // send the info back to driver station using telemetry function.
            // telemetry.addData("Clear", sensorRGB.alpha());
            red[0] = sensorRGB.red();
            blue[0] = sensorRGB.blue();
        }

        red[1] = sensorRGB.red();
        blue[1] = sensorRGB.blue();

        double blueAvg = (blue[0] + blue[1]) / 2;;
        double redAvg = (red[0] + red[1]) / 2;

        telemetry.addData("Blue", blueAvg);
        telemetry.addData("Red", redAvg);

        if (blueAvg * THRESHOLD < redAvg) {
            return "red";
        } else if (redAvg * THRESHOLD < blueAvg) {
            return "blue";
        } else {
            return "null";
        }
    }

}
