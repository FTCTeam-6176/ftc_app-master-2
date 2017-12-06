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
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous(name="Color Sensor By Wyatt for color red left ", group="Templates")
//@Disabled  //Comment this out to add to the opmode list
public class AutonomousColorSensorByWyattredleft extends LinearOpMode {

    // Declare OpMode members.
    // private ElapsedTime runtime = new ElapsedTime();
    public ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        DcMotor left_Drive = hardwareMap.get(DcMotor.class, "left_Drive");
        DcMotor right_Drive = hardwareMap.get(DcMotor.class, "right_Drive");
        CRServo Sensor_arm = hardwareMap.get(CRServo.class, "Sensor_arm");
        DcMotor arm_Drive = hardwareMap.get(DcMotor.class, "arm_Drive");
        ColorSensor colory = hardwareMap.get(ColorSensor.class, "colory");
        CRServo Pin = hardwareMap.get(CRServo.class, "Pin");
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        left_Drive.setDirection(DcMotor.Direction.FORWARD);
        right_Drive.setDirection(DcMotor.Direction.REVERSE);

        // get a reference to the color sensor

        // hsvValues is an array that will hold the hue, saturation, and value information
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attenuate the measured values.
        final double SCALE_FACTOR = 255;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()&&runtime.seconds() <= 6.5)
        {

            // Extend turn on LED Jewel Arm
            Sensor_arm.setPower(1.0);
            sleep(6000);
            Sensor_arm.setPower(0);
            // convert the RGB values to HSV values
            // multiply by the SCALE_FACTOR
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int)(colory.red() * SCALE_FACTOR),
                    (int)(colory.green() * SCALE_FACTOR),
                    (int)(colory.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function
            telemetry.addData("Alpha", colory.alpha());
            telemetry.addData("Red", colory.red());
            telemetry.addData("Green", colory.green());
            telemetry.addData("blue", colory.blue());
            telemetry.addData("Hue", hsvValues[0]);
            telemetry.update();

            if (colory.red() > colory.blue())
            {
                sleep(5000);
                left_Drive.setPower(-.25);
                right_Drive.setPower(.25);
                sleep(500);
                left_Drive.setPower(0);
                right_Drive.setPower(0);
                sleep(500);
                Sensor_arm.setPower(-1.0);
                sleep(6000);
                Sensor_arm.setPower(0);
                left_Drive.setPower(-.25);
                right_Drive.setPower(.25);
                sleep(1000);
                left_Drive.setPower(1);
                right_Drive.setPower(1);
                sleep(500);
            }
            else
            {
                sleep(5000);
                left_Drive.setPower(.25);
                right_Drive.setPower(-.25);
                sleep(500);
                left_Drive.setPower(0);
                right_Drive.setPower(0);
                sleep(500);
                Sensor_arm.setPower(-1.0);
                sleep(6000);
                Sensor_arm.setPower(0);
                left_Drive.setPower(-.5);
                right_Drive.setPower(.5);
                sleep(1000);
                left_Drive.setPower(1);
                right_Drive.setPower(1);
                sleep(500);
            }
        }
    }
}
