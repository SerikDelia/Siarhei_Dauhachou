package epamlab.people;

import java.util.List;
import org.apache.log4j.Logger;
import epamlab.constant.ConstantElevator;
import epamlab.container.Elevator;
import epamlab.container.Floor;
import epamlab.swing.MainWindows;
import epamlab.swing.MessageDailog;

public class Controller {
	private Floor curFloor;
	private List<Floor> floors;
	private int currentFloor;
	private Elevator elevator;
	private boolean wasPaintGointo = false;
	private boolean wasPaintGoOut = false;
	private boolean upDirection;
	private boolean openDoor = false;
	private boolean isLevelEmpty = false;
	private boolean isElevatorEmpty = true;
	private boolean ready = false;
	private boolean goneInto = false;
	private boolean goneOut = false;
	private boolean open=false;
	private int createdPassangers = 0;
	private Object waitingStart = new Object();
	private Object paintLiftWaitUP=new Object();
    private Object paintLiftWaitDown=new Object();
	private boolean liftup=false;
	private boolean liftDown=false;
	private Logger logger = Logger.getLogger(MainWindows.class);
	public int liftY = (ConstantElevator.storiesNumber * 200) - 200;

	public Controller(List<Floor> floors) {
		currentFloor = 1;
		elevator = new Elevator();
		upDirection = true;
		this.floors = floors;

	}

	
	public boolean open(){
		return open;
	}
	
	public boolean getOpenDoor() {
		return openDoor;
	}

	public List<Floor> getLevels() {

		return floors;
	}

	public synchronized Object getObgect() {
		return waitingStart;
	}

	public int getCreatedPassangers() {

		return createdPassangers;
	}

	public int getCurentLeve() {
		return currentFloor;
	}

	public synchronized boolean getIsEmptyElevator() {

		return isElevatorEmpty;

	}

	public synchronized boolean getIsEmptyLevel() {

		return isLevelEmpty;
	}

	public synchronized Elevator getElevator() {

		return elevator;
	}

	public synchronized Floor getFloor() {
		curFloor = floors.get(currentFloor - 1);

		return curFloor;
	}

	/*
	 * Method setCreaedPassangers(): 
	 * The method is invoked transportationTask
	 * which count created passengers and passenger which was created last
	 * bother main thread.
	 * The method has synchronized block.
	 */
	public void setCreaedPassangers() {
		synchronized (waitingStart) {
			createdPassangers++;
			logger.info("Passenger was created N " + createdPassangers);
			if (ConstantElevator.animationBoost > 0) {
				MessageDailog.doWriteIntoTextArea("Passenger was created N "
						+ createdPassangers + "\n");
			}
			if (createdPassangers == ConstantElevator.passengersNumber) {
				ready = true;
				waitingStart.notify();
			}
		}
	}

