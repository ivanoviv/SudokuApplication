package ivanov.sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class includes an algorithm to generate a fully solved Sudoku board, and
 * a corresponding Sudoku game (with missing cells) from it. The class also
 * contains a method to check the user input obtained from the NumbersPanel.
 * 
 * Credits to Eric Beijer for the algorithm
 *
 */
public class Game implements Serializable {
	private int[][] solution; // Generated solution.
	private int[][] game; // Generated game with user input.

	/**
	 * Constructor which creates a new Sudoku game
	 * 
	 * @ param difficulty - a parameter indicating how many numbers on top of the
	 * hardest level should the Sudoku have (0 - hardest, 10 -medium, 20 - easy)
	 * 
	 */
	public Game(int dificulty) {
		newGame(dificulty);
		print(game);
		print(solution);
	}

	/**
	 * Generates a new Sudoku game.<br />
	 *
	 */
	private void newGame(int difficulty) {
		solution = generateSolution(new int[9][9], 0);
		game = generateGame(copy(solution), difficulty);
	}

	/**
	 * Sets given number on given position in the game.
	 *
	 * @param x
	 *            The x position in the game.
	 * @param y
	 *            The y position in the game.
	 * @param number
	 *            The number to be set.
	 */
	public void setNumber(int x, int y, int number) {
		game[y][x] = number;
	}

	/**
	 * Returns number of given position.
	 *
	 * @param x
	 *            X position in game.
	 * @param y
	 *            Y position in game.
	 * @return Number of given position.
	 */
	public int getNumber(int x, int y) {
		return game[y][x];
	}

	/**
	 * Returns whether given number is candidate on x axis for given game.
	 *
	 * @param game
	 *            Game to check.
	 * @param y
	 *            Position of x axis to check.
	 * @param number
	 *            Number to check.
	 * @return True if number is candidate on x axis, false otherwise.
	 */
	private boolean isPossibleX(int[][] game, int y, int number) {
		for (int x = 0; x < 9; x++) {
			if (game[y][x] == number)
				return false;
		}
		return true;
	}

	/**
	 * Returns whether given number is candidate on y axis for given game.
	 *
	 * @param game
	 *            Game to check.
	 * @param x
	 *            Position of y axis to check.
	 * @param number
	 *            Number to check.
	 * @return True if number is candidate on y axis, false otherwise.
	 */
	private boolean isPossibleY(int[][] game, int x, int number) {
		for (int y = 0; y < 9; y++) {
			if (game[y][x] == number)
				return false;
		}
		return true;
	}

	/**
	 * Returns whether given number is candidate in block for given game.
	 *
	 * @param game
	 *            Game to check.
	 * @param x
	 *            Position of number on x axis in game to check.
	 * @param y
	 *            Position of number on y axis in game to check.
	 * @param number
	 *            Number to check.
	 * @return True if number is candidate in block, false otherwise.
	 */
	private boolean isPossibleBlock(int[][] game, int x, int y, int number) {
		int x1 = x < 3 ? 0 : x < 6 ? 3 : 6;
		int y1 = y < 3 ? 0 : y < 6 ? 3 : 6;
		for (int yy = y1; yy < y1 + 3; yy++) {
			for (int xx = x1; xx < x1 + 3; xx++) {
				if (game[yy][xx] == number)
					return false;
			}
		}
		return true;
	}

	/**
	 * Returns next possible number from list for given position or -1 when list
	 * is empty.
	 *
	 * @param game
	 *            Game to check.
	 * @param x
	 *            X position in game.
	 * @param y
	 *            Y position in game.
	 * @param numbers
	 *            List of remaining numbers.
	 * @return Next possible number for position in game or -1 when list is
	 *         empty.
	 */
	private int getNextPossibleNumber(int[][] game, int x, int y,
			List<Integer> numbers) {
		while (numbers.size() > 0) {
			int number = numbers.remove(0);
			if (isPossibleX(game, y, number) && isPossibleY(game, x, number)
					&& isPossibleBlock(game, x, y, number))
				return number;
		}
		return -1;
	}

