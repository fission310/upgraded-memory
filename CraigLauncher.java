package org.firstinspires.ftc.teamcode;/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="CraigLauncher Teleop", group="PushBot")  // @Autonomous(...) is the other common choice
public class CraigLauncher extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        DcMotor leftFront = hardwareMap.dcMotor.get("leftFront");
        DcMotor leftBack = hardwareMap.dcMotor.get("leftBack");
        DcMotor rightFront = hardwareMap.dcMotor.get("rightFront");
        DcMotor rightBack = hardwareMap.dcMotor.get("rightBack");

        DcMotor slides = hardwareMap.dcMotor.get("slides");

        /*
        Servo hopper = hardwareMap.servo.get("hopper");

        Servo capBallRight = hardwareMap.servo.get("capBallRight");
        Servo capBallLeft = hardwareMap.servo.get("capBallLeft");

        Servo rotate1 = hardwareMap.servo.get("rotate1");
        Servo rotate2 = hardwareMap.servo.get("rotate2");
        */


        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        leftFront.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive() && runtime.seconds() < 120.0) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            /*
            Joystick Map

            DRIVER
            Joysticks - drivetrain
            Linear slides down (hold)- left trigger
            Linear slides up (hold) - right trigger
            Hopper servo (hold) - left bumper
            Cap ball servo - right bumper
            Cap ball down - B
            Cap ball up - START

            OPERATOR
            same except linear slides controlled by joysticks

             */

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            leftFront.setPower(-gamepad1.left_stick_y);
            leftBack.setPower(-gamepad1.left_stick_y);
            rightFront.setPower(-gamepad1.right_stick_y);
            rightBack.setPower(-gamepad1.right_stick_y);


            // linear slides down
            if (gamepad1.left_bumper) {
                slides.setPower(-1);
            } else if (gamepad1.right_bumper) {
                slides.setPower(1);
            } else {
                slides.setPower(0);
            }

            /*
            // hopper servo
            if (gamepad1.left_bumper || gamepad2.left_bumper) {
                hopper.setPosition(1);
            } else {
                hopper.setPosition(0.5);
            }

            // cap ball servo
            if (gamepad1.right_bumper || gamepad2.right_bumper) {
                capBallLeft.setPosition(0);
                capBallRight.setPosition(1);
            } else {
                capBallLeft.setPosition(0.5);
                capBallRight.setPosition(0.5);
            }

            // cap ball rotate
            if (gamepad1.b || gamepad2.b) {
                double pos1 = rotate1.getPosition();
                double pos2 = rotate2.getPosition();
                if (pos1 > 0 && pos2 < 1) {
                    rotate1.setPosition(rotate1.getPosition() - 0.02);
                    rotate2.setPosition(rotate2.getPosition() + 0.02);
                }
            } else if (gamepad1.start || gamepad2.start) {
                double pos1 = rotate1.getPosition();
                double pos2 = rotate2.getPosition();
                if (pos2 > 0 && pos1 < 1) {
                    rotate1.setPosition(rotate1.getPosition() + 0.02);
                    rotate2.setPosition(rotate2.getPosition() - 0.02);
                }
            }
            */

        }
    }
}