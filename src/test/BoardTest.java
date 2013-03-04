package test;
import main.*;


public class BoardTest {
	public static void main(String[] args) {
		Board board = new Board();
		board.initializeBoard();
		
		HumanPlayer player1 = new HumanPlayer(Colour.BLACK, board);
		AIPlayer player2 = new AIPlayer(Colour.RED, board);

		Location l1 = new Location(2,2);
		Location l2 = new Location(3,3);
		board.movePiece(player1, l1, l2);

		Location l3 = new Location(5, 5);
		Location l4 = new Location(4, 4);
		System.out.println("This test illistrates a move being made, first the pieces make" +
			" two legal moves.");
			board.movePiece(player2,l3, l4);

		board.printArray();
		board.movePiece(player1,l2, l3);
		System.out.println("\nValid jump made.\n");
		System.out.println("Here the red piece has been jumped by the black and removed.");
		board.printArray();

	}
}
