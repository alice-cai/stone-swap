/**
* Bejeweled.java
*
* This class represents a Bejeweled (TM) game, which allows the player to make moves
* by swapping two pieces. Chains formed after valid moves disappear and the pieces on
* top fall to fill in the gaps. New, random pieces fill in the empty slots. The game
* ends when the player runs out of moves or when there are no more possible moves.
*/

import java.awt.Color;

public class Bejeweled {
	final Color SELECT_COLOUR = Color.GRAY;
	final Color CHAIN_COLOUR = Color.DARK_GRAY;
	final Color HINT_COLOUR = Color.RED;

	final int MIN_CHAIN_LENGTH = 3;		// minimum size required to form a chain
	final int NUMMOVE = 20;				// number of moves to be play in one game
	final int EMPTY = -1;				// represents a slot on the game board where a piece has disappeared

	final int NUMPIECESTYLE;	// number of different piece styles
	final int NUMROW;			// number of rows in the game board
	final int NUMCOL;			// number of columns in the game board

	BejeweledGUI gui;			// the object referring to the GUI; used when calling methods to update the GUI

	int board[][];				// the 2D array representing the current content of the game board

	boolean firstSelection;		// boolean indicating whether the player is about to select the first piece
	int slot1Row, slot1Col;		// stores the location of the first selection

	int totalChainLength;		// total length of the current chain
	int score;					// current score of the game
	int numMoveLeft;			// number of move left for the game
	boolean findingValidMove;

	public Bejeweled(BejeweledGUI gui) {
		this.gui = gui;
		NUMPIECESTYLE = gui.NUMPIECESTYLE;
		NUMROW = gui.NUMROW;
		NUMCOL = gui.NUMCOL;
		board = new int[NUMROW][NUMCOL];

		score = 0;
		numMoveLeft = NUMMOVE;
		gui.setMoveLeft(NUMMOVE);
		totalChainLength = 0;
		firstSelection = true;
		findingValidMove = false;
		initializeBoard();
		removeExistingChains(false);
	}

	/**
	* initializeBoard
	* This method fills the game board with random pieces.
	*/
	public void initializeBoard () {
		for (int row = 0; row < NUMROW; row++) {
			for (int col = 0; col < NUMCOL; col++) {
				int pieceIndex = (int)(Math.random() * NUMPIECESTYLE);
				board[row][col] = pieceIndex;
				gui.setPiece(row, col, pieceIndex);
			}
		}
	}

	/**
	* play
	* This method is called when a piece is clicked. Parameters "row" and "column"
	* indicate the location of the piece that is clicked by the player.
	*/
	public void play (int row, int column) {
		if (firstSelection) {
			slot1Row = row;
			slot1Col = column;
			gui.highlightSlot(row, column, SELECT_COLOUR);
			firstSelection = false;
		} else if (slot1Row == row && slot1Col == column) {
			gui.unhighlightSlot(slot1Row, slot1Col);
			firstSelection = true;
		} else {
			gui.unhighlightSlot(slot1Row, slot1Col);
			if (!checkAdjacentPieces(row, column)) {
				gui.showInvalidMoveMessage();
			} else {
				swapWithFirstSelection(row, column, true);

				if (checkChainFormation(row, column)) {
					score += totalChainLength;
					gui.showChainSizeMessage(totalChainLength);
					gui.setScore(score);
					numMoveLeft--;
					gui.setMoveLeft(numMoveLeft);
					fillEmptySlots();
					removeExistingChains(true);
					totalChainLength = 0;
				} else {
					gui.showInvalidSwapMessage();
					swapWithFirstSelection(row, column, true);
				}
			}
			firstSelection = true;
			if (numMoveLeft <= 0) {
				gui.showOutOfMovesMessage(score);
			}
			if (!findPossibleMoves(false)) {
				gui.showNoMoreMovesMessage(score, NUMMOVE - numMoveLeft);
			}
		}
	}

	/**
	 * checkChainFormation
	 * Checks for horizontal and vertical chain formation for the first piece selected
	 * by the user and the argument piece. If a chain is found, return true. Otherwise,
	 * return false.
	 */
	private boolean checkChainFormation (int row, int column) {
		int piece1 = board[row][column];
		int piece2 = board[slot1Row][slot1Col];

		boolean verticalChainFound1 = checkVerticalChain(row, column, piece1);
		boolean horizontalChainFound1 = checkHorizontalChain(row, column, piece1);
		if (verticalChainFound1 || horizontalChainFound1) {
			totalChainLength++;
		}
		boolean verticalChainFound2 = checkVerticalChain(slot1Row, slot1Col, piece2);
		boolean horizontalChainFound2 = checkHorizontalChain(slot1Row, slot1Col, piece2);
		if (verticalChainFound2 || horizontalChainFound2) {
			totalChainLength++;
		}

		if (horizontalChainFound1 || verticalChainFound1 || horizontalChainFound2 || verticalChainFound2) {
			return true;
		}
		return false;
	}