	/**
	 * Generates Sudoku game solution.
	 *
	 * @param game
	 *            Game to fill, user should pass 'new int[9][9]'.
	 * @param index
	 *            Current index, user should pass 0.
	 * @return Sudoku game solution.
	 */
	private int[][] generateSolution(int[][] game, int index) {
		if (index > 80) {
			return game;
		}
		int x = index % 9;
		int y = index / 9;
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= 9; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);
		while (numbers.size() > 0) {
			int number = getNextPossibleNumber(game, x, y, numbers);
			if (number == -1)
				return null;
			game[y][x] = number;
			int[][] tmpGame = generateSolution(game, index + 1);
			if (tmpGame != null)
				return tmpGame;
			game[y][x] = 0;
		}
		return null;
	}

	/**
	 * Generates Sudoku game from solution.
	 *
	 * @param game
	 *            Game to be generated, user should pass a solution.
	 * @return Generated Sudoku game.
	 */
	private int[][] generateGame(int[][] game, int difficulty) {
		List<Integer> positions = new ArrayList<Integer>();
		for (int i = 0; i < 81; i++){
			positions.add(i);
		}
		Collections.shuffle(positions);
		return generateGame(game, positions, difficulty);
	}

	/**
	 * Generates Sudoku game from solution, user should use the other
	 * generateGame method. This method simple removes a number at a position.
	 * If the game isn't anymore valid (does not have only 1 solution) after
	 * this action, the game will be brought back to previous state.
	 *
	 * @param game
	 *            Game to be generated.
	 * @param positions
	 *            List of remaining positions to clear.
	 * @return Generated Sudoku game.
	 */
	private int[][] generateGame(int[][] game, List<Integer> positions,
			int difficulty) {
		List<Integer> removedNumberIndex = new ArrayList<Integer>();
		while (positions.size() > 0) {
			int position = positions.remove(0);
			removedNumberIndex.add(position);
			int x = position % 9;
			int y = position / 9;
			int temp = game[y][x];
			game[y][x] = 0;
			if (!isValid(game)) {
				game[y][x] = temp;
				removedNumberIndex.remove((Integer) position);
			}
		}
		this.setDificulty(game, removedNumberIndex, difficulty);
		return game;
	}
	
	/**
	 * An internal method used to set the difficulty to the game once a game has been created
	 * @param game - the game for which the difficulty will be set 
	 * @param removedNumberIndex - a list containing the position of each removed number
	 * @param difficulty - an integer indicating what difficulty should the game be set to
	 */
	private void setDificulty(int[][] game, List<Integer> removedNumberIndex, int difficulty){
		int i = 0;
		while (i <= difficulty) {
			int position = removedNumberIndex.remove(i);
			int x = position % 9;
			int y = position / 9;
			game[y][x] = this.solution[y][x];
			i++;
		}
	}

	public int[][] getSolution() {
		return this.solution;
	}

	/**
	 * Checks whether given game is valid.
	 *
	 * @param game
	 *            Game to check.
	 * @return True if game is valid, false otherwise.
	 */
	private boolean isValid(int[][] game) {
		return isValid(game, 0, new int[] { 0 });
	}

	/**
	 * Checks whether given game is valid, user should use the other isValid
	 * method. There may only be one solution.
	 *
	 * @param game
	 *            Game to check.
	 * @param index
	 *            Current index to check.
	 * @param numberOfSolutions
	 *            Number of found solutions. Int[] instead of int because of
	 *            pass by reference.
	 * @return True if game is valid, false otherwise.
	 */
	private boolean isValid(int[][] game, int index, int[] numberOfSolutions) {
		if (index > 80)
			return ++numberOfSolutions[0] == 1;
		int x = index % 9;
		int y = index / 9;
		if (game[y][x] == 0) {
			List<Integer> numbers = new ArrayList<Integer>();
			for (int i = 1; i <= 9; i++){
				numbers.add(i);
			}
			while (numbers.size() > 0) {
				int number = getNextPossibleNumber(game, x, y, numbers);
				if (number == -1){
					break;
				}
				game[y][x] = number;
				if (!isValid(game, index + 1, numberOfSolutions)) {
					game[y][x] = 0;
					return false;
				}
				game[y][x] = 0;
			}
		} else if (!isValid(game, index + 1, numberOfSolutions)) {
			return false;
		}
		return true;
	}

	/**
	 * A method to check the passed as an argument game
	 * 
	 * @param userGame
	 *            - the game to be checked
	 * @return a boolean matrix indicating whether a number in the game to be
	 *         checked is correct or not
	 */

	public boolean[][] checkUserGame(int[][] userGame) {
		boolean[][] checkMatrix = new boolean[9][9];
		for (int i = 0; i < userGame.length; i++) {
			for (int j = 0; j < userGame[i].length; j++) {
				checkMatrix[i][j] = userGame[i][j] == this.solution[i][j];
			}
		}
		return checkMatrix;
	}

	/**
	 * Copies a game.
	 *
	 * @param game
	 *            Game to be copied.
	 * @return Copy of given game.
	 */
	private int[][] copy(int[][] game) {
		int[][] copy = new int[9][9];
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++)
				copy[y][x] = game[y][x];
		}
		return copy;
	}

	/**
	 * Prints the solution of the Sudoku - used for debugging
	 * 
	 * @param game
	 */
	private void print(int[][] game) {
		System.out.println();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++)
				System.out.print(" " + game[y][x]);
			System.out.println();
		}
	}

	/**
	 * a method used to set a solution from a loaded game
	 * 
	 * @param solutionCopy
	 *            - the solution to the game
	 */
	public void setSolution(int[][] solutionCopy) {
		this.solution = solutionCopy;
	}

}