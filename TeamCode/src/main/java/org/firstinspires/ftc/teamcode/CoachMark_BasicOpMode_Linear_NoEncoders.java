/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

// **Added imports

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Color Sensor Test", group="No Encoder")
//@Disabled  //Comment this out to add to the opmode list
public class CoachMark_BasicOpMode_Linear_NoEncoders extends LinearOpMode {

    // Use Pushbot's Hardware
    Hardwaremap robot = new Hardwaremap();

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor left_Drive = null;
    private DcMotor right_Drive = null;

    //** Added declarations
    private ColorSensor colory;
    private DistanceSensor sensorDistance;
    private CRServo Sensor_arm = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        left_Drive  = hardwareMap.get(DcMotor.class, "left_Drive");
        right_Drive = hardwareMap.get(DcMotor.class, "right_Drive");
        Sensor_arm  = hardwareMap.get(CRServo.class, "Sensor_arm");

        // Initialize drive train system variables
        // the init() method of the hardware class does all the work here
        robot.init(hardwareMap);

        // get a reference to the color sensor
        colory = hardwareMap.get(ColorSensor.class, "colory");

        // hsvValues is an array that will hold the hue, saturation, and value information
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array
        // final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attenuate the measured values.
        final double SCALE_FACTOR = 255;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Extend turn on LED Jewel Arm
            colory.enableLed(true);  // Turn on LED
            Sensor_arm.setPower(1.0);
            sleep(10000);
            Sensor_arm.setPower(0);
            colory.enableLed(false);
            Sensor_arm.setPower(-1.0);
            sleep(9000);
            Sensor_arm.setPower(0);

            // convert the RGB values to HSV values
            // multiply by the SCALE_FACTOR
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int)(colory.red() * SCALE_FACTOR),
                    (int)(colory.green() * SCALE_FACTOR),
                    (int)(colory.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function
            telemetry.addData("Distance (cm)", String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Alpha", colory.alpha());
            telemetry.addData("Red", colory.red());
            telemetry.addData("Green", colory.green());
            telemetry.addData("blue", colory.blue());
            telemetry.addData("Hue", hsvValues[0]);

        }
    }
}
