package ivanov.sudoku;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * A class representing a TimeKeeper object used to  track the time since the beginning of the game
 * 
 * @author Ivo Ivanov
 *
 */
public class TimeKeeper extends JLabel implements ActionListener {
	private Timer timer;
	
	private int hours = 00;
	private int minutes = 00;
	private int seconds = 0;
	private String time;

	public TimeKeeper() {
		super();
		this.time= String.format("Time: %02d:%02d:%02d", this.hours, this.minutes, this.seconds);
		this.setText(this.time);
		this.timer = new Timer(1000, this);
		timer.start();

	}

	private void updateTime() {
		this.seconds++;
		if(this.seconds == 60){
			this.seconds =0;
			this.minutes++;
		}
		if (this.minutes ==60){
			this.minutes =0;
			this.hours++;
		}
		this.time= String.format("Time: %02d:%02d:%02d", this.hours, this.minutes, this.seconds);
		
	}
	public void restart(){
		this.timer.stop();
		this.seconds =0;
		this.minutes = 0;
		this.hours = 0;
		this.timer.restart();
	}
	
	public Timer getTimer(){
		return this.timer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateTime();
		this.setText(this.time);
		repaint();
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	

}
