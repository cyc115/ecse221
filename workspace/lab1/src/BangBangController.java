import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwith;
	private final int MOTOR_LOW, MOTOR_HIGH;
	private final int motorStraight = 100;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;
	private int distance;
	private int currentLeftSpeed;
	private final int CHANGE_SPEED_FACTOR = 20 ;
	
	public BangBangController(int bandCenter, int bandwith, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		this.MOTOR_LOW = motorLow;
		this.MOTOR_HIGH = motorHigh;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		this.distance = distance;
		//Just right 
		if ( Math.abs(distance - bandCenter) < bandwith )
		{
			//do nothing 
		}
		//make correction 
		else
		{	double speedFactor = 1.4; //control the overall speed 
			int fast = 240 ; //control the correction 
			int slow = 150 ;
			
			/**
			 * the distance where we're probably facing a wall. 
			 */
			int criticalDistance = 20;
			
			if (distance < criticalDistance ){
				rightMotor.setSpeed( 10 );
				leftMotor.setAcceleration(fast );
			}
			//too close -- go right
			else if (distance < bandCenter ) {
				rightMotor.setSpeed((int) (slow *speedFactor) ); 
				leftMotor.setSpeed((int) (fast * speedFactor) ); 
//				rightMotor.setSpeed(rightMotor.getSpeed() - motorStraight/CHANGE_SPEED_FACTOR ); 
//				leftMotor.setSpeed(leftMotor.getSpeed() + motorStraight/CHANGE_SPEED_FACTOR ); 
			}
			// too far -- go left 
			else if (distance > bandCenter ){
				rightMotor.setSpeed((int) (fast * speedFactor) ); 
				leftMotor.setSpeed((int) (slow *speedFactor)); 
				
//				rightMotor.setSpeed(rightMotor.getSpeed() + motorStraight/CHANGE_SPEED_FACTOR ); 
//				leftMotor.setSpeed(leftMotor.getSpeed() - motorStraight/CHANGE_SPEED_FACTOR ); 
			}  
			else {
				System.out.println("error");
			}
				
		}
		
		
		
	}

	public int getLeftMotorSpeed(){
		return leftMotor.getSpeed();
	}
	public int getRightMotorSpeed(){
		return rightMotor.getSpeed();
	}
	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
