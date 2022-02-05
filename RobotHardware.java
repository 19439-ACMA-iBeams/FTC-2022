package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

class RobotHardware {
    
    //Constants
    static final double COUNTS_PER_REV = 547.7;
    static final double COUNTS_PER_CM = (COUNTS_PER_REV / 96)/10;
    
    private ElapsedTime     runtime = new ElapsedTime();
    
    //Control Hub
    DcMotor fl_m;
    DcMotor fr_m;
    DcMotor bl_m;
    DcMotor br_m;
    
    //Expansion Hub
    DcMotor liftarm_m;
    Servo   cap_s;
    boolean caparm_extended = false;
    
    CRServo carosel;
    CRServo intake_a;
    CRServo intake_b;
    
    BNO055IMU imu;
    ColorSensor color;
    
    static final int LEVEL_0 = 0 * 5;
    static final int LEVEL_1 = 50 * 5;
    static final int LEVEL_2 = 100 * 5;
    static final int LEVEL_3 = 150 * 5;
    static final int LEVEL_4 = 120 * 5;
    
    
    void init(HardwareMap hardware) {
        
        //Initialize Motors
        fl_m = hardware.get(DcMotor.class,"frontleft_drive");
        fr_m = hardware.get(DcMotor.class,"frontright_drive");
        bl_m = hardware.get(DcMotor.class,"backleft_drive");
        br_m = hardware.get(DcMotor.class,"backright_drive");
        
        fr_m.setDirection(DcMotorSimple.Direction.FORWARD);
        br_m.setDirection(DcMotorSimple.Direction.FORWARD);
        fl_m.setDirection(DcMotorSimple.Direction.REVERSE);
        bl_m.setDirection(DcMotorSimple.Direction.REVERSE);
        
        //Reset Encoders
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        //Initialize Lift Arm & Capping Arm
        liftarm_m = hardware.get(DcMotor.class,"liftarm");
        liftarm_m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftarm_m.setDirection(DcMotor.Direction.REVERSE);
        
        liftarm_m.setTargetPosition(LEVEL_0);
        liftarm_m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftarm_m.setPower(0.5);
        
        cap_s = hardware.get(Servo.class,"cap_servo");
        
        //Init Spinnythings
        carosel = hardware.get(CRServo.class,"carosel");
        intake_a = hardware.get(CRServo.class,"intake_a");
        intake_b = hardware.get(CRServo.class,"intake_b");
        
        //Init IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        imu = hardware.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);
        
        //Init Color Sensor
        color = hardware.get(ColorSensor.class,"color");
        color.enableLed(false);
    }
    
    void setMode(DcMotor.RunMode runmode) {
        fl_m.setMode(runmode);
        fr_m.setMode(runmode);
        bl_m.setMode(runmode);
        br_m.setMode(runmode);
    }
    
    boolean anyMotorsBusy() {
        return fl_m.isBusy() ||fr_m.isBusy() || bl_m.isBusy() || br_m.isBusy();
    }
    void stop() {
        setPower(0,0,0);
    }
    
