/**
 * BejeweledRunner.java
 * Runs the Bejeweled game.
 */

public class BejeweledRunner {
	public static void main (String[] args) {
		BejeweledGUI gui = new BejeweledGUI ();
		Bejeweled game = new Bejeweled (gui);
		BejeweledListener listener = new BejeweledListener (game, gui);
	}
}