package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.time.Duration;
import java.time.Instant;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.RobotHardware;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Main Driver OpMode", group="Iterative Opmode")

public class DriverControlMode extends OpMode
{
    private boolean dualControllers = false;
    
    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = null;
    private boolean spinning = false;
    private boolean aReleased = true;
    private boolean bReleased = true;
    private int suckstate = 0;
    private boolean bumperReleased = true;
    private boolean armUp = false;
    private boolean triggerReleased = true;
    private int dpad_state = 0;
    
    private boolean armHeightOverride = false;
    private boolean updateArm = false;
    
    private int armLevel = 0;
    
    private double driveSpeedModifier = 1;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized!");
    }

    @Override
    public void start() {
        runtime.reset();
        telemetry.addData("Status","Started!");
    }
    


    @Override
    public void loop() {
        
        //Main drive control
        robot.setPower(-gamepad1.left_stick_y * driveSpeedModifier, gamepad1.right_stick_x * driveSpeedModifier,-gamepad1.left_stick_x * driveSpeedModifier);
        
        
        if ((gamepad1.a && !dualControllers) || (gamepad2.a && dualControllers)) {
            if (aReleased) {
                aReleased = false;
                if (spinning) {
                    robot.stopCarosel();
                    spinning = false;
                } else {
                    robot.startCarosel(false);
                    spinning = true;
                }
            }
        } else {
            aReleased = true;
        }
        
        //Carosel Control
        
        if ((gamepad1.b && !dualControllers) || (gamepad2.b && dualControllers)) {
            if (bReleased) {
                bReleased = false;
                if (spinning) {
                    robot.stopCarosel();
                    spinning = false;
                } else {
                    robot.startCarosel(true);
                    spinning = true;
                }
            }
        } else {
            bReleased = true;
        }
        
        //Arm Lift Cycle Control
        
        /*if (dualControllers) {
            if (gamepad2.dpad_up) {
                robot.targetLevel = 3;
                updateArm = true;
            } else if (gamepad2.dpad_right) {
                robot.targetLevel = 1;
                updateArm = true;
            } else if (gamepad2.dpad_down) {
                robot.targetLevel = 0;
                updateArm = true;
            }
            
            if (gamepad2.right_stick_y > 0.1) {
                robot.unsuck();
            } else if (gamepad2.right_stick_y < 0.1) {
                robot.suck();
            } else {
                robot.stopsuck();
            }
        }*/
        
        if (/*(*/gamepad1.left_bumper /*&& !dualControllers) || updateArm*/) {
            //updateArm = false;
            if (triggerReleased) {
                triggerReleased = false;
                armLevel++;
                
                switch (armLevel) {
                    case 2:
                        armLevel++; //Skip to level 3
                    case 3: 
                        if (robot.caparm_extended) {
                            //Drop caparm
                            robot.extendCapArm(true);
                        }
                        break;
                    case 4: 
                        if (robot.caparm_extended) {
                            armLevel = 4; //Special Capping level
                        } else {
                            armLevel = 0;
                        }
                        break;
                    case 5:
                        armLevel = 0;
                        if (robot.caparm_extended) {
                            robot.retractCapArm();
                        }
                        break;
                }
                robot.setLiftarmPosition(armLevel);
            }
        } else {
            triggerReleased = true;
        }
        
        //Intake System Cycle Control
        
        if ((gamepad1.right_bumper && !dualControllers)) {
            if (bumperReleased) {
                bumperReleased = false;
                
                switch (suckstate) {
                    case 0:
                        robot.suck();
                        break;
                    case 1:
                        robot.stopsuck();
                        break;
                    case 2:
                        robot.unsuck();
                        break;
                    case 3:
                        robot.stopsuck();
                        suckstate= -1;
                        break;
                }
                suckstate++;
            }
        } else {
            bumperReleased = true;
        }
        
        //Capping Controls
        if (gamepad1.x) {
            robot.extendCapArm(false);
        } else if (gamepad1.y) {
            robot.retractCapArm();
        }
        
        // Turbotrigger
        if (gamepad1.right_trigger > 0.1) {
            
            if (!armHeightOverride) {
                armHeightOverride = true;
                if (armLevel < 1) {
                    armLevel = 1;
                    robot.setLiftarmPosition(1);
                }
            }
            driveSpeedModifier = 0.4f + ((float)gamepad1.right_trigger / 1.5f);
        } else {
            if (armHeightOverride) {
                armHeightOverride = false;
                if (armLevel == 1) {
                    armLevel = 0;
                    robot.setLiftarmPosition(0);
                }
            }
            driveSpeedModifier = 0.4f;
        }
        

        // Show the elapsed game time and wheel power.
        telemetry.addData("Run Time",runtime.toString());
        //telemetry.addData("Liftarm Liftness",robot.liftarm_motor.getCurrentPosition());
      
        if (spinning) {
            telemetry.addData("Spinning!","");
        }
        if (armUp) {
            telemetry.addData("Arm","Up");
        } else {
            telemetry.addData("Arm","Down");
        }
        telemetry.addData("Heading",robot.getHeading());
        telemetry.addData("ARM",String.valueOf(robot.liftarm_m.getCurrentPosition()));
        //telemetry.addData("RGB", String.valueOf(robot.color_sensor.red()) + ", " + String.valueOf(robot.color_sensor.green()) + ", " +String.valueOf(robot.color_sensor.blue()));
    
        telemetry.addData("ARMTARGET", String.valueOf(armLevel));
    }

    @Override
    public void stop() {
        robot.setLiftarmPosition(0);
        robot.retractCapArm();
        telemetry.addData("Status","Done!");
    }

}
