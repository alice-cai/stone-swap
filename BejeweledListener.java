import javax.swing.*;
import java.awt.event.*;
import java.awt.Component;

public class BejeweledListener implements MouseListener {
	private BejeweledGUI gui;
	private Bejeweled game;

	public BejeweledListener (Bejeweled game, BejeweledGUI gui) {
		this.game = game;
		this.gui = gui;
		gui.addListener (this);
	}

	public void mouseClicked (MouseEvent event) {
		Component objectClicked = event.getComponent();

		if (objectClicked instanceof JLabel) {
			JLabel label = (JLabel) objectClicked;		
			int row = gui.getRow(label);
			int column = gui.getColumn (label);
			game.play(row, column);
		}
		else if (objectClicked instanceof JButton) {
			if (event.getSource() == gui.getHintButton()) {
				game.displayHint();
			} else {
				game.endGame();
			}
		}
	}

	public void mousePressed (MouseEvent event) {
	}

	public void mouseReleased (MouseEvent event) {
	}


	public void mouseEntered (MouseEvent event) {
	}

	public void mouseExited (MouseEvent event) {
	}
}