/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name="Auto Red", group="Iterative Opmode")
public class AutoRed extends AutonomousOpMode {
    public double normalSpeed = 0.50;
    public double slow        = 0.30;
    
    @Override
    public void run() {
        
        /* Pick up block and run carosel *
        robot.suck(); //Pick up block
        waitS(0.5);
        robot.stopsuck();
        
        robot.setLiftarmPosition(1); //Go to carosel
        waitS(1);
        encoderDrive(19/100f,6/100f,normalSpeed,2);
        
        robot.startCarosel(true);  //Spin Carosel
        encoderDrive(0f,12/100f,slow,1);
        waitS(1);
        robot.stopCarosel();
        
        /* Get set up for cup scanning *
        robot.setLiftarmPosition(0);
        waitS(1);
        encoderDrive(19/100f, -35f/100f,0.6,6);
        robot.turnSpeedModifier = 0.1f;
        turnTo(0,0.1f,3);
        encoderDrive(0f,-2/100f,0.3,3);
        
        
        int i = 0;
        while (i < 3) {
            i+= 1;
            if (robot.isCup() || robot.isCup() || robot.isCup() || i == 3) {
                break;
            }
            encoderDrive(0f,-26/100f,slow,3);
            waitS(1);
        }
        switch (i) {
            case 0:
                telemetry.addData("Sensed","1");
                //First Level
                robot.setLiftarmPosition(1);
                encoderDrive(-2/100f,-92/100f,slow,4);
                break;
            case 1:
                telemetry.addData("Sensed","2");
                //Second Level
                robot.setLiftarmPosition(2);
                encoderDrive(-2/100f,-71/100f,slow,4);
                break;
            case 3:
                telemetry.addData("Error","Didn't sense block");
                //We didn't see any cups, so pretend it should go on level 3 (For max points)
                //encoderDrive(-2/100f,-21/100f,normalSpeed,4);
            case 2:
                telemetry.addData("Sensed","3");
                //Third Level
                robot.setLiftarmPosition(3);
                encoderDrive(-2/100f,-51/100f,slow,4);
                break;
        }
        encoderDrive(26/100f,0f,slow,4);
        robot.unsuck();
        waitS(0.5);
        robot.stopsuck();
    }
}*/
