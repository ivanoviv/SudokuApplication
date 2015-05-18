package ivanov.sudoku;

import java.io.Serializable;

/**
 * This class gets all the data from the current game including
 * an array with the numbers;
 * the matrix which details which number is initially input and which is input from the user 
 * the current state of the clock
 * @author Ivo Ivanov
 *
 */

public class SavedGame implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int[][] gameCopy;
	private Game game;
	private int hours;
	private int minutes;
	private int seconds;
	private boolean[][] userInputCopy;
	
	public SavedGame (){
		
	}
	
	public SavedGame(NumbersPanel numbersPanel, TimeKeeper timeKeeper ){
		this.gameCopy = this.copyGame(numbersPanel);
		this.game = numbersPanel.getGame();
		this.hours = timeKeeper.getHours();
		this.minutes = timeKeeper.getMinutes();
		this.seconds = timeKeeper.getSeconds();
		this.userInputCopy = this.copyUserInput(numbersPanel);
	
	}
	
	/**
	 * Creates a two dimensional array of numbers from the specified NumbersPanel (the Sudoku board)
	 * 
	 * 
	 * @param numbersPanel - the provided Sudoku board  
	 * @return - a two dimensional array of numbers from the Sudoku board 
	 */
	
	private int[][] copyGame (NumbersPanel numbersPanel){
		int[][] game = new int[9][9];
		for (int i =0 ; i < 9; i++){
			for (int j= 0; j< 9; j++){
				game[i][j] = numbersPanel.getUserGame()[i][j];
			}
		}
		return game;
	}
	
	/**
	 * Gets the boolean matrix indicating whether a cells is supposed to be filled by the user or it is 
	 * specified by the game 
	 * 
	 * @param numbersPanel
	 * @return 
	 */
	
	private boolean[][] copyUserInput (NumbersPanel numbersPanel){
		boolean[][] userInput = new boolean[9][9];
		for (int i =0 ; i < 9; i++){
			for (int j= 0; j< 9; j++){
				userInput[i][j] = numbersPanel.getUserInput()[i][j];
			}
		}
		return userInput;
	}
	
	/**
	 * Prints the game on the console for debugging
	 */
	
	public void printGame(){
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++)
				System.out.print(" " + this.gameCopy[y][x]);
			System.out.println();
		}
	}

	public boolean[][] getUserInput(){
		return this.userInputCopy;
	}
	
	public int[][] getGameCopy() {
		return gameCopy;
	}



	public int getHours() {
		return hours;
	}


	public int getMinutes() {
		return minutes;
	}

	public Game getGame(){
		return this.game;
	}

	public int getSeconds() {
		return seconds;
	}

}
