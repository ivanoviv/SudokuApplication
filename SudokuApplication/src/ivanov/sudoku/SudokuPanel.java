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
 * This class represents the panel containing Sudoku board containing all the
 * number fields (the NumberPanel), the menu bar, the check button and the
 * timekeeper at the bottom
 * 
 * @author Ivo Ivanov
 *
 */
public class SudokuPanel extends JPanel {
	private MenuBar menuBar;
	private NumbersPanel numbersPanel;

	public SudokuPanel() {
		setLayout(new BorderLayout());
		this.numbersPanel = new NumbersPanel();
		this.add(this.numbersPanel, BorderLayout.CENTER);
		TimeKeeper timeKeeper = new TimeKeeper();
		this.generateBottomPanel(timeKeeper);
		this.generateMenuPanel(timeKeeper);
	}

	/**
	 * Generates the menupanel with the menubar
	 * @param timeKeeper
	 */
	private void generateMenuPanel(TimeKeeper timeKeeper) {
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(1, 1));
		this.menuBar = new MenuBar(this.numbersPanel, timeKeeper);
		menuPanel.add(this.menuBar);
		this.add(menuPanel, BorderLayout.NORTH);
	}

	/**
	 * Generates the bottom panel with the check button and the timer
	 * @param timeKeeper
	 */
	private void generateBottomPanel(TimeKeeper timeKeeper) {
		JPanel bottomPanel = new JPanel();
		JButton checkButton = new JButton("Check game");
		bottomPanel.add(timeKeeper);
		checkButton
				.addActionListener(new CheckButtonListener(this, timeKeeper));
		bottomPanel.add(checkButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * A ActionListener for the check button at the bottom which checks the
	 * Sudoku game and stops the timekeeper
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