	/**
	 * findPossibleMoves
	 * This method checks all possible moves. As soon as it finds a move that results
	 * in a chain, it returns true. If it can't find any valid moves, it returns false.
	 *
	 * @param displayHint - Indicates whether or not a hint should be displayed. If
	 * set to true, this method calls the showHintMessage method in BejeweledGUI.
	 */
	private boolean findPossibleMoves (boolean displayHint) {
		findingValidMove = true;

		for(int row = 0; row < NUMROW; row++) {
			for (int col = 0; col < NUMCOL; col++) {
				slot1Row = row;
				slot1Col = col;

				// Check if swapping the current piece left or right results in any chain formations.
				// If so, return true.
				for(int i = -1; i <= 1; i += 2) {
					int newRow = row + i;
					if (newRow >= 0 && newRow < NUMROW) {
						swapWithFirstSelection (newRow, col, false);
						boolean foundValidMove = checkHorizontalChain(newRow, col, board[newRow][col]) || checkVerticalChain(newRow, col, board[newRow][col]);
						swapWithFirstSelection (newRow, col, false);
						if (foundValidMove) {
							if (displayHint) {
								gui.showHintMessage(slot1Row, slot1Col);
							}
							totalChainLength = 0;
							findingValidMove = false;
							return true;
						}
					}
				}

				// Check if swapping the current piece up or down results in any chain formations.
				// If so, return true;
				for(int i = -1; i <= 1; i += 2) {
					int newCol = col + i;
					if (newCol >= 0 && newCol < NUMCOL) {
						swapWithFirstSelection (row, newCol, false);
						boolean foundValidMove = checkHorizontalChain(row, newCol, board[row][newCol]) || checkVerticalChain(row, newCol, board[row][newCol]);
						swapWithFirstSelection (row, newCol, false);
						if (foundValidMove) {
							if (displayHint) {
								gui.showHintMessage(slot1Row, slot1Col);
							}
							totalChainLength = 0;
							findingValidMove = false;
							return true;
						}
					}
				}
			}
		}
		totalChainLength = 0;
		findingValidMove = false;
		return false;
	}

	/**
	* removeExistingChains
	* This method keeps calling the findExistingChains method until it cannot
	*/
	private void removeExistingChains (boolean gameInProgress) {
		do {
			totalChainLength = 0;
			findExistingChains();
			if (gameInProgress && totalChainLength != 0) {
				gui.showChainReactionMessage();
				score += totalChainLength;
				gui.setScore(score);
			}
			fillEmptySlots();
		} while (totalChainLength != 0);
	}

	/**
	* findExistingChains
	* This method traverses the game board looking for existing chains. Once
	* it finds one, it stops looking.
	*/
	private void findExistingChains () {
		for(int row = 0; row < NUMROW; row++) {
			for (int col = 0; col < NUMCOL; col++) {
				int piece = board[row][col];
				if (piece != EMPTY) {
					boolean foundVerticalChain = checkVerticalChain(row, col, piece);
					boolean foundHorizontalChain = checkHorizontalChain(row, col, piece);
					if (foundVerticalChain || foundHorizontalChain) {
						totalChainLength++;
					}
				}
			}
		}
	}

	/*
	private int[][] copyBoard () {
		int[][] boardCopy = new int[NUMROW][NUMCOL];

		for(int row = 0; row < NUMROW; row++) {
			for (int col = 0; col < NUMCOL; col++) {
				boardCopy[row][col] = board[row][col];
			}
		}
		return boardCopy;
	}*/

	/**
	* checkAdjacentPieces
	* This method checks if the indicated piece is adjacent to the piece
	* that was first selected by the user. Returns true if adjacent and
	* false otherwise.
	*/
	private boolean checkAdjacentPieces (int row, int column) {
		int rowDiff = Math.abs(slot1Row-row);
		int colDiff = Math.abs(slot1Col-column);

		return (rowDiff == 0 && colDiff == 1) || (rowDiff == 1 && colDiff == 0);
	}

	/**
	* swapWithFirstSelection
	* This method swaps the indicated piece with the first piece that was
	* selected by the user.
	*/
	private void swapWithFirstSelection (int row, int column, boolean showONGUI) {
		int temp = board[slot1Row][slot1Col];
		board[slot1Row][slot1Col] = board[row][column];
		board[row][column] = temp;
		if (showONGUI) {
			gui.setPiece(slot1Row, slot1Col, board[slot1Row][slot1Col]);
			gui.setPiece(row, column, board[row][column]);
		}
	}

