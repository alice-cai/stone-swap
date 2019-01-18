/**
* BejeweledGUI.java
* Provides the GUI for the Bejeweled game.
*/

import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;

public class BejeweledGUI {
	// the name of the configuration file
	private final String CONFIGFILE = "config.txt";
	private final Color BACKGROUNDCOLOUR = new Color(255, 255, 255);

	private JLabel[][] slots;
	private JFrame mainFrame;
	private ImageIcon[] pieceIcon;
	private JButton endGameButton;
	private JTextField score;
	private JTextField numMoveLeft;
	private JButton hintButton;

	private String messageIconFile;
	private ImageIcon messageIcon;

	private String logoIcon;
	private String[] iconFile;

	public final int NUMPIECESTYLE = 7;    // number of different piece styles
	public final int NUMROW = 8;           // number of rows on the game board
	public final int NUMCOL = 8;           // number of columns on the game board

	/**
	* Constants defining the demensions of the different components on the GUI.
	*/    
	private final int PIECESIZE = 70;
	private final int PLAYPANEWIDTH = NUMCOL * PIECESIZE;
	private final int PLAYPANEHEIGHT = NUMROW * PIECESIZE;

	private final int INFOPANEWIDTH = 2 * PIECESIZE + 30;
	private final int INFOPANEHEIGHT = PLAYPANEHEIGHT;

	private final int LOGOHEIGHT = 2 * PIECESIZE + 10;
	private final int LOGOWIDTH = PLAYPANEWIDTH + INFOPANEWIDTH;

	private final int FRAMEWIDTH = (int)(LOGOWIDTH);
	private final int FRAMEHEIGHT = (int)((LOGOHEIGHT + PLAYPANEHEIGHT) * 1.1);


	// Constructor:  BejeweledGUI
	// - intialize variables from config files
	// - initialize the imageIcon array
	// - initialize the slots array
	// - create the main frame
	public BejeweledGUI () {
		iconFile = new String[NUMPIECESTYLE];

		initConfig();
		initImageIcon();
		initSlots();
		createMainFrame();
		messageIcon = new ImageIcon(messageIconFile);
	}


