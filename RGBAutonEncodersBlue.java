/*
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
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mod e to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="CraigLauncher: Both Beacons and Cap Ball Park BLUE", group="CraigLauncher")
public class RGBAutonEncodersBlue extends CraigLauncherAuton {

    /* Declare OpMode members. */
    private String teamColor = "blue";

    @Override
    public void runOpMode() {

        setup();
        setupColor();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        runRoutine(0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void runRoutine(long delay) {
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        // (-,+) is turn left; (+,-) is turn right
        // Max time values are commented for each robot operation (not including delayO

        sleep(delay);

        encoderDrive(CraigLauncherAuton.DRIVE_SPEED,  25, 25, 5.0);  // 3
        encoderDrive(CraigLauncherAuton.TURN_SPEED,   7, -7, 3.0);  // 4
        encoderDrive(CraigLauncherAuton.DRIVE_SPEED,  23, 23, 3.0);
        encoderDrive(CraigLauncherAuton.TURN_SPEED,   10, -10, 3.0);  // 4
        encoderDrive(CraigLauncherAuton.DRIVE_SPEED, 25, 25, 5.0);  // 6
        String color = beaconDetect();  // 8
        if (!color.equals(teamColor) && !color.equals("null")) {  // 13
            sleep(5000);
            encoderDrive(DRIVE_SPEED, -3, -3, 3.0);
            encoderDrive(DRIVE_SPEED, 4, 4, 3.0);
        }
        encoderDrive(DRIVE_SPEED, -10, -10, 3.0); // 17

        // ADD CHECK FOR TIME TO ENSURE WRONG BUTTON IS NOT PRESSED
        encoderDrive(CraigLauncherAuton.TURN_SPEED, -17, 17, 3.0);  // 18
        encoderDrive(CraigLauncherAuton.DRIVE_SPEED,  46,  46, 5.0);  // 21
        encoderDrive(CraigLauncherAuton.TURN_SPEED,   17, -17, 3.0);  // 22
        encoderDrive(CraigLauncherAuton.DRIVE_SPEED, 15, 15, 3.0);  // 23
        color = beaconDetect();  // 24
        if (!color.equals(teamColor) && !color.equals("null")) {  // 29
            sleep(5000);
            encoderDrive(DRIVE_SPEED, -3, -3, 3.0);
            encoderDrive(DRIVE_SPEED, 4, 4, 3.0);
        }
        encoderDrive(DRIVE_SPEED, -10, -10, 3.0);  // 30

        encoderDrive(CraigLauncherAuton.TURN_SPEED,   25, -25, 3.0);
        encoderDrive(CraigLauncherAuton.DRIVE_SPEED, 60, 60, 5.0);
    }

}