	/**
	* checkHorizontalChain
	* This method takes in the indicies of a piece on the board. It calculates
	* the length of the horizontal chain that the specified piece is part of.
	* If the length is greater than or equal to MIN_CHAIN_LENGTH, the entire
	* chain is highlighted and set to be EMPTY (indicating that it should be
	* removed from the board).
	*/
	private boolean checkHorizontalChain (int row, int column, int pieceIndex) {
		int startChainIndex = findStartOfHorizontalChain(row, column, pieceIndex);
		int endChainIndex = findEndOfHorizontalChain(row, column, pieceIndex);
		int chainLength = endChainIndex - startChainIndex + 1;

		if (chainLength >= MIN_CHAIN_LENGTH) {
			totalChainLength += chainLength - 1; // excludes current piece
			if (!findingValidMove) {
				for(int i = startChainIndex; i <= endChainIndex; i++) {
					board[row][i] = EMPTY;
					gui.highlightSlot(row, i, CHAIN_COLOUR);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	* findStartOfHorizontalChain
	* This method looks at the pieces to the left of the specified square
	* and returns the column index of the leftmost piece in the chain.
	*/
	private int findStartOfHorizontalChain (int row, int column, int pieceIndex) {
		int startChainIndex = 0;
		boolean exit = false;

		for (int i = column-1; i >= 0 && !exit; i--) {
			// Check if the current square has the correct piece. If so,
			// keep looping. If not, set endChainIndex to the previous
			// square and exit the loop.
			if (board[row][i] == pieceIndex) {
				continue;
			} else {
				startChainIndex = i + 1;
				exit = true;
			}
		}

		return startChainIndex;
	}

	/**
	* findEndOfHorizontalChain
	* This method looks at the pieces to the right of the specified square
	* and returns the column index of the rightmost piece in the chain.
	*/
	private int findEndOfHorizontalChain (int row, int column, int pieceIndex) {
		int endChainIndex = NUMCOL - 1;
		boolean endOfChain = false;

		for (int i = column+1; i < NUMCOL && !endOfChain; i++) {
			// Check if the current square has the correct piece. If so,
			// keep looping. If not, set endChainIndex to the previous
			// square and exit the loop.
			if (board[row][i] == pieceIndex) {
				continue;
			} else {
				endChainIndex = i-1;
				endOfChain = true;
			}
		}

		return endChainIndex;
	}


	/**
	* checkVerticalChain
	* This method takes in the indicies of a piece on the board. It calculates
	* the length of the vertical chain that the specified piece is part of.
	* If the length is greater than or equal to MIN_CHAIN_LENGTH, the entire
	* chain is highlighted and set to be EMPTY (indicating that it should be
	* removed from the board).
	*/
	private boolean checkVerticalChain (int row, int column, int pieceIndex) {
		int startChainIndex = findStartOfVerticalChain(row, column, pieceIndex);
		int endChainIndex = findEndOfVerticalChain(row, column, pieceIndex);
		int chainLength = endChainIndex - startChainIndex + 1;

		if (chainLength >= MIN_CHAIN_LENGTH	) {
			totalChainLength += chainLength - 1; // excludes current piece
			if (!findingValidMove) {
				for (int i = startChainIndex; i <= endChainIndex; i++) {
					board[i][column] = EMPTY;
					gui.highlightSlot(i, column, CHAIN_COLOUR);
				}
			}
			return true;
		} else {
			return false;
		}
	}


	/**
	* findStartOfVerticalChain
	* This method looks at the pieces directly above the specified square and
	* returns the row index of the highest piece in the chain.
	*/
	private int findStartOfVerticalChain (int row, int column, int pieceIndex) {
		int startChainIndex = 0;
		boolean exit = false;

		for (int i = row-1; i >= 0 && !exit; i--) {
			// Check if the current square has the correct piece. If so,
			// keep looping. If not, set endChainIndex to the previous
			// square and exit the loop.
			if (board[i][column] == pieceIndex) {
				continue;
			} else {
				startChainIndex = i + 1;
				exit = true;
			}
		}

		return startChainIndex;
	}

	/**
	* findEndOfVerticalChain
	* This method looks at the pieces directly below the specified square and
	* returns the row index of the lowest piece in the chain.
	*/
	private int findEndOfVerticalChain (int row, int column, int pieceIndex) {
		int endChainIndex = NUMROW - 1;
		boolean exit = false;

		for (int i = row+1; i < NUMROW && !exit; i++) {
			// Check if the current square has the correct piece. If so,
			// keep looping. If not, set endChainIndex to the previous
			// square and exit the loop.
			if (board[i][column] == pieceIndex) {
				continue;
			} else {
				endChainIndex = i-1;
				exit = true;
			}
		}

		return endChainIndex;
	}

	/**
	* fillEmptySlots
	* This method fills empty spots in the game board by having existing pieces
	* on top "fall" to fill in the gaps. Randomly generated pieces are added at
	* the top of the board.
	*/
	private void fillEmptySlots () {
		for (int column = 0; column < NUMCOL; column++) {
			for (int row = 0; row < NUMROW; row++) {
				if (board[row][column] == EMPTY) {
					gui.unhighlightSlot(row, column);
					for (int i = row; i > 0; i--) {
						board[i][column] = board[i-1][column];
						gui.setPiece(i, column, board[i][column]);
					}
					board[0][column] = (int)(Math.random() * NUMPIECESTYLE);
					gui.setPiece(0, column, board[0][column]);
				}
			}
		}
	}

	/**
	* displayHint
	* This method is called when the player clicks on the "Hint" button. This
	* method displays a hint by calling the findPossibleMoves method, passing
	* in true as the argument.
	*/
	public void displayHint() {
		findPossibleMoves(true);
	}

	/**
	* endGame
	* This method is called when the player clicks on the "End Game" button.
	*/
	public void endGame() {
		gui.showGameOverMessage(score, NUMMOVE - numMoveLeft);
	}
}