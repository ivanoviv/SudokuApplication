package ivanov.sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 * A class which creates the Sudoku board and inputs a game (fills in the
 * numbers generated from the Game, or loaded by the user) on the board. The
 * class also gathers the input from the user.
 * 
 * @author Ivo Ivanov
 *
 */

public class NumbersPanel extends JPanel {
	private JFormattedTextField[][] numberFields;
	private boolean[][] userInput;
	private JPanel[][] panels;
	private Game game;

	public NumbersPanel() {
		this.setLayout(new GridLayout(3, 3, 5, 5));
		this.setPanels();
		this.setCells();
		this.userInput = new boolean[9][9];
		this.setInitialGame(20); // Initial difficulty set to 20 which
									// translates to easy
	}

	/**
	 * Creates the 9 panels for each block of numbers
	 */
	private void setPanels() {
		this.panels = new JPanel[3][3];
		for (int i = 0; i < panels.length; i++) {
			for (int j = 0; j < panels[i].length; j++) {
				panels[i][j] = new JPanel(new GridLayout(3, 3));
				this.add(panels[i][j]);
			}
		}
	}

	/**
	 * Creates the two dimensional array of TextFields to hold the number of the
	 * Sudoku Each TextField is placed on its respective JPanel (block)
	 */
	private void setCells() {
		MaskFormatter f = createFormatter("#"); // the MaskFormatter is used to
												// specify what can be input in
												// the Text Fields
		f.setValidCharacters("123456789");
		this.numberFields = new JFormattedTextField[9][9];
		for (int i = 0; i < numberFields.length; i++) {
			for (int j = 0; j < numberFields[i].length; j++) {
				numberFields[i][j] = new JFormattedTextField(f);
				numberFields[i][j].addKeyListener(new DeleteValueListener(
						this.numberFields[i][j]));// adds an ActionListener used
													// to delete the value from
													// a textfield which is
													// intended for user input
				panels[i / 3][j / 3].add(numberFields[i][j]);
			}
		}
	}

	/**
	 * Creates a new Sudoku game and inputs the numbers in the Number Fields
	 *
	 * @param difficulty
	 *            - a parameter indicating the difficulty of the the game
	 */
	public void setInitialGame(int dificulty) {
		this.clearNumberFields();
		for (int i = 0; i < this.userInput.length; i++) {
			Arrays.fill(userInput[i], true);
		}
		this.game = new Game(dificulty);
		for (int i = 0; i < numberFields.length; i++) {
			for (int j = 0; j < numberFields[i].length; j++) {

				if (game.getNumber(j, i) > 0)
					this.userInput[i][j] = false;
				this.setNumberInField(j, i, game.getNumber(j, i),
						this.userInput[i][j]);

			}
		}
	}

	/**
	 * a method used to load a game to the Numbers Panel
	 * 
	 * @param game
	 *            - an array of the current game to be set on the Sudoku board
	 * @param userInput
	 *            - a boolean matrix indicating whether the current cells is
	 *            intended for the user ot input
	 * @param savedGame
	 *            - an object of type SavedGame, set as a instance field of the
	 *            NumbersPanel
	 */

	public void setLoadGame(int[][] game, boolean[][] userInput, Game savedGame) {
		this.userInput = userInput;
		this.game = savedGame;
		this.clearNumberFields();
		for (int i = 0; i < numberFields.length; i++) {
			for (int j = 0; j < numberFields[i].length; j++) {
				this.setNumberInField(j, i, game[i][j], userInput[i][j]);
			}
		}
	}

	/**
	 * method which formats the Number fields
	 * 
	 * @param x
	 *            - the coordinate on the x axis
	 * @param y
	 *            - the coordinate on the y axis
	 * @param number
	 *            - the number to be input in the field
	 * @param userInput
	 *            - a boolean which indicates whether the number is inputed by
	 *            the user or by the game generator
	 * 
	 * */

	public void setNumberInField(int x, int y, int number, boolean userInput) {
		this.numberFields[y][x].setFont(new Font("SansSerif", Font.BOLD, 20));
		this.numberFields[y][x].setHorizontalAlignment(JTextField.CENTER);
		if (number > 0) {
			this.numberFields[y][x].setValue(new Integer(number));
		}
		if (number > 0 && !userInput) {
			this.numberFields[y][x].setEditable(false);
			this.numberFields[y][x].setBackground(Color.BLACK);
			this.numberFields[y][x].setForeground(Color.WHITE);
		}
	}

	/**
	 * an internal method to clear all the number fields
	 */

	private void clearNumberFields() {
		for (int i = 0; i < numberFields.length; i++) {
			for (int j = 0; j < numberFields[i].length; j++) {
				this.numberFields[i][j].setValue(null);
				this.numberFields[i][j].setBackground(Color.WHITE);
				this.numberFields[i][j].setForeground(Color.BLACK);
				this.numberFields[i][j].setEditable(true);
			}
		}
	}

	/**
	 * calls the check method of the Game instance and colors the wrong input
	 * (the cells where the user inputted wrong numbers) by the user in red
	 * 
	 * @return boolean variable indicating whether the whole game was correct or
	 *         not
	 */
	public boolean checkGame() {
		boolean[][] checkMatrix = this.game.checkUserGame(this.getUserGame());
		boolean checkWholeGame = true;
		for (int i = 0; i < numberFields.length; i++) {
			for (int j = 0; j < numberFields[i].length; j++) {
				if (!checkMatrix[i][j]) {
					checkWholeGame = false;
					this.numberFields[i][j].setBackground(Color.RED);
				}
			}
		}
		return checkWholeGame;
	}

	/**
	 * Gets all the numbers from the Sudoku board
	 * 
	 * @return a two dimensional array with the all of the numbers in the Sudoku
	 *         board
	 */

	public int[][] getUserGame() {
		int[][] userGame = new int[9][9];
		for (int i = 0; i < numberFields.length; i++) {
			for (int j = 0; j < numberFields[i].length; j++) {
				if (this.numberFields[i][j].getValue() != null) {
					userGame[i][j] = Integer
							.parseInt((String) this.numberFields[i][j]
									.getText());
				} else {
					userGame[i][j] = 0;
				}
			}
		}
		return userGame;
	}

	public boolean[][] getUserInput() {
		return this.userInput;
	}

	public Game getGame() {
		return this.game;
	}

	public JFormattedTextField getNumberField(int x, int y) {
		return this.numberFields[y][x];
	}

	/**
	 * Class to specify that only single digit numbers can be input in the
	 * NumberFields
	 * 
	 * @param s
	 *            - the string which should be input
	 * @return an object of type MaskFormatter
	 */
	protected MaskFormatter createFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
		}
		return formatter;
	}

	/**
	 * a Listener used to delete the inputed number in the Number field
	 *
	 */
	private class DeleteValueListener implements KeyListener {
		private JFormattedTextField numberField;

		private DeleteValueListener(JFormattedTextField numberField) {
			this.numberField = numberField;
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
					&& this.numberField.getBackground() != Color.BLACK) {
				this.numberField.setValue(null);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}
}
