package main;
import java.util.Scanner;

import userInterface.controller.FrameSwitcher;
import userInterface.view.IModel;
import userInterface.view.PanelListener;

/**
 * A singleton of the Checkers game.
 * 
 * @author james
 *
 */
public class Game{
	public static Game instance;
	private Board board = Board.getInstance();
	private Player blackPlayer, redPlayer;
	private int mode;
	private boolean gameOver;
	private Scanner input = new Scanner(System.in);
	
	private Game() {
		initialize();
		play();
	}
	
	private Game(int mode) {
		initialize(mode);
		play();
	}
	
	/**
	 * Accessor Method to return the Singleton instance of the game, or call the constructor if it does not exist.
	 * @return The instance of the Game.
	 */
	public static Game getInstance(){
		if(instance == null){
			instance = new Game();
		}
		return instance;
	}
	
	public static Game getInstance(int mode){
		if(instance == null){
			instance = new Game(mode);
		}
		return instance;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	/*
	 * Private method that will call the turn(Player aPlayer) method until the gameOver is true.
	 * Alternates between the Black and the Red player.
	 */
	private void play() {
		int turn = 1;
		while(!gameOver) {
			board.resetTurn();
			switch(turn) {
			case 0: turn(blackPlayer);
						turn +=1;
						break;
			case 1: turn(redPlayer);
						turn -=1;
						break;
			}
		}
	}
	
	
	/*
	 * Method to initialize the game, including the Board's initialization and setting up the Players.
	 * The Private play() method is called at the end, which will begin the game. The game will continue until a player loses.
	 */
	private void initialize(){
		System.out.println("\nWelcome to Checkers!\nRED will play first.");
		System.out.println("Enter locations when prompted in the form \"x,y\".");
		System.out.println("The board is arranged with 0,0 at the Top-Left," + 
			 " 7,7 at the Bottom-Right.");
		
		boolean validUserInput = false;
		while(!validUserInput){
			System.out.println("Do you want to play 1 or 2 player?");
			
			try { 
				String selection = input.nextLine();
				mode = Integer.parseInt(selection);
				if(mode != 1 && mode != 2) {
					System.out.println("Invalid entry- Setting to 1-player.");
					mode = 1;
				}
				validUserInput = true;
			} catch(IndexOutOfBoundsException e) {
				System.out.print("Invalid entry- try again. ");
			} catch(NumberFormatException e) {
				System.out.print("Invalid entry- try again. ");
			}
		}
		
		switch(mode) {
			case 1:
				System.out.println("Playing against an AI Player.");
				blackPlayer = new AIPlayer(Colour.BLACK,board);
				redPlayer = new HumanPlayer(Colour.RED,board); 
				break;
			case 2: 
				System.out.println("Red will move first.");
				blackPlayer = new HumanPlayer(Colour.BLACK,board);
				redPlayer = new HumanPlayer(Colour.RED,board); 
				break;
		}
		
		board.printArray();
	}
	
	private void initialize(int mode) {
		switch(mode) {
			case 1:
				blackPlayer = new AIPlayer(Colour.BLACK,board);
				redPlayer = new HumanPlayer(Colour.RED,board); 
				break;
			case 2: 
				blackPlayer = new HumanPlayer(Colour.BLACK,board);
				redPlayer = new HumanPlayer(Colour.RED,board); 
				break;
		}
	}
	
	/*
	 * Method to run a single Turn for a Human Player.
	 * A player can move a single Piece in a turn. If they jump a piece, they can continue to move
	 * the same piece, as long as they can continue to jump other pieces.
	 */
	private void turn(Player aPlayer){
		aPlayer.updatePieces();
		if(aPlayer.myPieces.length == 0 || canMove(aPlayer) == false){
			gameOver(aPlayer);
			return;
		}
		System.out.println("Turn: "+aPlayer.toString());
		
		Location start = null;
		Location end = null;
			
		// This segment is called until a valid movement has been made by the Player.
		while(board.turnComplete() == 0) {
			start = aPlayer.selectStart();
			end = aPlayer.selectEnd(start);
			aPlayer.movePiece(start,end);
		}
		board.printArray();
		
		start = end;
		
		// The following section pertains to a continuing turn- if a player jumps a piece,
		// they can proceed to make another jump if it is possible.
		while(board.turnComplete() == 1 && board.checkSquare(start).emptyJumps(aPlayer).length > 0){
			System.out.println("You took a piece and can continue to move!");
			end = aPlayer.selectEnd(start);
			aPlayer.movePiece(start, end);
			board.printArray();
			start = end;
		}
	}
	

	
	/*
	 * Accessor method to determine if a Player has any valid moves on the board.
	 */
	private boolean canMove(Player aPlayer) {
		for (int i = 0; i < aPlayer.myPieces.length; i++) {
			if (aPlayer.myPieces[i].emptyMoves(aPlayer).length > 0 
					|| aPlayer. myPieces[i].emptyJumps(aPlayer).length > 0){
				return true;
			} 
		}
		return false;
	}
	
	/*
	 * Method to end the game for a given Player.
	 */
	private void gameOver(Player aPlayer){
		System.out.println(aPlayer.toString()+" no longer has any pieces to move!"
			+ " They lose.");
		gameOver = true;
	}
	
	/**
	 * @return True if the game is over (ie, A player can no longer move.)
	 */
	public boolean gameOver(){
		return gameOver;
	}
}
