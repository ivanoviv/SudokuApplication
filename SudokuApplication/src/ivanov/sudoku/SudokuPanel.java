package ivanov.sudoku;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class represents the panel containing Sudoku board containing all the number fields (the NumberPanel), the
 * menu bar, the check button and the timekeeper at the bottom
 * 
 * @author Ivo Ivanov
 *
 */

public class SudokuPanel extends JPanel {
	private MenuBar menuBar;
	private NumbersPanel numbersPanel;

	public SudokuPanel() {

		setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(1, 1));
		
		JPanel bottomPanel = new JPanel();
		TimeKeeper timeKeeper = new TimeKeeper();
		JButton checkButton = new JButton("Check game");

		this.numbersPanel = new NumbersPanel();
		
		this.menuBar = new MenuBar(this.numbersPanel, timeKeeper);
		menuPanel.add(this.menuBar);

		this.add(menuPanel, BorderLayout.NORTH);
		
		this.add(this.numbersPanel, BorderLayout.CENTER);

		bottomPanel.add(timeKeeper);

		checkButton
				.addActionListener(new CheckButtonListener(this, timeKeeper));

		bottomPanel.add(checkButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * A ActionListener for the check button at the bottom which checks the Sudoku game and 
	 * stops the timekeeper
	 * 
	 * @author Ivo Ivanov
	 *
	 */
	private class CheckButtonListener implements ActionListener {
		private SudokuPanel panel;
		private TimeKeeper timekeeper;

		private CheckButtonListener(SudokuPanel panel, TimeKeeper timeKeeper) {
			this.panel = panel;
			this.timekeeper = timeKeeper;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.timekeeper.getTimer().stop();
			JOptionPane checkPopup = new JOptionPane();
			if (numbersPanel.checkGame()) {
				JOptionPane.showMessageDialog(this.panel,
						"You are the Sudoku Master");

			} else {
				JOptionPane.showMessageDialog(this.panel,
						"Incorrect. Please try again!");

			}

		}

	}
}
