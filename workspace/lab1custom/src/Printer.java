import lejos.nxt.Button;
import lejos.nxt.LCD;
/**
 * Object used to print on the Screen of the NJX brick
 */
public class Printer extends Thread {
	
	private UltrasonicController cont;
	private final int option;
	/**
	 * create a new printer object 
	 * @param option button click option code , 0 , 2, 4
	 * @param cont
	 */
	public Printer(int option, UltrasonicController cont) {
		this.cont = cont;
		this.option = option;
	}
	
	public void run() {
		while (true) {
			LCD.clear();
			LCD.drawString("Controller Type is... ", 0, 0);
			if (this.option == Button.ID_LEFT)
				LCD.drawString("BangBang", 0, 1);
			else if (this.option == Button.ID_RIGHT)
				LCD.drawString("P controller", 0, 1);
			LCD.drawString("US Distance: " + cont.readUSDistance(), 0, 2 );
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
	
	public static void printMainMenu() {
		LCD.clear();
		LCD.drawString("left = bangbang",  0, 0);
		LCD.drawString("right = p type", 0, 1);
	}
}