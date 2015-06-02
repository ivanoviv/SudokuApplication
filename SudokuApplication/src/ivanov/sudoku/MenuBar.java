package ivanov.sudoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This class represents the File menu at the top of the application. The menu contains a 
 * New game - another menu which offers 3 possible difficulty levels -Easy, Medium and Hard 
 * Load game - a button used to load a saved game 
 * Save game - a button used to save a current game 
 * Exit - a button used to exit the application. Upon restarting the application there will be a completely new game
 * @author Ivo Ivanov
 *
 */

public class MenuBar extends JMenuBar {
	private NumbersPanel numbersPanel;
	private TimeKeeper timeKeeper;
	private JMenu file;
	private JMenu newGame;
	private JMenuItem loadGame;
	private JMenuItem saveGame;
	private JMenuItem exit;
	
	/**
	 * Constructor creating the MenuBar 
	 * 
	 * @param numbersPanel - a panel containing the cells with the numbers 
	 * @param timeKeeper - a timekeeper object used to keep track of time
	 */

	public MenuBar(NumbersPanel numbersPanel, TimeKeeper timeKeeper) {
		this.numbersPanel = numbersPanel;
		this.timeKeeper = timeKeeper;
		this.file = this.createFileMenu();
		this.add(this.file);
	}
	
	/**
	 * a private method used to create the File menu, add all buttons and 
	 * their corresponding action listeners
	 * 
	 * 
	 */

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		this.newGame = this.createNewGameMenu();
		menu.add(newGame);
		
		this.saveGame = new JMenuItem("Save Game");
		this.saveGame.addActionListener(new SaveGameListener(this.numbersPanel));
		menu.add(saveGame);
		
		this.loadGame = new JMenuItem("Load Game");
		this.loadGame.addActionListener(new LoadGameListener(this.numbersPanel));
		menu.add(loadGame);
		
		this.exit = new JMenuItem("Exit");
		exit.addActionListener(new ExitListener());
		menu.add(exit);
		
		return menu;
	}
	
	/**
	 * creates the New game menu with all the difficulty buttons 
	 * 
	 * @return the created JMenu
	 */
	private JMenu createNewGameMenu() {
		JMenu newGameMenu = new JMenu("New Game");
		
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(new NewGameListener());
		newGameMenu.add(easy);
		
		JMenuItem medium = new JMenuItem("Medium");
		medium.addActionListener(new NewGameListener());
		newGameMenu.add(medium);
		
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(new NewGameListener());
		newGameMenu.add(hard);
		
		return newGameMenu;
	}

	/**
	 * This action listener set a new Game when the player chooses a difficulty
	 * level Easy - adds 20 additional numbers to the hardest level Medium -
	 * adds 10 additional numbers to the hardest level Hard - does not add any
	 * additional numbers to the generated game by the generator which is the
	 * hardest level
	 * 
	 * @author Ivo Ivanov
	 *
	 */

	private class NewGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String difficulty = (String) ((JMenuItem) e.getSource()).getText();
			if (difficulty.equalsIgnoreCase("Easy")) {
				numbersPanel.setInitialGame(20);
				timeKeeper.restart();
			} else if (difficulty.equalsIgnoreCase("Medium")) {
				numbersPanel.setInitialGame(10);
				timeKeeper.restart();	
			} else {
				numbersPanel.setInitialGame(-1); // -1 is used so that no additional numbers are added
				timeKeeper.restart();
			}
		}

	}
	
	/**
	 * An ActionListener for the save game button. An SaveGame object containing the current game, time and solution
	 * is initialized and serialized to a file chosen by the user 
	 * 
	 *
	 */

	private class SaveGameListener implements ActionListener {
		private NumbersPanel numbersPanel;

		public SaveGameListener(NumbersPanel numbersPanel) {
			this.numbersPanel = numbersPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser saveGameLocation = new JFileChooser();
			int retrival = saveGameLocation.showSaveDialog(this.numbersPanel);
			if (retrival == JFileChooser.APPROVE_OPTION) {
				saveGame(saveGameLocation);
			}
		}
		
		private void saveGame(JFileChooser saveGameLocation){
			SavedGame savedGame = new SavedGame(MenuBar.this.numbersPanel, timeKeeper);
			File saveGameFile = new File(saveGameLocation.getSelectedFile()
					+ ".txt");
			if (!saveGameFile.exists()) {
				try {
					System.out.println("File created: "
							+ saveGameFile.createNewFile());
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			try (OutputStream os = new FileOutputStream(saveGameFile);
					ObjectOutputStream oos = new ObjectOutputStream(os)) {
				oos.writeObject(savedGame);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * An ActionListener for the load game button in the File menu. An object of type Savedgame is deserialized
	 * from a file chosen by the user and is set on the numbersPanel of the Sudoku.
	 * 
	 * @author Ivo Ivanov
	 *
	 */
	
	private class LoadGameListener implements ActionListener{
		private NumbersPanel numbersPanel;

		public LoadGameListener(NumbersPanel numbersPanel) {
			this.numbersPanel = numbersPanel;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser loadGameLocation = new JFileChooser();
			int retrival = loadGameLocation.showOpenDialog(this.numbersPanel);
			SavedGame loadGame = new SavedGame();
			if (retrival == JFileChooser.APPROVE_OPTION) {
				File loadGameFile = loadGameLocation.getSelectedFile();
				try (InputStream is = new FileInputStream(loadGameFile);
						ObjectInputStream ois = new ObjectInputStream(is)) {
					loadGame = (SavedGame)ois.readObject();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}		
				setGame(loadGame);
				setTimeKeeper(loadGame);				
			}		
		}	
		
		/**
		 * Gets the numbers from the loaded game and inputs the 
		 * numbers on the current number grid
		 * @param loadGame
		 */
		private void setGame (SavedGame loadGame){
			numbersPanel.setLoadGame(loadGame.getGameCopy(), loadGame.getUserInput(), loadGame.getGame());
		}
		
		/**
		 * Sets the timekeeper data from the loadgame
		 * @param loadGame
		 */
		private void setTimeKeeper (SavedGame loadGame){
			timeKeeper.setMinutes(loadGame.getMinutes());
			timeKeeper.setHours(loadGame.getHours());
			timeKeeper.setSeconds(loadGame.getSeconds());
			timeKeeper.getTimer().start();
		}
	}
	
	/**
	 * An ActionListener for the exit button in the File menu. 
	 *
	 *
	 */
	private class ExitListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);		
		}
	}
}
