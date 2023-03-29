package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * 
 */
public class Checkers extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  //make a checkersboard object
  CheckersBoard board; 
  
//make game button 
  private Button newGameButton; 

  //surrender button
  private Button surrenderButton; 
 // create a label
  private Label message; 

 
  //start method that sets stage and displays the ui buttons
  public void start(Stage stage) {

    message = new Label("Click \"New Game\" to begin.");
    message.setTextFill(Color.rgb(121, 78, 40)); // Light green.
    message.setFont(Font.font(null, FontWeight.BOLD, 18));

    
    newGameButton = new Button("New Game!");
    surrenderButton = new Button("Surrender");

    board = new CheckersBoard(); 
    board.drawBoard(); 
    
    //actionEvent handler for buttons
    newGameButton.setOnAction(e -> board.doNewGame());
    surrenderButton.setOnAction(e -> board.doSurrender());
    board.setOnMousePressed(e -> board.mousePressed(e));
    //setting the buttons on the screen
    board.relocate(20, 20);
    newGameButton.relocate(370, 120);
    surrenderButton.relocate(370, 200);
    message.relocate(20, 370);
    // sizing the buttons 
    surrenderButton.setManaged(false);
    surrenderButton.resize(100, 30);
    newGameButton.setManaged(false);
    newGameButton.resize(100, 30);

   //create pane and set size
    Pane root = new Pane();

    root.setPrefWidth(500);
    root.setPrefHeight(420);
    
    //changing window icon
	Image logo = new Image("Logo.png");
	stage.getIcons().add(logo);

	//setting colors and title
    root.getChildren().addAll(board, newGameButton, surrenderButton, message);
    root.setStyle("-fx-background-color: burlywood; "
        + "-fx-border-color: darksalmon; -fx-border-width:3");
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle("Checker Game!");
    stage.show();

  } // end start()
  
  public class CheckersBoard extends Canvas {
	  // special object
	  MySpecialSpot special;
	  //board data and list of allowed moves
	  GameData board; 
	  //check if game is in progress
	  boolean gameInProgress; 
	  /* The next three variables are valid only when the game is in progress. */
	  //checks whose turn
	  int currentPlayer; 
	  int selectedRow, selectedCol; 
	  //array of allowed moves
	  CheckersMovement[] legalMoves; 

	  //constructor that initillizes new GameDataand the function doNewGame starts a game 
	  CheckersBoard() {
	    super(324, 324); // canvas is 324-by-324 pixels
	    board = new GameData();	      
	    doNewGame();
	  }

	 //start new game method 
	 public  void doNewGame() {
		//random special tile to skip opponent turn
		Random random = new Random(); 
		
		int tileRow = random.nextInt(2) + 3; // generates a random integer between 0 and 1, 
		//then adds 4 to get a number between 4 and 3(to decide between the 2 middle rows) 
		
		// Generate a random number between 0 and 7 for a random column
		int tileCol = random.nextInt(8);
				 
		//the tile has to be odd in row 4 because the pieces can only move in the odd column 
		//of that row, and the tile has to be even in row 4 because of the same reason
				 
		if(tileCol == 7 && tileRow == 4 ) {//to keep it within range
			tileCol--;
		}
				 
		else if (tileCol % 2 != 0 && tileRow == 4) {//make sure it is odd in row 4
			tileCol++;
		}

				 
		else if (tileCol % 2 == 0 && tileRow == 3) {//make sure it is even in row 3
			tileCol++;
		}

		 
		//so here it chooses randomly between the middle rows, then a random column in that row where
		//the piece can move
		special=new MySpecialSpot(tileRow,tileCol);
	    board.initialize(); // Set up the pieces.
	    currentPlayer = GameData.BEIGE; // Beige moves first.
	    legalMoves = board.getValidMoves(GameData.BEIGE); // Get Beige's legal moves.
	    selectedRow = -1; // Beige has not yet selected a piece to move.
	    message.setText("Beige:  Make your move.");
	    gameInProgress = true;
	    newGameButton.setDisable(false);
	    surrenderButton.setDisable(false);
	    drawBoard();
	  }

	 //surrender method
	 public void doSurrender() {
	      gameOver(currentPlayer == GameData.BEIGE ? "Beige Surrenders.  SaddleBrown wins." : "SaddleBrown Surrenders.  Beige wins.");
	  }
	 // gameover method
	public  void gameOver(String str) {
	    message.setText(str);
	    newGameButton.setDisable(false);
	    surrenderButton.setDisable(true);
	    gameInProgress = false;
	  }
	
		// handles the click of a tile and checks of piece can be moved
	 public  void doClickSquare(int row, int col) {

		 //checks if the piece is allowed to make a move 
		 for (int i = 0; i < legalMoves.length; i++) {
		    if (legalMoves[i].getStartRow() == row && legalMoves[i].getStartCol() == col) {
		      selectedRow = row;
		      selectedCol = col;
		      if (currentPlayer == GameData.BEIGE)
		        message.setText("Beige:  Make your move.");
		      else
		        message.setText("SaddleBrown:  Make your move.");
		      drawBoard();
		      return;
		    }
		    else if (selectedRow >= 0 && legalMoves[i].getStartRow() == selectedRow && legalMoves[i].getStartCol() == selectedCol && legalMoves[i].getEndRow() == row && legalMoves[i].getEndCol() == col) {
		    	//	make move and return	    
		    	doMakeMove(legalMoves[i]);
		      return;
		    }
		  }

	  //if no piece is selected prompt user to move

	    if (selectedRow < 0) {
	      message.setText("Click the piece you want to move.");
	      return;
	    }
	  	    for (int i = 0; i < legalMoves.length; i++) {
	    	  if (legalMoves[i].getStartRow() == selectedRow && legalMoves[i].getStartCol() == selectedCol
	    	      && legalMoves[i].getEndRow() == row && legalMoves[i].getEndCol() == col) {
	    	    doMakeMove(legalMoves[i]);
	    	    break;
	    	  }
	    	}


	   //error message
	    message.setText("Click the square you want to move to.");

	  } 

	 	//move method 
	  public void doMakeMove(CheckersMovement move) {

	    board.makeMove(move);
	    //check if another jump is allowed
	    int currentPlayerColor = currentPlayer;
	    int otherPlayerColor = (currentPlayer == GameData.BEIGE) ? GameData.SADDLEBROWN : GameData.BEIGE;

	    if (move.isLongJump()) {
	      legalMoves = board.getLegalJumps(currentPlayer, move.getEndRow(), move.getEndCol());
	      if (legalMoves != null) {
	        message.setText(currentPlayerColor + ":  You must continue jumping.");
	        selectedRow = move.getEndRow(); // Since only one piece can be moved, select it.
	        selectedCol = move.getEndCol();
	        drawBoard();
	        return;
	      }
	    }

	    // special spot that skips opponents turn and gives another turn to the currentPlayer
	    if(special.isSpecial(move.getEndRow(), move.getEndCol())) {
	    	  message.setText(currentPlayerColor + ":  You land special spot you need to keep moving.");
	    	  selectedRow = move.getEndRow(); // Since only one piece can be moved, select it.
		      selectedCol = move.getEndCol();
		      legalMoves = board.getValidMoves(currentPlayer);
		      drawBoard();
		      return;
	    }
	    //change players
	    currentPlayer = otherPlayerColor;
	    legalMoves = board.getValidMoves(currentPlayer);
	    if (legalMoves == null)
	      gameOver(otherPlayerColor + " has no moves.  " + currentPlayerColor + " wins.");
	    else if (legalMoves[1].isLongJump())
	      message.setText(otherPlayerColor + ":  Make your move.  You must jump.");
	    else
	      message.setText(otherPlayerColor + ":  Make your move.");


	    //user did not select piece yet

	    selectedRow = -1;

	   //if the same piece left auto select it

	    if (legalMoves != null &&
	    	    Arrays.stream(legalMoves).allMatch(m -> m.getStartRow() == legalMoves[0].getStartRow() &&
	    	                                         m.getStartCol() == legalMoves[0].getStartCol())) {
	    	  selectedRow = legalMoves[0].getStartRow();
	    	  selectedCol = legalMoves[0].getStartCol();
	    	}
	   //redraw the board
	    drawBoard();

	  }

	  
	  //drawBoard draws the current checker board and the moves that are allowed
	  public void drawBoard() {

	    GraphicsContext g = getGraphicsContext2D();
	    g.setFont(new Font("Condensed", 12));

	    //Draws the Dark Red border 
	    g.setStroke(Color.DARKRED);
	    g.setLineWidth(2);
	    g.strokeRect(1, 1, 322, 322);

	    //Draws the tiles and pieces 
	    for (int row = 0; row < 8; row++) {
	    	  for (int col = 0; col < 8; col++) {
	    	    Color fillColor;
	    	    if (row % 2 == col % 2)
	    	      fillColor = Color.LIGHTGRAY;
	    	    else
	    	      fillColor = Color.GRAY;
	    	    if(special.getCol()==col && special.getRow()==row) {
	    	    	fillColor=Color.YELLOW;
	    	    }
	    	    g.setFill(fillColor);
	    	    g.fillRect(2 + col * 40, 2 + row * 40, 40, 40);
	    	    switch (board.getPieceAt(row, col)) {
	    	      case GameData.BEIGE:
	    	        g.setFill(Color.BEIGE);
	    	        g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
	    	        break;
	    	      case GameData.SADDLEBROWN:
	    	        g.setFill(Color.SADDLEBROWN);
	    	        g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
	    	        break;
	    	      case GameData.BEIGE_KING:
	    	        g.setFill(Color.BEIGE);
	    	        g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
	    	        g.setFill(Color.WHITE);
	    	        g.fillText("King", 15 + col * 40, 29 + row * 40);
	    	        break;
	    	      case GameData.SADDLEBROWN_KING:
	    	        g.setFill(Color.SADDLEBROWN);
		            g.fillOval(8 + col * 40, 8 + row * 40, 28, 28);
		            g.setFill(Color.WHITE);
		            g.fillText("King", 15 + col * 40, 29 + row * 40);
		            break;
	        }
	      }
	    }

	    
	    //highlights the legal moves when the game is in progress	
	    if (gameInProgress) {
	      g.setStroke(Color.FIREBRICK);
	      g.setLineWidth(4);
	      for (CheckersMovement move : legalMoves) {
	    	  g.strokeRect(4 + move.getStartCol() * 40, 4 + move.getStartRow() * 40, 36, 36);
	    	}

	      //if a piece is selected it draws a yellow border around the piece
	      //then draws a border where it can move
	      if (selectedRow >= 0) {
	        g.setStroke(Color.YELLOW);
	        g.setLineWidth(4);
	        g.strokeRect(4 + selectedCol * 40, 4 + selectedRow * 40, 36, 36);
	        g.setStroke(Color.GREEN);
	        g.setLineWidth(4);
	        for (int i = 0; i < legalMoves.length; i++) {
	          if (legalMoves[i].getStartCol() == selectedCol && legalMoves[i].getStartRow() == selectedRow) {
	            g.strokeRect(4 + legalMoves[i].getEndCol() * 40, 4 + legalMoves[i].getEndRow() * 40, 36, 36);
	          }
	        }
	      }
	    }

	  } 

	  /**
	   * Handles a mouse press event by determining which square on the chessboard
	   * was clicked, and calling the appropriate method to handle the click.
	   *
	   * @param evt The MouseEvent object that contains information about the event.
	   */
	  public void mousePressed(MouseEvent evt) {
	    if (gameInProgress == false)
	      message.setText("Click \"New Game\" to start a new game.");
	    else {
	      int col = (int) ((evt.getX() - 2) / 40);
	      int row = (int) ((evt.getY() - 2) / 40);
	      if (col >= 0 && col < 8 && row >= 0 && row < 8)
	        doClickSquare(row, col);
	    }
	  }

	} 

}
