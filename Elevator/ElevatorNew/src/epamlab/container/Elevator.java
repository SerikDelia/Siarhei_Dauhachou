package epamlab.container;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import epamlab.constant.ConstantElevator;
import epamlab.people.Passenger;

public class Elevator {
	private int elevatorCapacity;

	private List<Passenger> elevatorContainer = new ArrayList();
	private int countMovingInto = 0;
	private int countGoingOut = 0;
	public Image imgLiftClose = new ImageIcon(
			"src/Resurces/Pictures/ElevatorClose.png").getImage();
	public Image imgLiftOpen = new ImageIcon(
			"src/Resurces/Pictures/ElevatorOpen.png").getImage();

	public Elevator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Elevator(int elevatorCapacity, int elevatorConteinerSiE,
			List<Passenger> elevatorContainer, int countMoving) {
		super();
		this.elevatorCapacity = elevatorCapacity;
		this.elevatorContainer = elevatorContainer;
		this.countMovingInto = countMoving;
	}

	public synchronized void upCountMoveingInto() {

		countMovingInto++;
	}

	public synchronized void upCountGoingOut() {
		countGoingOut++;
	}

	public int getCountGoingOut() {

		return countGoingOut;
	}

	public int getCountMovingInto() {

		return countMovingInto;
	}

	public List<Passenger> getElevatorContainer() {
		return elevatorContainer;
	}

	public void setElevatorContainer(List<Passenger> elevatorContainer) {
		this.elevatorContainer = elevatorContainer;
	}

	public void setElevatorCapacity(int elevatorCapacity) {

		this.elevatorCapacity = elevatorCapacity;

	}

	public int getElevatorCapacity() {

		return elevatorCapacity;
	}

	@Override
	public String toString() {
		return "Elevator [elevatorContainer=" + elevatorContainer + "]";
	}

}
