package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameData {
  public static final int NONE = 0, BEIGE = 1, BEIGE_KING = 2, SADDLEBROWN = 3, SADDLEBROWN_KING = 4;
  private int[][] cells;

  /**
   * Constructor for the GameData class.
   * Initializes an 8x8 array of cells and sets up the board for a new game.
   */
  public GameData() {
    cells = new int[8][8];
    initialize();
  }

  /**
   * Initializes the board, placing pieces in their starting positions.
   * 
   */
  public void initialize() {
    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        // Calculate the parity of r and c.
        boolean isEvenCell = (r % 2 == c % 2);
        if (isEvenCell) {
          if (r < 3) {
            cells[r][c] = SADDLEBROWN;
          } else if (r > 4) {
            cells[r][c] = BEIGE;
          } else {
            cells[r][c] = NONE;
          }
        } else {
          cells[r][c] = NONE;
        }
      }
    }
  }

  /**
   * Returns the contents of the square at the specified row and column.
   *
   * @param r The row of the square.
   * @param c The column of the square.
   * @return The contents of the square at the specified row and column.
   */
  public int getPieceAt(int r, int c) {
    return cells[r][c];
  }
  
  void makeMove(CheckersMovement move) {
      make_move(move.getStartRow(), move.getStartCol(), move.getEndRow(), move.getEndCol());
  }

  /**
   * This function makes a move from the piece at (fromRow, fromCol) to (toRow,
   * toCol) on the game board.
   * It is assumed that this move is legal. If the move is a jump, the jumped
   * piece is removed from the board.
   * If a piece moves to the last row on the opponent's side of the board, the
   * piece becomes a king.
   * 
   * @param fromRow The row number of the piece to move.
   * @param fromCol The column number of the piece to move.
   * @param toRow   The row number where the piece should be moved.
   * @param toCol   The column number where the piece should be moved.
   * 
   */
  void make_move(int from_row, int from_col, int to_row, int to_col) {
    cells[to_row][to_col] = cells[from_row][from_col];
    cells[from_row][from_col] = NONE;
    if (from_row - to_row == 2 || from_row - to_row == -2) {
      // The move is a jump. Calculate the row and column of the jumped piece.
      int jump_row = (from_row + to_row) / 2;
      int jump_col = (from_col + to_col) / 2;
      // Remove the jumped piece from the board.
      cells[jump_row][jump_col] = NONE;
    }
    if (to_row == 0 && cells[to_row][to_col] == BEIGE) {
      // The move made the beige piece a king.
      cells[to_row][to_col] = BEIGE_KING;
    }
    if (to_row == 7 && cells[to_row][to_col] == SADDLEBROWN) {
      // The move made the saddlebrown piece a king.
      cells[to_row][to_col] = SADDLEBROWN_KING;
    }
    
  //special tiles
    Random random = new Random();
    int randomColKing = random.nextInt(8); // generates a random number from 0 to 7

  

    /* a random tile in the first row of movements will grant the king abilities, skip opponent, or land on mine.
         there is 50% chance that you will be granted the abilities because you can 
         only move in the light tiles, which are every other tile. 

    */
    if (to_row == 4 && to_col == randomColKing && cells[to_row][to_col] == BEIGE) {
        //the move made the Beige piece King
        cells[to_row][to_col] = BEIGE_KING;
      }

    if (to_row == 3 && to_col == randomColKing &&cells[to_row][to_col] == SADDLEBROWN) {
        // The move made the SaddleBrown piece a king.
        cells[to_row][to_col] = SADDLEBROWN_KING;
      }
  }

  /**
   * 
   * Returns an array of all legal moves for the given player on the current
   * board.
   * If the player has no legal moves, null is returned. If the player is not Beige
   * or SaddleBrown,
   * null is also returned. If a non-null value is returned, it will contain only
   * regular
   * moves or only jump moves, since a player can only make jump moves if they are
   * available.
   * 
   * @param player The player for whom to determine legal moves. This should be
   *               either Beige or SaddleBrown.
   * @return An array of legal moves for the given player, or null if the player
   *         is not Beige or SaddleBrown
   *         or if the player has no legal moves.
   */
  public CheckersMovement[] getValidMoves(int player) {
    if (player != BEIGE && player != SADDLEBROWN) {
      return null;
    }

    // Use a ternary operator to determine the value of pKing.
    int pKing = (player == BEIGE) ? BEIGE_KING : SADDLEBROWN_KING;

    List<CheckersMovement> moves = new ArrayList<>();
    // Determine legal moves and add them to the moves list
    // Return the list of moves as an array of CheckersMovement objects
    /*
     * This checks for any possible jumps on the checkers board.
     * It looks at each square on the board,
     * and if that square contains one of the player's pieces,
     * it looks at a possible jump in each of the four
     * directions from that square. If there is a legal jump in that
     * direction, it is added to the moves ArrayList.
     */

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (cells[r][c] == player || cells[r][c] == pKing) {
          if (canJump(player, r, c, r + 1, c + 1, r + 2, c + 2))
            moves.add(new CheckersMovement(r, c, r + 2, c + 2));
          if (canJump(player, r, c, r - 1, c + 1, r - 2, c + 2))
            moves.add(new CheckersMovement(r, c, r - 2, c + 2));
          if (canJump(player, r, c, r + 1, c - 1, r + 2, c - 2))
            moves.add(new CheckersMovement(r, c, r + 2, c - 2));
          if (canJump(player, r, c, r - 1, c - 1, r - 2, c - 2))
            moves.add(new CheckersMovement(r, c, r - 2, c - 2));
        }
      }
    }

    /*
     * This checks if any legal moves exist on the checkers board.
     * If no jump moves were found, this method checks each square on the board
     * to see if it contains one of the player's pieces. If a player's piece is
     * found, it looks at a possible move in each of the four directions from that
     * square.
     * If there is a legal move in that direction, it is added to the moves
     * ArrayList.
     */

    if (moves.isEmpty()) {
      for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
          if (cells[r][c] == player || cells[r][c] == pKing) {
            if (canMove(player, r, c, r + 1, c + 1))
              moves.add(new CheckersMovement(r, c, r + 1, c + 1));
            if (canMove(player, r, c, r - 1, c + 1))
              moves.add(new CheckersMovement(r, c, r - 1, c + 1));
            if (canMove(player, r, c, r + 1, c - 1))
              moves.add(new CheckersMovement(r, c, r + 1, c - 1));
            if (canMove(player, r, c, r - 1, c - 1))
              moves.add(new CheckersMovement(r, c, r - 1, c - 1));
          }
        }
      }
    }

    /*
     * if no legal moves are found return null, if there are legal moves
     * create an array to fit the all the legal moves.
     */

    if (moves.isEmpty()) {
      return null;
    } else {
      CheckersMovement[] legalMoves = moves.toArray(new CheckersMovement[0]);
      return legalMoves;
    }
  }

  /**
   * Returns a list of the legal jumps that the specified player can make starting
   * from the specified row and column.
   * If no such jumps are possible, null is returned.
   * 
   * @param player The player making the jump. Must be either Beige or SaddleBrown.
   * @param r      The row of the piece to move.
   * @param c      The column of the piece to move.
   * @return An array of CheckersMovement objects representing the legal jumps
   *         that the player can make,
   *         or null if no legal jumps are possible.
   */

  CheckersMovement[] getLegalJumps(int player, int r, int c) {
    if (player != BEIGE && player != SADDLEBROWN) {
      return null;
    }

    int pKing = (player == BEIGE) ? BEIGE_KING : SADDLEBROWN_KING;

    // Use a fixed-size array to store the legal jumps.
    // Set the initial size of the array to be the maximum
    // number of legal jumps that a piece can make.
    CheckersMovement[] moves = new CheckersMovement[4];
    int numJumps = 0;

    if (cells[r][c] == player || cells[r][c] == pKing) {
      if (canJump(player, r, c, r + 1, c + 1, r + 2, c + 2)) {
        moves[numJumps++] = new CheckersMovement(r, c, r + 2, c + 2);
      }
      if (canJump(player, r, c, r - 1, c + 1, r - 2, c + 2)) {
        moves[numJumps++] = new CheckersMovement(r, c, r - 2, c + 2);
      }
      if (canJump(player, r, c, r + 1, c - 1, r + 2, c - 2)) {
        moves[numJumps++] = new CheckersMovement(r, c, r + 2, c - 2);
      }
      if (canJump(player, r, c, r - 1, c - 1, r - 2, c - 2)) {
        moves[numJumps++] = new CheckersMovement(r, c, r - 2, c - 2);
      }
    }

    if (numJumps == 0) {
      return null;
    } else {
      // Trim the array to the actual number of legal jumps.
      CheckersMovement[] trimmedMoves = new CheckersMovement[numJumps];
      System.arraycopy(moves, 0, trimmedMoves, 0, numJumps);
      return trimmedMoves;
    }
  }

  /**
   * Returns true if the specified player can legally jump from (r1,c1) to
   * (r3,c3), jumping over (r2,c2).
   *
   * @param player The player making the jump. Must be either Beige or SaddleBrown.
   * @param r1     The row of the piece to move.
   * @param c1     The column of the piece to move.
   * @param r2     The row of the piece to jump over.
   * @param c2     The column of the piece to jump over.
   * @param r3     The row of the destination cell.
   * @param c3     The column of the destination cell.
   * @return true if the specified jump is legal, false otherwise.
   */
  private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

    // Check if (r3,c3) is off the board.
    if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8) {
      return false;
    }

    if (cells[r3][c3] != NONE) {
      return false; // (r3,c3) already contains a piece.
    }

    if (player == BEIGE) {
      if (cells[r1][c1] == BEIGE && r3 > r1) {
        return false; // Beige piece can only move up.
      }
      if (cells[r2][c2] != SADDLEBROWN && cells[r2][c2] != SADDLEBROWN_KING) {
        return false; // There is no SaddleBrown piece to jump.
      }
      return true; // The jump is legal.
    } else {
      if (cells[r1][c1] == SADDLEBROWN && r3 < r1) {
        return false; // SaddleBrown pieces can only move down.
      }
      if (cells[r2][c2] != BEIGE && cells[r2][c2] != BEIGE_KING) {
        return false; // There is no Beige piece to jump.
      }
      return true; // The jump is legal.
    }

  }

  /**
   * Returns true if the specified player can legally move from (r1,c1) to
   * (r2,c2).
   *
   * @param player The player making the move. Must be either Beige or SaddleBrown.
   * @param r1     The row of the piece to move.
   * @param c1     The column of the piece to move.
   * @param r2     The row of the destination cell.
   * @param c2     The column of the destination cell.
   * @return true if the specified move is legal, false otherwise.
   */
  private boolean canMove(int player, int r1, int c1, int r2, int c2) {

    // Return false if the destination cell is off the board.
    if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8) {
      return false;
    }

    // Return false if the destination cell already contains a piece.
    if (cells[r2][c2] != NONE) {
      return false;
    }

    // Return false if a regular Beige piece is trying to move up.
    // Return true if the move is legal.
    if (player == BEIGE) {
      if (cells[r1][c1] == BEIGE && r2 > r1) {
        return false;
      }
      return true;
    } else {
      // Return false if a regular SaddleBrown piece is trying to move down.
      // Return true if the move is legal.
      if (cells[r1][c1] == SADDLEBROWN && r2 < r1) {
        return false;
      }
      return true;
    }
  }
}

