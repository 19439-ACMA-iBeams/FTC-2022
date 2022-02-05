package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class TestMode extends OpMode {
    
    private ElapsedTime runtime = new ElapsedTime();
    private RobotHardware robot = null;

    @Override
    public void init() {
        robot = new RobotHardware();
        robot.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized Successfully");
    }

    @Override
    public void start() {
        runtime.reset();
        telemetry.addData("Status","Started!");
    }

    @Override
    public void loop() {
        
        if (gamepad1.right_trigger > 0.5) {
            robot.suck();
        } else {
            robot.unsuck();
        }
        
        if (gamepad1.y) {
            robot.fl_m.setPower(0.3);
        } else {
            robot.fl_m.setPower(0);
        }
        if (gamepad1.x) {
            robot.bl_m.setPower(0.3);
        } else {
            robot.bl_m.setPower(0);
        }
        /*robot.targetLevel = 4;
        robot.processArm();*/
        
        
        if (gamepad1.b) {
            robot.fr_m.setPower(0.3);
        } else {
            robot.fr_m.setPower(0);
        }
        if (gamepad1.a) {
            robot.br_m.setPower(0.3);
        } else {
            robot.br_m.setPower(0);
        }
        
        telemetry.addData("FL",robot.fl_m.getCurrentPosition());
        telemetry.addData("FR",robot.fr_m.getCurrentPosition());
        telemetry.addData("BL",robot.bl_m.getCurrentPosition());
        telemetry.addData("BR",robot.br_m.getCurrentPosition());
        telemetry.addData("Heading",robot.getHeading());
        telemetry.addData("R",robot.color.red());
        telemetry.addData("G",robot.color.green());
        telemetry.addData("B",robot.color.blue());
        telemetry.addData("Yellowness", (robot.color.red() + robot.color.green() - robot.color.blue())/2  );
    }
}