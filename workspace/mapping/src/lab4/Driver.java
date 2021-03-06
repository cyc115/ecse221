package lab4;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
 
public abstract class Driver extends Thread {

	protected RobotConfiguration config ;
	protected Coordinate currentCoordinate, startCoord , endCoord;
	protected NXTRegulatedMotor leftMotor , rightMotor;
	/**
	 * should the driver be paused ?
	 */
	protected boolean pause = false ;
	private final int CHECK_DISTANCE = 100;

	public Driver(RobotConfiguration config){
		this.config = config ;
		leftMotor = RobotConfiguration.LEFT_MOTOR;	// this is very frequently used
		rightMotor = RobotConfiguration.RIGHT_MOTOR;
		
		currentCoordinate = config.getCurrentLocation();
		endCoord = config.getStartingCoordinate();
	}


	/**
	 * travel to wrt to the global (0,0) coordinate.
	 * @param nextLocation
	 */
	public void travelTo(double x, double y) {
		travelTo(new Coordinate(x,y,0));
	}

	/**
	 * travel to wrt to the global (0,0) coordinate
	 * @param nextLocationg
	 */
	public void travelTo(Coordinate nextLocation) {
		config.setNextLocation(nextLocation);
		config.setStartingCoordinate(currentCoordinate.clone());

		double distance = Coordinate.calculateDistance(currentCoordinate, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentCoordinate, nextLocation);
		RConsole.println("Driver:travelTo:NxtCoord: " + nextLocation.toString());
		RConsole.println("Driver:travelTo:traveling dist: " + distance);
		RConsole.println("Driver:travelTo:turning Angle: " + turningAngle);
		
		//make turn
		RConsole.println("Driver:travelTo:making turn: " + turningAngle);
		rotateToRelatively(turningAngle);
		//set to forward speed 
		RConsole.println("Driver:travelTo:setting speed: " + config.getForwardSpeed());
		RobotConfiguration.LEFT_MOTOR.setSpeed(config.getForwardSpeed());
		RobotConfiguration.RIGHT_MOTOR.setSpeed(config.getForwardSpeed());
		
		
		boolean finishedTravelTo = false ;
		
		while(!finishedTravelTo){
			
			//when navigating
			while( 
					//!config.getPlanner().hasWallAhead()
					//check if it has wall ahead, this is disabled because lab 4 has no planner 
					true
					){
				double moveDist;
				//move x cm forward if distance is bigger then 1cm
				if (distance > CHECK_DISTANCE ){
					moveDist = CHECK_DISTANCE;
					distance -= CHECK_DISTANCE;					//minus 1cm from distance
				}
				else {
					moveDist = distance ;
					finishedTravelTo = true ;
					break;
				}
				travel(moveDist);
			}
			/*if wall follows 
			 * TODO revert this when we have a planner 
			if (!config.getPlanner().hasWallAhead()	){
				
				handleObsticle();
				//sleep for 0.5 sec and check again 
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			*/
			
		}
		travel(distance);
		Coordinate temp = new Coordinate(
					nextLocation.getX(), nextLocation.getY() ,
					Coordinate.normalize((currentCoordinate.getTheta() + turningAngle))
				);
		currentCoordinate = temp ;
		RConsole.println("Driver:travelTo:currentCoordinate : x " + config.getCurrentLocation().getX()
				+"\ty " + config.getCurrentLocation().getY() 
				+ "\ttheata " +config.getCurrentLocation().getTheta());
	}

	/**
	 * what to do when there is wall detected ahead 
	 */
	protected abstract void handleObsticle();
	
	/**
	 * move forward x cm 
	 * @param dist
	 */
	public void travel(double dist){
		
		rightMotor.setSpeed(config.getForwardSpeed());
		leftMotor.setSpeed(config.getForwardSpeed());
		
		leftMotor.rotate(
				convertDistance(RobotConfiguration.LEFT_RADIUS, dist), 
				true
				);
		rightMotor.rotate(
				convertDistance(RobotConfiguration.RIGHT_RADIUS, dist), 
				false
				);
	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 *	<br> this method is here only for comparability.
	 *	<br> use rotateToRelatively (doubel degree , boolean returnRightAway);
	 *	when ever possible 
	 * @param degree
	 * @deprecated
	 */
	protected void rotateToRelatively(double degree){
		rotateToRelatively(degree, false);
	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 * @param degree 
	 * @param returnRightAway should the function finish before finishing the turn 
	 */
	public void rotateToRelatively(double degree, boolean returnRightAway){
		rightMotor.setSpeed(config.getRotationSpeed());
		leftMotor.setSpeed(config.getRotationSpeed());
		
		
		if (degree < 0){		//if degree is negative then rotate back ward
			leftMotor.backward();
			rightMotor.backward();
		}
		
		leftMotor.rotate(
				convertAngle(RobotConfiguration.LEFT_RADIUS, config.getWidth(), degree)
				, true);
		rightMotor.rotate(
				-convertAngle(RobotConfiguration.RIGHT_RADIUS,config.getWidth() , degree)
				, returnRightAway);
	}
	/**
	 * 
	 * @param radius radius of the wheel 
	 * @param distance distance we want to cover 
	 * @return 
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	/**
	 * turn to angle wrt to the y axies 
	 */
	public void turnTo(double theata) {
		rotateToRelatively(theata);
	}
	public boolean isNagivating() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public RobotConfiguration getConfig() {
		return config;
	}
	public void setConfig(RobotConfiguration config) {
		this.config = config;
	}
	public Coordinate getCurrentCoordinate() {
		return currentCoordinate;
	}
	public void setCurrentCoordinatel(Coordinate currentCoordinate) {
		this.currentCoordinate = currentCoordinate;
	}

}