	private void initConfig() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(CONFIGFILE));

			logoIcon = in.readLine();
			for (int i = 0; i < NUMPIECESTYLE; i++) {
				iconFile[i] = in.readLine();
			}
			messageIconFile = in.readLine();

			in.close();
		} catch (IOException iox) {
			System.out.println("Error reading from config file.");
		}
	}


	// initImageIcon
	// Initialize pieceIcon arrays with graphic files
	private void initImageIcon() {
		pieceIcon = new ImageIcon[NUMPIECESTYLE];
		for (int i = 0; i < NUMPIECESTYLE; i++) {
			pieceIcon[i] = new ImageIcon(iconFile[i]);
		}
	}


	// initSlots
	// initialize the array of JLabels
	private void initSlots() {
		slots = new JLabel[NUMROW][NUMCOL];
		for (int i = 0; i < NUMROW; i++) {
			for (int j = 0; j < NUMCOL; j++) {
				slots [i] [j] = new JLabel ();
				slots[i][j].setPreferredSize(new Dimension(PIECESIZE, PIECESIZE));
				slots [i] [j].setHorizontalAlignment (SwingConstants.CENTER);      
			}
		}
	}


	// createPlayPanel
	private JPanel createPlayPanel() {
		JPanel panel = new JPanel(); 
		panel.setPreferredSize(new Dimension(PLAYPANEWIDTH, PLAYPANEHEIGHT));
		panel.setBackground(BACKGROUNDCOLOUR);
		panel.setLayout(new GridLayout(NUMROW, NUMCOL));
		for (int i = 0; i < NUMROW; i++) {
			for (int j = 0; j < NUMCOL; j++) {
				panel.add(slots[i][j]);
			}
		}
		return panel;    
	}


	// createInfoPanel
	private JPanel createInfoPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(INFOPANEWIDTH, INFOPANEHEIGHT));
		panel.setBackground (BACKGROUNDCOLOUR);
		panel.setBorder (new LineBorder (Color.white)); 

		Font headingFont = new Font ("Arial", Font.BOLD, 24);
		Font regularFont = new Font ("Arial", Font.BOLD, 20);
		Font buttonFont = new Font ("Arial", Font.BOLD, 18);

		// Create a panel for the scoreboard
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
		scorePanel.setBackground(BACKGROUNDCOLOUR);

		// Create the label to display "Score" heading
		JLabel scoreLabel = new JLabel ("     Score     ", JLabel.CENTER);
		scoreLabel.setFont(headingFont);
		scoreLabel.setForeground(Color.DARK_GRAY);
		scoreLabel.setAlignmentX (Component.CENTER_ALIGNMENT);
		//nextLabel.setForeground(Color.white);

		score = new JTextField();
		score.setFont(regularFont);
		score.setText("0");
		score.setForeground(Color.DARK_GRAY);
		score.setEditable(false);
		score.setHorizontalAlignment (JTextField.CENTER);
		score.setBackground(BACKGROUNDCOLOUR);

		scorePanel.add(scoreLabel);
		scorePanel.add(score);

		JPanel moveLeftPanel = new JPanel();
		moveLeftPanel.setLayout(new BoxLayout(moveLeftPanel, BoxLayout.Y_AXIS));
		moveLeftPanel.setBackground(BACKGROUNDCOLOUR);

		// Create the label to display "Moves Left" heading
		JLabel moveLeftLabel = new JLabel ("Moves Left", JLabel.CENTER);
		moveLeftLabel.setFont(headingFont);
		moveLeftLabel.setForeground(Color.DARK_GRAY);
		moveLeftLabel.setAlignmentX (Component.CENTER_ALIGNMENT);

		numMoveLeft = new JTextField();
		numMoveLeft.setFont(regularFont);
		numMoveLeft.setText("0");
		numMoveLeft.setForeground(Color.DARK_GRAY);
		numMoveLeft.setEditable(false);
		numMoveLeft.setHorizontalAlignment (JTextField.CENTER);
		numMoveLeft.setBackground(BACKGROUNDCOLOUR);

		JLabel emptyLabel1 = new JLabel (" ", JLabel.CENTER);
		emptyLabel1.setFont(headingFont);
		emptyLabel1.setAlignmentX (Component.CENTER_ALIGNMENT); 

		JLabel emptyLabel2 = new JLabel (" ", JLabel.CENTER);
		emptyLabel2.setFont(headingFont);
		emptyLabel2.setAlignmentX (Component.CENTER_ALIGNMENT);

		moveLeftPanel.add(emptyLabel1);
		moveLeftPanel.add(moveLeftLabel);
		moveLeftPanel.add(numMoveLeft);
		moveLeftPanel.add(emptyLabel2);

		hintButton = new JButton("Hint");
		hintButton.setFont(buttonFont);
		hintButton.setForeground(Color.DARK_GRAY);

		endGameButton = new JButton("End Game");
		endGameButton.setFont(buttonFont);
		endGameButton.setForeground(Color.DARK_GRAY);

		panel.add(scorePanel);
		panel.add(moveLeftPanel);
		panel.add(hintButton);
		panel.add(endGameButton);

		return panel;
	}


	// createMainFrame
	private void createMainFrame() {
		// Create the main Frame
		mainFrame = new JFrame ("Bejeweled");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = (JPanel)mainFrame.getContentPane();
		panel.setLayout (new BoxLayout(panel,BoxLayout.Y_AXIS));

		// Create the panel for the logo
		JPanel logoPane = new JPanel();
		logoPane.setPreferredSize(new Dimension (LOGOWIDTH, LOGOHEIGHT));
		logoPane.setBackground (BACKGROUNDCOLOUR);

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(logoIcon));
		logoPane.add(logo);

		// Create the bottom Panel which contains the play panel and info Panel
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BoxLayout(bottomPane,BoxLayout.X_AXIS));
		bottomPane.setPreferredSize(new Dimension(PLAYPANEWIDTH + INFOPANEWIDTH, PLAYPANEHEIGHT));
		bottomPane.add(createPlayPanel());
		bottomPane.add(createInfoPanel());

		// Add the logo and bottom panel to the main frame
		panel.add(logoPane);
		panel.add(bottomPane);

		mainFrame.setContentPane(panel);
		// mainFrame.setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		mainFrame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
		mainFrame.setVisible(true);
	}


	/**
	* Returns the column number of where the given JLabel is on
	* 
	* @param  label the label whose column number to be requested
	* @return the column number
	*/
	public int getRow(JLabel label) {
		int result = -1;
		for (int i = 0; i < NUMROW && result == -1; i++) {
			for (int j = 0; j < NUMCOL && result == -1; j++) {
				if (slots[i][j] == label) {
					result = i;
				}
			}
		}
		return result;
	}


	/**
	* Returns the column number of where the given JLabel is on
	* 
	* @param  label the label whose column number to be requested
	* @return the column number
	*/
	public int getColumn(JLabel label) {
		int result = -1;
		for (int i = 0; i < NUMROW && result == -1; i++) {
			for (int j = 0; j < NUMCOL && result == -1; j++) {
				if (slots[i][j] == label) {
					result = j;
				}
			}
		}
		return result;
	}


	public JButton getHintButton () {
		return hintButton;
	}


	public void addListener (BejeweledListener listener) {
		// add listener for each slot on the game board
		for (int i = 0; i < NUMROW; i++) {
			for (int j = 0; j < NUMCOL; j++) {
				slots [i] [j].addMouseListener (listener);
			}
		}

		// add listener for the button
		endGameButton.addMouseListener(listener);
		hintButton.addMouseListener(listener);
	}


	/**
	* Display the specified player icon on the specified slot
	* 
	* @param row row of the slot
	* @param col column of the slot
	* @param piece index of the piece to be displayed
	*/
	public void setPiece(int row, int col, int piece) {
		slots[row][col].setIcon(pieceIcon[piece]);
	}


	/**
	* Highlight the specified slot with the specified colour
	* 
	* @param row row of the slot
	* @param col column of the slot
	* @param colour colour used to highlight the slot
	*/
	public void highlightSlot(int row, int col, Color colour) {
		slots[row][col].setBorder (new LineBorder (colour));   
	}


	/**
	* Unhighlight the specified slot to the default grid colour
	* 
	* @param row row of the slot
	* @param col column of the slot
	*/
	public void unhighlightSlot(int row, int col) {
		slots[row][col].setBorder (new LineBorder (BACKGROUNDCOLOUR));   
	}


	/**
	* Display the score on the corresponding textfield
	* 
	* @param point the score to be displayed
	*/
	public void setScore(int point) {
		score.setText(point+"");
	}


	/**
	* Display the number of moves left on the corresponding textfield
	* 
	* @param num number of moves left to be displayed
	*/
	public void setMoveLeft(int num) {
		numMoveLeft.setText(num+"");
	}	


	/**
	* Reset the game board (clear all the pieces on the game board)
	* 
	*/
	public void resetGameBoard() {
		for (int i = 0; i < NUMROW; i++) {
			for (int j = 0; j < NUMCOL; j++) {
				slots[i][j].setIcon(null);
			}
		}
	}


	public void showInvalidSwapMessage () {
		JOptionPane.showMessageDialog(null, "That move does not result in any chain\nformations!", "Invalid Move!", JOptionPane.ERROR_MESSAGE, null); 
	}


	/**
	* Display a pop up window displaying the message about invalid move
	* 
	*/
	public void showInvalidMoveMessage () {
		JOptionPane.showMessageDialog(null, "That move is invalid! Selected pieces must be\nadjacent to one aother.", "Invalid Move!", JOptionPane.ERROR_MESSAGE, null); 
	}


	public void showHintMessage (int row, int column) {
		highlightSlot(row, column, Color.RED);
		JOptionPane.showMessageDialog(null, "Chansey says: \"Here's a hint!\"", "Hint!", JOptionPane.INFORMATION_MESSAGE, messageIcon);
		unhighlightSlot(row, column);
	}


	/**
	* Display a pop up window specifying the size of the chain(s) that is (are) formed after the swap
	* 
	* @param chainSize the size of the chain(s) that is (are) formed
	*/
	public void showChainSizeMessage(int chainSize){
		JOptionPane.showMessageDialog(null, "Chansey says: \"Chain(s) formed! You\nearned " + chainSize + " points.\"", "Chain Formed!", JOptionPane.INFORMATION_MESSAGE, messageIcon); 
	}


	public void showChainReactionMessage() {
		JOptionPane.showMessageDialog(null, "Chansey says: \"Chain(s) formed by\nfalling stones!\"", "Chain Reaction!", JOptionPane.INFORMATION_MESSAGE, messageIcon); 
	}


	public void showOutOfMovesMessage (int point) {
		JOptionPane.showMessageDialog(null, "Chansey says: \"Out of moves! You\nended the game with " + point + " points.\nThanks for playing!\"", "Game over!", JOptionPane.INFORMATION_MESSAGE, messageIcon);
		System.exit(0);
	}


	public void showNoMoreMovesMessage (int point, int numMove) {
		JOptionPane.showMessageDialog(null, "Chansey says: \"No more possible\nmoves. You ended the game with\n" + point + " points after " + numMove + " moves. Thanks\nfor playing!\"", "Game over!", JOptionPane.INFORMATION_MESSAGE, messageIcon);
		System.exit(0);
	}


	/**
	* Display a pop up window specifying the game is over with the score and number of moves used
	* 
	* @param point the score earned in the game
	* @param numMove the number of moves used in the game
	*/
	public void showGameOverMessage(int point, int numMove){
		JOptionPane.showMessageDialog(null, "Chansey says: \"You got " + point + " points with\n" + numMove + " moves. Thanks for playing!\"", "Game Over!", JOptionPane.INFORMATION_MESSAGE, messageIcon); 
		System.exit(0);
	}
}