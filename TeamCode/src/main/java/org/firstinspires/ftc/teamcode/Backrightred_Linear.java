package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Backrightred_Linear", group="Pushbot")
//@Disabled
public class Backrightred_Linear extends LinearOpMode {


    private ElapsedTime     runtime = new ElapsedTime();
    public DcMotor  left_Drive   = null;
    public DcMotor  right_Drive  = null;
    public DcMotor  arm_Drive     = null;
    public CRServo Pin    = null;
    private CRServo Sensorarm = null;
    public ColorSensor colory = null;
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder

    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = COUNTS_PER_MOTOR_REV/
                                                      (WHEEL_DIAMETER_INCHES * 3.14159);

    @Override
    public void runOpMode() {
        left_Drive  = hardwareMap.get(DcMotor.class, "left_Drive");
        right_Drive = hardwareMap.get(DcMotor.class, "right_Drive");
        arm_Drive = hardwareMap.get(DcMotor.class, "arm_Drive");
        Sensorarm = hardwareMap.get(CRServo.class, "sensor_arm");
        colory = hardwareMap.get(ColorSensor.class,"colorsensor");
        Pin = hardwareMap.get(CRServo.class, "Pin");
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        left_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          left_Drive.getCurrentPosition(),
                          right_Drive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)

        encoderDrive(0.5,   37, 37, 9.0);
        encoderDrive(0.5, 12, -12, 4.0);
        encoderDrive(0.5, 22, 22, 7.0);
        arm(1.0, 3000);
        encoderDrive(0.5, -1.5, -1.5, 3.0);



        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = left_Drive.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = right_Drive.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            left_Drive.setTargetPosition(newLeftTarget);
            right_Drive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            left_Drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_Drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            left_Drive.setPower(Math.abs(speed));
            right_Drive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (left_Drive.isBusy() && right_Drive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        left_Drive.getCurrentPosition(),
                        right_Drive.getCurrentPosition());
                telemetry.update();


            }

            // Stop all motion;
            left_Drive.setPower(0);
            right_Drive.setPower(0);

            // Turn off RUN_TO_POSITION
            left_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
        public void arm (double speed,long time){
            Pin.setPower(speed);
            sleep(time);
            Pin.setPower(0.0);
        }
    }








