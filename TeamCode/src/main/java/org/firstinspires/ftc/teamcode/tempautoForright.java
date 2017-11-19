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
 *  The code is written using a method called: encoderDrive(spe ed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Pushbot: Use this during the tournament final Right jewel Red alliance", group="Pushbot")
//@Disabled
public class tempautoForright extends LinearOpMode {

    Hardwaremap         robot   = new Hardwaremap();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();
    public DcMotor  left_Drive   = null;
    public DcMotor  right_Drive  = null;
    public DcMotor  arm_Drive     = null;
    public CRServo Pin    = null;
    private CRServo Sensorarm = null;
    public ColorSensor colory = null;
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double     ARM_SPEED               =0.3;
    @Override
    public void runOpMode() {
        left_Drive  = hardwareMap.get(DcMotor.class, "left_Drive");
        right_Drive = hardwareMap.get(DcMotor.class, "right_Drive");
        Pin = hardwareMap.get(CRServo.class, "Pin");
        arm_Drive = hardwareMap.get(DcMotor.class, "arm_Drive");
        Sensorarm = hardwareMap.get(CRServo.class, "Sensor_arm");
        colory = hardwareMap.get(ColorSensor.class,"colory");
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders Hi this is Wyatt's program and he is awesome");    //
        telemetry.update();

        robot.left_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right_Drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.left_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.right_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.left_Drive.getCurrentPosition(),
                robot.right_Drive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        colory.enableLed(true);  // Turn the LED on


        Sensorarm.setPower(1);
        Sensorarm.setPower(1);
        Sensorarm.setPower(1);
        encoderDrive(DRIVE_SPEED, 0, 0, 5.0);

        //sleep(1000); this should work as a wait block



            sleep(10000);
            encoderDrive(TURN_SPEED, 3.5, -3.5, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        Sensorarm.setPower(-1);
        Sensorarm.setPower(-1);
        Sensorarm.setPower(-1);
        Sensorarm.setPower(-1);
        Sensorarm.setPower(-1);
        Sensorarm.setPower(-1);
        Sensorarm.setPower(-1);
        sleep(600);
        encoderDrive(DRIVE_SPEED, -22, -22, 4.0);


        /*
        encoderDrive(TURN_SPEED, 10,10, 4.0);
        encoderDrive(DRIVE_SPEED, 10, 10, 4.0);*/


        // encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        // encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout

        //encoderDrive(TURN_SPEED,)
        sleep(1000);     // pause for servos to move

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
            newLeftTarget = robot.left_Drive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.right_Drive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.left_Drive.setTargetPosition(newLeftTarget);
            robot.right_Drive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.left_Drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.right_Drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.left_Drive.setPower(Math.abs(speed));
            robot.right_Drive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.left_Drive.isBusy() && robot.right_Drive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.left_Drive.getCurrentPosition(),
                        robot.right_Drive.getCurrentPosition());
                telemetry.update();


            }

            // Stop all motion;
            robot.left_Drive.setPower(0);
            robot.right_Drive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.left_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.right_Drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }





}


