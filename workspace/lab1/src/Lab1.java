import lejos.nxt.*;
//hello
public class Lab1 {
	//signifies when buttons are not pressed
	private static final int NOT_PRESSED = 0 ;
	
	private static final SensorPort usPort = SensorPort.S1;
	//private static final SensorPort lightPort = SensorPort.S2;
	
	private static final int bandCenter = 20, bandWidth = 3;
	private static final int motorLow = 100, motorHigh = 400;
	
	
	
	public static void main(String [] args) {
		/*
		 * Wait for startup button press
		 * Button.ID_LEFT = BangBang Type
		 * Button.ID_RIGHT = P Type
		 */
		
		/**
		 * signifies which button (left or right ) is pressed. 
		 * 0 signifies unpressed button.
		 */
		int buttonOption = NOT_PRESSED;
		Printer.printMainMenu();
		
		while (buttonOption == NOT_PRESSED)
			buttonOption = Button.waitForAnyPress();
		
		// Setup controller
		BangBangController bangbang = new BangBangController(bandCenter, bandWidth, motorLow, motorHigh);
		PController p = new PController(bandCenter, bandWidth);
		
		// Setup ultrasonic sensor
		UltrasonicSensor usSensor = new UltrasonicSensor(usPort);
		
		// Setup Printer
		Printer printer = null;
		
		// Setup Ultrasonic Poller
		UltrasonicPoller usPoller = null;
		
		//if button is pressed : 
		switch(buttonOption) {
		case Button.ID_LEFT:
			usPoller = new UltrasonicPoller(usSensor, bangbang);
			printer = new Printer(buttonOption, bangbang);
			break;
		case Button.ID_RIGHT:
			usPoller = new UltrasonicPoller(usSensor, p);
			printer = new Printer(buttonOption, p);
			break;
		default:
			System.out.println("Error - invalid button");
			System.exit(-1);
			break;
		}
		
		usPoller.start();
		printer.start();
		
		//Wait for another button press to exit
		Button.waitForAnyPress();
		System.exit(0);
		
	}
}
