package lab3StartFromFresh;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.LCPBTResponder;

public class Configuration implements RobotConfiguration{

	private Object lock ;
	private Coordinate startLocation;// starting location
	private Coordinate currentLocation ; //current location ;
	private Coordinate nextLocation; //current location ;
	//Threads to be started 
	private Odometer odometer;
	private LCDWriter monitor;
	private Planner planner ;
	private Drivable driver ;
	private UsPoller usPoller ;
	

	//	private LCPBTResponder lcpThread;   //pc debugging not used in this demo
	static boolean driveComplete  = false ;
	
	public static Configuration getDefaultLab3Config(){
		Configuration config = new Configuration();
		config.startLocation = new Coordinate(0,0,0); 
		config.currentLocation = config.startLocation.clone();
		
//		config.lcpThread = new LCPBTResponder();
		config.odometer = new Odometer(config);
		config.monitor = new LCDWriter(config);
		config.planner = new UltraSonicPlanner(config);
		config.usPoller = new UsPoller(config);
		config.driver = new ZigZag(config);
		
		return config;
	}
	
	public Configuration(){
		lock = new Object();

//		lcpThread.setDaemon(true);
//		lcpThread.start();
	}
	@Override
	public double getAvgRadius() {
		return (LEFT_RADIUS + RIGHT_RADIUS)/2;
	}
	@Override
	public Odometer getOdometer() {
		return odometer;
	}
	@Override
	public void setOdometer(Odometer odometer) {
		this.odometer = odometer;
	}
	@Override
	public LCDWriter getMonitor() {
		return monitor;
	}
	@Override
	public void setMonitor(LCDWriter monitor) {
		this.monitor = monitor; 
	}
	public double getWidth() {
		return WIDTH;
	}

	@Override
	public Coordinate getStartingCoordinate() {
		Coordinate coord ;
		synchronized(lock){
			coord = startLocation;
		}
		return coord;
	}

	public void setStartingCoordinate(Coordinate c ) {
		synchronized (lock) {
			startLocation = c;
		}
	}

	@Override
	public void writeToMonitor(String str,int ln) {
		this.getMonitor().writeToScreen(str, ln);
	}

	@Override
	public boolean driveComplete() {
		return 	driveComplete;
	}
	
	public void setDriveComplete(boolean comp) {
		driveComplete = comp;
	}

	@Override
	public Coordinate getCurrentLocation() {
		Coordinate coord ;
		synchronized(lock){
			coord = currentLocation;
		}
		return coord;
	}

	@Override
	public void setCurrentLocation(Coordinate loc) {
		synchronized(lock){
			currentLocation = loc;
		}
	}

	@Override
	public Coordinate getNextLocation() {
		return nextLocation;
	}

	@Override
	public void setNextLocation(Coordinate loc) {
		nextLocation = loc;
	}

	@Override
	public RobotConfiguration setDriver(Drivable driver) {
		this.driver = driver;
		return this;
	}

	@Override
	public Drivable getDriver() {

		return driver;
	}

	@Override
	public Planner getPlanner() {
		return planner;
	}

	@Override
	public void setPlanner(Planner p) {
		planner = p;
	}
	
	public void leftButtonPressed(){
		//really important ... don't forget to start 
		monitor.start();
		odometer.start();
		planner.start();//ultrasonic poller and pCOntrol are started implicitly by planner 
		driver.start();
		usPoller.start();
	}
	@Override
	/**
	 * reset the motot speed to default forward_speed and motor rotation to forward
	 */
	public void resetMotorSpeed() {
		RobotConfiguration.LEFT_MOTOR.stop();
		RobotConfiguration.RIGHT_MOTOR.stop();
		
		RobotConfiguration.LEFT_MOTOR.setSpeed(FORWARD_SPEED);
		RobotConfiguration.RIGHT_MOTOR.setSpeed(FORWARD_SPEED);
		
		RobotConfiguration.LEFT_MOTOR.forward();
		RobotConfiguration.RIGHT_MOTOR.forward();
	}

	@Override
	public void rightButtonPressed() {
		
	}

	@Override
	public UsPoller getUsPoller() {
		return usPoller;
	}

	@Override
	public void setUsPoller(UsPoller ultrasonicPoller) {
		usPoller  = ultrasonicPoller;
		
	}


	
}
