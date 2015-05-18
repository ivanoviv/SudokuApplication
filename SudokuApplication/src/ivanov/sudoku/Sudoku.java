package ivanov.sudoku;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * This is the main entry of the program which creates the JFrame and the panel containing the Sudoku
 * 
 * @author Ivo Ivanov
 *
 */
public class Sudoku {
	public static void main(String[] args) {
		// Create JFrame
		JFrame frame = new JFrame("Sudoku");
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and add the Sudoku panel to the JFrame
		JPanel panel = new SudokuPanel();
		frame.add(panel);
		frame.setVisible(true);
	}

}
