package org.firstinspires.ftc.teamcode;

import java.lang.Math;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutonomousOpMode extends LinearOpMode 
{

    ElapsedTime runtime = new ElapsedTime();
    RobotHardware       robot   = new RobotHardware();
    
    private final double NORMAL_SPEED = 0.7;
    private final double SLOW_SPEED   = 0.5;
    
    public void pause(double seconds) {
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < seconds) {
            //Wait
        }
    }
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        run();
    }
    public void run() {
        robot.suck(); //Pick up block
        pause(0.5);
        robot.stopsuck();
        
        robot.setLiftarmPosition(1); //Go to carosel
        pause(1);
        robot.encoderDrive(NORMAL_SPEED,19,6,2);
        
        robot.startCarosel(true);  //Spin Carosel
        robot.encoderDrive(SLOW_SPEED,0f,12,1);
        pause(1);
        robot.stopCarosel();
        
        /* Get set up for cup scanning */
        robot.setLiftarmPosition(0);
        pause(1);
        robot.encoderDrive(0.6,19, -35f,6);
        robot.turnTo(0,0.1f,3);
        robot.encoderDrive(0.3, 0f,-2,3);
        
        
        int i = 0;
        while (i < 3) {
            i+= 1;
            if (robot.isCup() || robot.isCup() || robot.isCup() || i == 3) {
                break;
            }
            robot.encoderDrive(SLOW_SPEED,0f,-26,3);
            pause(1);
        }
        switch (i) {
            case 0:
                telemetry.addData("Sensed","1");
                //First Level
                robot.setLiftarmPosition(1);
                robot.encoderDrive(SLOW_SPEED,-2,-92,4);
                break;
            case 1:
                telemetry.addData("Sensed","2");
                //Second Level
                robot.setLiftarmPosition(2);
                robot.encoderDrive(SLOW_SPEED, -2,-71,4);
                break;
            case 3:
                telemetry.addData("Error","Didn't sense block");
                //We didn't see any cups, so pretend it should go on level 3 (For max points)
                //encoderDrive(-2/100f,-21/100f,normalSpeed,4);
            case 2:
                telemetry.addData("Sensed","3");
                //Third Level
                robot.setLiftarmPosition(3);
                robot.encoderDrive(SLOW_SPEED,-2,-51,4);
                break;
        }
        robot.encoderDrive(SLOW_SPEED,26,0f,4);
        robot.unsuck();
        pause(0.5);
        robot.stopsuck();
    }
}