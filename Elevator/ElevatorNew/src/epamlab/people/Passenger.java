package epamlab.people;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Passenger {
	private int startStory = 0;
	private int destinationStory = 0;
	private int passengerId;
	public int x = 0;
	public int y = 0;
	private String transportationState;
	public Image imgPassenger = new ImageIcon(
			"src/Resurces/Pictures/Zubila_Shield.gif").getImage();
	public Image imgPassenArrived = new ImageIcon(
			"src/Resurces/Pictures/div_knight_run.gif").getImage();

	public Passenger() {

	}

	public Passenger(int maxFloor, int passengerId) {
		transportationState = "NOT_STARTED";
		Random rand = new Random();
		this.passengerId = passengerId;
		while (startStory == destinationStory) {
			this.startStory = rand.nextInt(maxFloor) + 1;
			this.destinationStory = rand.nextInt(maxFloor) + 1;
		}

	}

	public void setX(int x) {

		this.x = x;
	}

	public int getX() {

		return x;
	}

	public String getTransportationState() {

		return transportationState;

	}

	public void setTransportationState(String transportationState) {

		this.transportationState = transportationState;

	}

	public int getStartStory() {
		return startStory;
	}

	public int getDestinationStory() {
		return destinationStory;
	}

	public int getPassengerId() {
		return passengerId;
	}

	@Override
	public String toString() {
		return "|Passenger :startStory: " + startStory + "; destinationStory: "
				+ destinationStory + "; passengerId: " + passengerId + ";|";
	}

}
