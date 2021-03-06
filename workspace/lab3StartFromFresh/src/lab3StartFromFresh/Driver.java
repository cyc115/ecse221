package lab3StartFromFresh;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

public abstract class Driver extends Thread implements Drivable {
	protected RobotConfiguration config ;
	protected Coordinate currentCoordinate, startCoord , endCoord;
	protected NXTRegulatedMotor leftMotor , rightMotor;
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
	 * TODO this will check for 
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
//		endCoord = nextLocation;
		config.setStartingCoordinate(currentCoordinate.clone());
//		startCoord = currentCoordinate.clone() ;
				
		double distance = Coordinate.calculateDistance(currentCoordinate, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentCoordinate, nextLocation);
		
		RConsole.println("Driver:travelTo:CurrentCoord: " + currentCoordinate.toString());
		RConsole.println("Driver:travelTo:NxtCoord: " + nextLocation.toString());
		RConsole.println("Driver:travelTo:traveling dist: " + distance);
		RConsole.println("Driver:travelTo:turning Angle: " + turningAngle);
	

		//TODO remove debugging information 
		config.writeToMonitor( ((Double)distance).toString(), 1);
		config.writeToMonitor( ((Double)turningAngle).toString(), 2);	
		
		//make turn
		rotateToRelativly(turningAngle);
		boolean finishedTravelTo = false ;
		while(!finishedTravelTo){
			
			//when navigating
			while(!config.getPlanner().hasWallAhead()){
				pause = false ;
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
				
				leftMotor.rotate(
						convertDistance(RobotConfiguration.LEFT_RADIUS, moveDist), 
						true
						);
				rightMotor.rotate(
						convertDistance(RobotConfiguration.RIGHT_RADIUS, moveDist), 
						false
						);
			}
			//if wall follows 
			if (!config.getPlanner().hasWallAhead()){
				pause = true ;
				//sleep for 0.5 sec and check again 
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			
		}
		
		
		leftMotor.rotate(
				convertDistance(RobotConfiguration.LEFT_RADIUS, distance), 
				true
				);
		rightMotor.rotate(
				convertDistance(RobotConfiguration.RIGHT_RADIUS, distance), 
				false
				);
		
		Coordinate temp = new Coordinate(
					nextLocation.getX(), nextLocation.getY() ,
					Coordinate.normalize((currentCoordinate.getTheta() + turningAngle))
				);
		currentCoordinate = temp ;
		RConsole.println("Driver:travelTo:currentCoordinate : x " + config.getCurrentLocation().getX()
			+"\ty " + config.getCurrentLocation().getY() 
			+ "\ttheata " +config.getCurrentLocation().getTheta());
			RConsole.println("=======");
		
	}

	/**
	 * rotate to the angle wrt to the current robot angle 
	 * @param degree
	 */
	public void rotateToRelativly(double degree){
		
		rightMotor.setSpeed(RobotConfiguration.ROTATE_SPEED);
		leftMotor.setSpeed(RobotConfiguration.ROTATE_SPEED);
		
		if (degree < 0){		//if degree is negative then rotate back ward
			leftMotor.backward();
			rightMotor.backward();
		}
		leftMotor.rotate(
				convertAngle(RobotConfiguration.LEFT_RADIUS, config.getWidth(), degree)
				, true);
		rightMotor.rotate(
				-convertAngle(RobotConfiguration.RIGHT_RADIUS,config.getWidth() , degree)
				, false);
		
		rightMotor.setSpeed(RobotConfiguration.FORWARD_SPEED);
		leftMotor.setSpeed(RobotConfiguration.FORWARD_SPEED);
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
	@Override
	public void turnTo(double theata) {
		rotateToRelativly(theata);
		
	}
	

	@Override
	public boolean isNagivating() {
		// TODO Auto-generated method stub
		return false;
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
	@Override
	public void pause() {
		pause = true;
	}
	public void unpause(){
		pause = false ;
	}
	@Override
	public boolean isPaused() {
		return pause;
	}
}
