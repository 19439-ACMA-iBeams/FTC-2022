/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue", group="Iterative Opmode")
public class AutoBlue extends AutonomousOpMode
{
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.turnSpeedModifier = 0.3f;
        
        waitForStart();
        
        robot.grab();
        waitS(0.6);
        robot.liftArm();
        waitS(0.6);
        encoderDrive(0.56f,0f,0.5f,3f);
        robot.release();
        waitS(0.6);
        encoderDrive(-0.10f,0,0.5f,3f);
        encoderDrive(-0.50f,1.30f,0.5f,3f);
        turnTo(78f,1f,6f);
        robot.startCarosel(true);
        waitS(3);
        robot.stopCarosel();
        turnTo(78f,1f,6f);
        encoderDrive(3f,0f,1f,10f);
    }
}
*/