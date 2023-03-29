package application;

/**
 * A CheckersMovement object represents a move in the game of Checkers.
 * It holds the starting and ending positions of the piece that is to be moved.
 * This class does not check if the move is legal
 */
public class CheckersMovement {
	// Starting position of piece to be moved.
    private int startRow, startCol; 
    // Ending position of the piece.
    private int endRow, endCol; 

    public CheckersMovement(int r1, int c1, int r2, int c2) {
        // Constructor sets the values of the instance variables.
        setStartRow(r1);
        setStartCol(c1);
        setEndRow(r2);
        setEndCol(c2);
    }
    
    // Test whether this move is a long jump. It is assumed that
    // the move is legal. In a long jump, the piece moves two
    // rows. In a regular move, it only moves one row.
    public boolean isLongJump() {
        return (getStartRow() - getEndRow() == 2 || getStartRow() - getEndRow() == -2);
    }

	/**
	 * @return the startRow
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * @param startRow the startRow to set
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * @return the endCol
	 */
	public int getEndCol() {
		return endCol;
	}

	/**
	 * @param endCol the endCol to set
	 */
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}

	/**
	 * @return the startCol
	 */
	public int getStartCol() {
		return startCol;
	}

	/**
	 * @param startCol the startCol to set
	 */
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	/**
	 * @return the endRow
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * @param endRow the endRow to set
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
}