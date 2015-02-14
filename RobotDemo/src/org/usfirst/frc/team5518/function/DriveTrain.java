package org.usfirst.frc.team5518.function;

import org.usfirst.frc.team5518.framework.RobotFunction;
import org.usfirst.frc.team5518.robot.Robot;
import org.usfirst.frc.team5518.robot.RobotMap;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends RobotFunction {
	
	public static final double kDefaultSensitivity = 0.75;

	private RobotDrive m_robot;
	private Thread m_thread = null;
	private Runnable m_runnable = null;
	
	private float speed_factor;
	
	public DriveTrain(String name) {
		super(name);
	}

	@Override
	public void initialize() {
		m_robot = new RobotDrive(RobotMap.FRONT_LEFT_MOTOR, RobotMap.REAR_LEFT_MOTOR,
				RobotMap.FRONT_RIGHT_MOTOR, RobotMap.REAR_RIGHT_MOTOR);
		m_robot.setExpiration(RobotDrive.kDefaultExpirationTime);  // set expiration time for motor movement if error occurs
		m_robot.setSafetyEnabled(true);  // enable safety so motors don't burn out
		m_robot.setInvertedMotor(MotorType.kFrontRight, true);
		//m_robot.setInvertedMotor(MotorType.kFrontLeft, true);
		m_robot.setInvertedMotor(MotorType.kRearRight, true);
		//m_robot.setInvertedMotor(MotorType.kRearLeft, true);
		
		m_runnable = new Runnable() {
			
			@Override
			public void run() {
				m_robot.drive(-0.5, 0.0);	// drive forwards half speed
				Timer.delay(2.0);		//    for 2 seconds
				m_robot.drive(0.0, 0.0);	// stop robot
			}
		};
	}

	@Override
	public void start() {
		
		if (Robot.jOi.getJoystick().getRawButton(RobotMap.BTN_TRIGGER))
			speed_factor = 0.25f;
		else
			speed_factor = 0.75f;
		
		m_robot.mecanumDrive_Cartesian(Robot.jOi.getJoystick().getRawAxis(RobotMap.X_AXIS)*speed_factor,
				Robot.jOi.getJoystick().getRawAxis(RobotMap.Y_AXIS)*speed_factor, 
				Robot.jOi.getJoystick().getRawAxis(RobotMap.Z_AXIS)*speed_factor, 0);
		
	}

	@Override
	public void outputHandler() {
		log();
	}
	
	@Override
	protected void log() {
		super.log();
		SmartDashboard.putNumber("Joystick X Axis", Robot.jOi.getJoystick().getRawAxis(RobotMap.X_AXIS));
		SmartDashboard.putNumber("Joystick Y Axis", Robot.jOi.getJoystick().getRawAxis(RobotMap.Y_AXIS));
		SmartDashboard.putNumber("Joystick Z Axis", Robot.jOi.getJoystick().getRawAxis(RobotMap.Z_AXIS));
	}
	
	public void autoStart() {
		m_thread = new Thread(m_runnable);
        m_thread.start();
	}
	
	public void autoStop() {
		if (m_thread != null) m_thread.interrupt();
	}
	
	// *********************** HELPER FUNCTIONS **************************
	
	public void setMaxPower(double power) {
		m_robot.setMaxOutput(power);
	}
	
	public void setSensitivity(double sensitivity) {
		m_robot.setSensitivity(sensitivity);
	}

}
