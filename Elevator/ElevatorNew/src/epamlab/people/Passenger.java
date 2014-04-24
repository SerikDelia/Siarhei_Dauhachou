package epamlab.people;

import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Passenger {
	private int startStory = 0;
	private int destinationStory = 0;
	private int passengerId;
	private int passengerX = 0;
	private int passengerY = 0;
	private String transportationState;
	private static Image imgPassengerOnFloor = new ImageIcon(
			"src/Resurces/Pictures/Zubila_Shield.gif").getImage();
	private static Image imgPassenArrived = new ImageIcon(
			"src/Resurces/Pictures/div_knight_run.gif").getImage();

	private static Image imgAbortedPassengers=new ImageIcon("src/Resurces/Pictures/power_boy.gif").getImage();
	
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

	public int getPassengerX(){
		return passengerX;
	}
	
	public void setPassengerX(int passengerX){
		this.passengerX=passengerX;
	}
	
	public int getPassengerY(){
		return passengerY;
	}
	
	public void setPassengerY(int passengerY){
		this.passengerY=passengerY;
	}
	
	public static Image getImgPassengerOnFloor(){
		return imgPassengerOnFloor;
	}
	
	public static Image getimgPassenArrived(){
		return imgPassenArrived;
	}
	
	public static Image getImgAbortedPassengers(){
		return imgAbortedPassengers;
	}
	
	public void setX(int x) {

		this.passengerX = x;
	}

	public int getX() {

		return passengerX;
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
