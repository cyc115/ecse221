package lab4newArchetecture;

import lejos.nxt.comm.RConsole;

public class Configuration extends AbstractConfig{

	private static Configuration config = new Configuration();
	
	/**
	 * set the default location ans open RConsole.
	 * 
	 */
	private Configuration (){
		setCurrentLocation(new Coordinate(0, 0, 0));
		setStartLocation(new Coordinate(0, 0, 0));
		RConsole.openUSB(10000);
	}

	public static AbstractConfig getInstance() {
		return config;
	}

	@Override
	public void leftButtonPressed() {
			
	}

	@Override
	public void rightButtonPressed() {
			
	}

}