	/*
	 * Method isReadyStart(): 
	 * The method is invoked main thread and main thread
	 * wait if not all passengers was created. 
	 * The method has synchronized block.
	 */
	public void isReadyStart() {
		synchronized (waitingStart) {
			while (!ready) {
				try {
					waitingStart.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
		}
		ready = false;
	}

	/*
	 * Method waitElevatorCome(): 
	 * The method is invoked transportationTask and
	 * transportationTask wait before lift do bother. 
	 * The method has synchronized blocks.
	 */
	public void waitElevatorCome(int startStory, int destenitionStory) {
		synchronized (floors.get(startStory - 1).getDispatchStoryContainer()) {
			try {
				floors.get(startStory - 1).getDispatchStoryContainer().wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("Thred was waitingStart when aborted prossecing");
			}
		}

		if (startStory > destenitionStory && upDirection) {
			synchronized (elevator) {
				goneInto = true;
				elevator.notify();
			}

			synchronized (floors.get(startStory - 1)
					.getDispatchStoryContainer()) {
				try {
					floors.get(startStory - 1).getDispatchStoryContainer()
							.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
		}

		if (startStory < destenitionStory && !upDirection) {
			synchronized (elevator) {
				goneInto = true;
				elevator.notify();

			}

			synchronized (floors.get(startStory - 1)
					.getDispatchStoryContainer()) {
				try {
					floors.get(startStory - 1).getDispatchStoryContainer()
							.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
		}

	}

	// ----------------------------------------------------------------------------------

	/*
	 * Method pessengersGoingFromElevator(): 
	 * The method is invoked transportationTask when passenger need go into elevator. 
	 * The method has methods for correct paint graphics movement of passenger.
	 *  The method has synchronized blocks.
	 */
	public void peoplegoingLIft(int destenationStory, Passenger passenger) {
		if (ConstantElevator.animationBoost > 0) {
			waitUpdateImageGoneInto(passenger);
		}
		synchronized (floors.get(currentFloor - 1).getDispatchStoryContainer()) {

			floors.get(currentFloor - 1).getDispatchStoryContainer().remove(
					passenger);
			elevator.getElevatorContainer().add(passenger);

			logger.info("BOADING_OF_PASSENGER :" + passenger.getPassengerId()
					+ " on " + passenger.getStartStory() + "\n");
			if (ConstantElevator.animationBoost > 0) {
				MessageDailog.doWriteIntoTextArea("BOADING_OF_PASSENGER :"
						+ passenger.getPassengerId() + " on "
						+ passenger.getStartStory() + "\n");
			}
			elevator.getCountMovingInto();
		}

		synchronized (elevator) {
			goneInto = true;
			elevator.notify();
		}
		synchronized (floors.get(destenationStory - 1)
				.getArrivalStoryContainer()) {
			try {
				floors.get(destenationStory - 1).getArrivalStoryContainer()
						.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("Thred was waitingStart when aborted prossecing");
			}

		}
	}

	/*
	 * Method botherOnLevel(): 
	 * The method is invoked controller and bother
	 * passenger for go into lift if it's need. 
	 * The method has synchronized blocks.
	 */
	public void botherOnLevel() {

		while (poolGoInto()) {
			synchronized (floors.get(currentFloor - 1)
					.getDispatchStoryContainer()) {
				logger.info("BOADING!!!! \n");
				if (ConstantElevator.animationBoost > 0) {
					MessageDailog.doWriteIntoTextArea("BOADING!!!! \n");
				}

				floors.get(currentFloor - 1).getDispatchStoryContainer()
						.notify();
			}
			waitMoveInto();

		}

	}

	/*
	 * Method botherOnLift(): 
	 * The method is invoked controller and bother
	 * passenger for go out from lift if it's need.
	 * The method has synchronized blocks.
	 */
	public void botherOnLift() {

		while (poolGoOut()) {
			synchronized (floors.get(currentFloor - 1)
					.getArrivalStoryContainer()) {
				logger.info("DEBOADING!!!!!!!! \n");
				if (ConstantElevator.animationBoost > 0) {
					MessageDailog.doWriteIntoTextArea("DEBOADING!!!!!!!! \n");
				}
				floors.get(currentFloor - 1).getArrivalStoryContainer()
						.notifyAll();
				openDoor = true;
			}

			waitMoveOut();
		}

	}

	/*
	 * Method pessengersGoingFromElevator(): 
	 * The method is invoked transportationTask
	 *  when passenger need go out from elevator. 
	 * The method has methods for correct paint graphics movement of passenger. 
	 * The method has synchronized blocks.
	 */
	public void pessengersGoingFromElevator(int destenationStory,
			Passenger passenger) {
		if (ConstantElevator.animationBoost > 0) {
			synchronized (waitingStart) {
				waitUpdatePainGoneOut(passenger);

			}
		}
		synchronized (elevator.getElevatorContainer()) {
			elevator.getElevatorContainer().remove(passenger);
			floors.get(destenationStory - 1).getArrivalStoryContainer().add(
					passenger);
			logger.info("DEBOADING_OF_PASSENGER: " + passenger.getPassengerId()
					+ " on " + currentFloor + "\n");
			if (ConstantElevator.animationBoost > 0) {
				MessageDailog.doWriteIntoTextArea("DEBOADING_OF_PASSENGER: "
						+ passenger.getPassengerId() + " on " + currentFloor
						+ "\n");
				MessageDailog
						.doWriteIntoTextArea("ELEVATOR STATE AFTER DEBOADING: "
								+ elevator.getElevatorContainer() + "\n");
			}
			elevator.upCountGoingOut();
		}

		synchronized (elevator) {
			goneOut = true;
			elevator.notify();
		}

	}

	/*
	 * waitMoveInto(): The method is invoked controller and controller wait
	 * before passenger not go into floor. The method has synchronized blocks.
	 */
	public void waitMoveInto() {
		synchronized (elevator) {
			while (!goneInto) {
				try {
					elevator.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
		}

		goneInto = false;

	}

	/*
	 * Method waitMoveOut(): 
	 * The method is invoked controller and controller
	 * wait before passenger not go out from elevator.
	 *  The method has synchronized blocks.
	 */
	public void waitMoveOut() {
		synchronized (elevator) {
			while (!goneOut) {
				openDoor = true;
				try {
					elevator.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
		}

		goneOut = false;

	}

	// ------------------------------------------------------------------------------------------------------

	/*
	 * Method poolGoInto(): 
	 * The method is seeing there is anyone who wants go
	 * into elevator.
	 */
	public boolean poolGoInto() {

		synchronized (floors.get(currentFloor - 1).getDispatchStoryContainer()) {

			for (Passenger passenger : floors.get(currentFloor - 1)
					.getDispatchStoryContainer()) {

				if (currentFloor == passenger.getStartStory()
						&& passenger.getStartStory() < passenger
								.getDestinationStory()
						&& upDirection
						&& elevator.getElevatorContainer().size() < elevator
								.getElevatorCapacity()) {

					return true;
				} else if (currentFloor == passenger.getStartStory()
						&& passenger.getStartStory() > passenger
								.getDestinationStory()
						&& !upDirection
						&& elevator.getElevatorContainer().size() < elevator
								.getElevatorCapacity()) {

					return true;
				}

			}
		}

		return false;
	}

	/*
	 * Method poolGoOut(): 
	 * The method is seeing there is anyone who wants go out
	 * from elevator.
	 */
	public boolean poolGoOut() {

		synchronized (floors.get(currentFloor - 1).getArrivalStoryContainer()) {

			for (Passenger passenger : elevator.getElevatorContainer()) {

				if (currentFloor == passenger.getDestinationStory()
						&& !elevator.getElevatorContainer().isEmpty()) {

					return true;
				}

			}

		}

		return false;
	}

	/*
	 * The method is Running into main thread while all transportationTasks not
	 * is done or processing not aborted. 
	 * The method Running movements of elevator. 
	 * The method has another methods need for movements of passengers
	 * The method has methods with synchronized blocks.
	 */
	public void movingElevator() {
		open=false;
		if (upDirection) {
		    if (hasElevatorUp()) {
				if (isElevatorEmpty() && isFloorEmpty()) {			
					elevatorUp();
					logger
							.info("ELEVATTOR UP ON FLOR ¹ " + currentFloor
									+ "\n");
					if (ConstantElevator.animationBoost > 0) {
						MessageDailog
								.doWriteIntoTextArea("ELEVATTOR UP ON FLOR ¹ "
										+ currentFloor + "\n");
					}
					return;
				}
				if (!isElevatorEmpty()) {

					botherOnLift();
				}
				if (!isFloorEmpty()
						&& elevator.getElevatorContainer().size() < elevator
								.getElevatorCapacity()) {
					botherOnLevel();
				}
				elevatorUp();
				logger.info("ELEVATTOR UP ON FLOR ¹ " + currentFloor + "\n");
				if (ConstantElevator.animationBoost > 0) {
					MessageDailog.doWriteIntoTextArea("ELEVATTOR UP ON FLOR ¹ "
							+ currentFloor + "\n");
				}
				return;
			}
		} else if (!upDirection) {
			if (hasElevatorDown()) {
				if (isElevatorEmpty() && isFloorEmpty()) {
					elevatorDown();
					logger.info("ELEVATTOR DOWN ON FLOR ¹ " + currentFloor
							+ "\n");
					if (ConstantElevator.animationBoost > 0) {
						MessageDailog
								.doWriteIntoTextArea("ELEVATTOR DOWN ON FLOR ¹ "
										+ currentFloor + "\n");
					}
					return;
				}
				if (!isElevatorEmpty()) {

					botherOnLift();

				}
				if (!isFloorEmpty()) {

					botherOnLevel();
				}
				elevatorDown();
				logger.info("ELEVATTOR DOWN ON FLOR ¹ " + currentFloor + "\n");
				if (ConstantElevator.animationBoost > 0) {
					MessageDailog
							.doWriteIntoTextArea("ELEVATTOR DOWN ON FLOR ¹ "
									+ currentFloor + "\n");
				}
				return;

			}

		}
	}

	public boolean isElevatorEmpty() {

		if (elevator.getElevatorContainer().isEmpty()) {
			
			isElevatorEmpty = true;
		} else {

			isElevatorEmpty = false;
		}
		return isElevatorEmpty;

	}

	public boolean isFloorEmpty() {
		curFloor = getFloor();
		if (curFloor.getDispatchStoryContainer().isEmpty()) {
			isLevelEmpty = true;
			
		} else {
			open=true;
			isLevelEmpty = false;
		}
		return isLevelEmpty;

	}

	private void elevatorUp() {
		//liftY -= 200;
		
		currentFloor++;
		waitUpdatePainLiftUp();

	}

	private void elevatorDown() {
	//	liftY += 200;
		
		currentFloor--;
		waitUpdatePainLiftDown();

	}

	private boolean hasElevatorUp() {
		if (currentFloor != ConstantElevator.storiesNumber
				&& currentFloor < ConstantElevator.storiesNumber) {
			openDoor = true;
			return true;
		}
		upDirection = false;
		return false;
	}

	private boolean hasElevatorDown() {
		if (currentFloor > 1) {
			return true;
		}
		openDoor = true;
		upDirection = true;
		return false;
	}

	
	/*
	 * The method do validate when processing was successful or was aborted.
	 * The method input results of validation into log file or if animationBoots
	 * bigger null type result on messagDialog too.
	 */
	public void validation() {
		logger.info("Validation!!!!  \n");
		if (ConstantElevator.animationBoost > 0) {
			MessageDailog.doWriteIntoTextArea("Validation!!!!  \n");
		}
		for (Floor f : floors) {
			logger.info("CHECK FLOOR BOADING: Nomber floor: " + f.getIdFloor()
					+ " IS EMPTY ?: " + f.getDispatchStoryContainer().isEmpty()
					+ "\n");
			if (ConstantElevator.animationBoost > 0) {
				MessageDailog
						.doWriteIntoTextArea("CHECK FLOOR BOADING: Nomber floor: "
								+ f.getIdFloor()
								+ "\n IS EMPTY ?: "
								+ f.getDispatchStoryContainer().isEmpty()
								+ "\n");
				f.getDispatchStoryContainer();
				f.getIdFloor();
			}
			for (Passenger p : f.getArrivalStoryContainer()) {
				logger.info("CHECK FLOOR DEBOADING: Nomber floor: "
						+ f.getIdFloor()
						+ " IS DESTENATION_STORI EQUALS NUMBER FLOOR ?: "
						+ (f.getIdFloor() == p.getDestinationStory()) + "\n");
				if (ConstantElevator.animationBoost > 0) {
					MessageDailog
							.doWriteIntoTextArea("CHECK FLOOR DEBOADING: Nomber floor: "
									+ f.getIdFloor()
									+ " IS DESTENATION_STORI EQUALS NUMBER FLOOR ?: "
									+ (f.getIdFloor() == p
											.getDestinationStory()) + " \n");
				}
			}
		}
		logger.info("IT WAS CREATED PASSENGERS: "
				+  createdPassangers + " IT WAS NEED CREATED: "
				+ ConstantElevator.passengersNumber + "\n");
		if (ConstantElevator.animationBoost > 0) {
			MessageDailog.doWriteIntoTextArea("it WAS CREATED PASSENGERS: "
					+  createdPassangers + " IT WAS DEBOADING: "
					+  createdPassangers + "\n");
		}
	}
	
	// Methods for Graphic
	
	/*
	 * The method change coordinates passengers witch want to go into.
	 *The method has synchronized blocks.
	 */
	public void waitUpdateImageGoneInto(Passenger passenger) {

		synchronized (floors) {
			if (!wasPaintGointo) {
				try {
					open=true;
					floors.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
			for (double i = 0; i > -(200 + passenger.getPassengerId() * 10); i--) {
				i = i - ConstantElevator.animationBoost;
				passenger.setX((int) i);
				try {
					floors.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}

		}

		wasPaintGointo = false;

	}

	
	
	/*
	 * The method change coordinates passengers witch want to go out.
	 *The method  has synchronized blocks.
	 */
	
	public void waitUpdatePainGoneOut(Passenger passenger) {
		synchronized (floors) {
			if (!wasPaintGoOut) {
				try {
					open=true;
					floors.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
			for (int i = 1; i < 580; i++) {
				i += ConstantElevator.animationBoost;
				open=true;
				passenger.setX(i);
				try {
					floors.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}

		}

		wasPaintGoOut = false;

	}

	
	/*
	 * The method change coordinates lift witch  go up.
	 *The method  has synchronized blocks.
	 */
	
	public void waitUpdatePainLiftUp() {
		synchronized (paintLiftWaitUP) {
			if (!liftup) {
				try {
					open=false;
					paintLiftWaitUP.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
		
			
			for (int i=1;i<200;i++) {
				///ConstantElevator.animationBoost;
				open=false;
				liftY--;		
				try {
					paintLiftWaitUP.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}

		}
		
		paintLiftWaitUP = false;

	}

	
	/*
	 * The method change coordinates lift witch  go down.
	 *The method  has synchronized blocks.
	 */
	
	public void waitUpdatePainLiftDown() {
		synchronized (paintLiftWaitDown) {
			if (!liftDown) {
				try {
					open=false;
					paintLiftWaitDown.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}
			for (int i=1; i<200;i++) {
				open=false;
				liftY++;
				
				try {
					paintLiftWaitDown.wait();;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err
							.println("Thred was waitingStart when aborted prossecing");
				}
			}

		}

		liftDown= false;

	}
	
	/* The method weak controller up when paint movements of passengers what is
	 * going into lift; 
	 * The method has has synchronized blocks
	 */
	public void botherConsol() {
		synchronized (floors) {
			wasPaintGointo = true;
			floors.notify();
		}
	}
	
	/*
	 * The method weak controller up when paint movements of passengers what is
	 * going out from lift
	 * The method has has synchronized blocks
	 */
	public void botherConsolOut() {
		synchronized (floors) {
			wasPaintGoOut = true;
			floors.notify();
		}
	}
	
	/*
	 * The method weak controller up when paint movements up of lift; 
	 * The method has has synchronized blocks
	 */
	public void botherConsolLiftUp() {
		synchronized (paintLiftWaitUP) {
			liftup = true;
			paintLiftWaitUP.notify();
		}
	}
	public void botherConsolLiftDown() {
		synchronized (paintLiftWaitDown) {
			liftDown = true;
			paintLiftWaitDown.notify();
		}
	}
    
}