    double getHeading() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES).firstAngle;
    }
    
    
    //Strafe is to the right, turn is clockwise.
    void setPower(double forward, double strafe, double turn) {
        fl_m.setPower(Range.clip(forward + strafe - turn,-1,1));
        fr_m.setPower(Range.clip(forward - strafe + turn,-1,1));
        bl_m.setPower(Range.clip(forward + strafe + turn,-1,1));
        br_m.setPower(Range.clip(forward - strafe - turn,-1,1));
    }
    
    void setLiftarmPosition(int level) {
        int targetPos = 0;
        switch (level) {
            case 0: 
                targetPos = LEVEL_0;
                break;
            case 1:
                targetPos = LEVEL_1;
                break;
            case 2:
                targetPos = LEVEL_2;
                break;
            case 3:
                targetPos = LEVEL_3;
                break;
            case 4:
                targetPos = LEVEL_4;
                break;
            default:
                return;
        }
        liftarm_m.setTargetPosition(targetPos);
        liftarm_m.setPower(0.5);
    }
    
    void extendCapArm(boolean lowmode) {
        if (lowmode) {
            cap_s.setPosition(0.75);
        } else {
            cap_s.setPosition(0.54);
        }
        caparm_extended = true;
    }
    void retractCapArm() {
        caparm_extended = false;
        cap_s.setPosition(0);
    }
    
    void suck() {
        intake_a.setPower(1);
        intake_b.setPower(-1);
    }
    void unsuck() {
        intake_a.setPower(-1);
        intake_b.setPower(1);
    }
    void stopsuck() {
        intake_a.setPower(0);
        intake_b.setPower(0);
    }
    
    void startCarosel(boolean blue) {
        if (blue) {
            carosel.setPower(-1);
        } else {
            carosel.setPower(1);
        }
    }
    
    void stopCarosel() {
        carosel.setPower(0);
    }
    
    boolean isCup() {
        return false;
    }
    
    void encoderDrive(double power, double forward, double strafe, double maxTime) {
        //Set up timer
        runtime.reset();
        
        int fr_start = fr_m.getCurrentPosition();
        int br_start = br_m.getCurrentPosition();
        int fl_start = fl_m.getCurrentPosition();
        int bl_start = bl_m.getCurrentPosition();
        
        
        int fr_target = fr_start + (int)((-strafe+forward) * COUNTS_PER_CM);
        int br_target = br_start + (int)((strafe+forward) * COUNTS_PER_CM);
        int fl_target = fl_start + (int)((strafe+forward) * COUNTS_PER_CM);
        int bl_target = bl_start + (int)((-strafe+forward) * COUNTS_PER_CM);
        fr_m.setTargetPosition(fr_target);
        br_m.setTargetPosition(br_target);
        fl_m.setTargetPosition(fl_target);
        bl_m.setTargetPosition(bl_target);
       
        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power,0,0);
        
        double initialRotation = getHeading();
        
        double currentHeading;
        float left = 1;
        float right = 1;
        float mult = 0;
        while(runtime.seconds() <= maxTime && anyMotorsBusy()) {
                  
            double fr_pos = fr_m.getCurrentPosition() / COUNTS_PER_CM;
            double fl_pos = fl_m.getCurrentPosition() / COUNTS_PER_CM;
            double br_pos = br_m.getCurrentPosition() / COUNTS_PER_CM;
            double bl_pos = bl_m.getCurrentPosition() / COUNTS_PER_CM;
            
            double fr_progress = (fr_pos - fr_start) / (fr_target - fr_start);
            double fl_progress = (fl_pos - fl_start) / (fl_target - fl_start);
            double br_progress = (br_pos - br_start) / (br_target - br_start);
            double bl_progress = (bl_pos - bl_start) / (bl_target - bl_start);
            
            
            currentHeading = getHeading();
            
            if (currentHeading > initialRotation) {
                left = 0.003f * mult;
                right = -0.003f * mult;
                mult += 0.2f;
            } else if (currentHeading < initialRotation) {
                left = -0.003f * mult;
                right = 0.003f * mult;
                mult += 0.2f;
            }
            
            if (fr_progress > 0.65) fr_m.setPower(Range.clip((power * (1-fr_progress))+right,-1.0,1.0));
            if (fl_progress > 0.65) fl_m.setPower(Range.clip((power * (1-fl_progress))+left, -1.0,1.0));
            if (br_progress > 0.65) br_m.setPower(Range.clip((power * (1-br_progress))+right,-1.0,1.0));
            if (bl_progress > 0.65) bl_m.setPower(Range.clip((power * (1-bl_progress))+left, -1.0,1.0));
                  
            //WAIT
        }
        
        stop();
    }
    
    public void turnTo(double targetAngle, double power, double maxTime) {
        runtime.reset();

        boolean direction;
        
        double currentAngle = getHeading();
        
        if (targetAngle > currentAngle) {
            direction = true;
            setPower(0,power,0);
        } else {
            direction = false;
            setPower(0,-power,0);
        }
        
        while (runtime.seconds() <= maxTime) {
            currentAngle = getHeading();
            if (currentAngle < targetAngle + 0.1 &&
                currentAngle > targetAngle - 0.1) {
                
                break; //We've arrived at the target angle (+-tolerance) and anrt going to drift firther   
                
            } else if ((targetAngle > currentAngle) != direction) {
                if (direction) {
                    setPower(0,-power/5,0);
                } else {
                    setPower(0,power/5,0);
                }
            }
            
        }
        stop();
    }
    
    
    void deinit() {
        
    }
}