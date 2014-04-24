package epamlab.Runnable;

import org.apache.log4j.Logger;

import epamlab.constant.ConstantElevator;
import epamlab.people.Controller;
import epamlab.people.Passenger;
import epamlab.swing.MessageDailog;

public class TransportationTask implements Runnable {
	private Passenger passenger;
	private Controller controller;
	private Logger logger = Logger.getLogger(TransportationTask.class);
	private static String handling = "";
	private Thread tranportationTask;

	public TransportationTask(Passenger passenger, Controller controller) {
		super();
		this.passenger = passenger;
		this.controller = controller;
		tranportationTask = new Thread(this);
		tranportationTask.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread th = Thread.currentThread();
		if (th == tranportationTask) {

			// Do create and last Passenger message the controller to start;
			controller.setCreaedPassangers();
			passenger.setTransportationState("IN_PROGRESS");	
			// Do sleep on the level before controller not bother;
			logger.info(" STARTING_TRANSPORTATION ");
			if (ConstantElevator.animationBoost > 0) {
			MessageDailog.doWriteIntoTextArea(" STARTING_TRANSPORTATION ");
			}
			controller.waitElevatorCome(passenger.getStartStory(), passenger
					.getDestinationStory());						
			// Do sleep on the Lift before controller not bother;
			controller.peoplegoingLIft(passenger.getDestinationStory(),
					passenger);

			// Do go on the level and bother Lift;
			controller.pessengersGoingFromElevator(passenger
					.getDestinationStory(), passenger);
		    logger.info("COMPLETION_TRANSPORTATION ");
			passenger.setTransportationState("COMPLETED");
			if (ConstantElevator.animationBoost > 0) {
			MessageDailog.doWriteIntoTextArea("COMPLETION_TRANSPORTATION ");
			}
			
		}

	}

	public static String getHandling() {

		return handling;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	/*
	 * Method stop(): The method is invoked when do push on button with label
	 * 'ABORT'. The method do stop threads of transportationTask and interrupt
	 * them.
	 */
	public void stop() throws InterruptedException {
		handling = "ABORTED";	
		if(tranportationTask.isAlive()){
		if (!tranportationTask.isInterrupted()) {
			 logger.info("ABORTING_TRANSPORTATION");
			if (ConstantElevator.animationBoost > 0) {
				MessageDailog.doWriteIntoTextArea("ABORTING_TRANSPORTATION   ");
				}
			passenger.setTransportationState("ABORTED");
			tranportationTask.stop();
			tranportationTask.interrupt();
			tranportationTask=null;
		}
		}
	}

	

}
