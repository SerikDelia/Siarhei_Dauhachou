

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import epamlab.Runnable.TransportationTask;
import epamlab.constant.ConstantElevator;
import epamlab.container.Floor;
import epamlab.factory.FloorFactory;
import epamlab.factory.PassengerFactory;
import epamlab.people.Controller;
import epamlab.people.Passenger;
import epamlab.reader.ReaderConfig;
import epamlab.swing.MainWindows;


public class Runner {

	
	public static void main(String[] args) {
		ResourceBundle resourceBundle=ResourceBundle.getBundle("config");
		ReaderConfig read=new ReaderConfig(resourceBundle);
		ConstantElevator.storiesNumber=read.getStoriesNumber();
		ConstantElevator.elevatorCapacity=read.getElevatorCapacity();
		ConstantElevator.passengersNumber=read.getPassengersNumber();
		ConstantElevator.animationBoost=read.getAnimationBoost();
		
		List <TransportationTask>  transportationTasks=new ArrayList();
		/* created  floors from factory*/
		int i=1;
		List<Floor> floors=new ArrayList();
		FloorFactory floorFactory=new FloorFactory();
		while (i<ConstantElevator.storiesNumber+1) {
			floors.add(floorFactory.getClassFromFactory(i));
			i++;
		}
		
		/* set passengers on the floor */
		int j=0;
		Controller controller=new Controller(floors);
		PassengerFactory passFactory=new PassengerFactory();
		while (j<ConstantElevator.passengersNumber) {
			Passenger pass=passFactory.getClassFromFactory(ConstantElevator.storiesNumber, j);
			for (Floor floor : floors) {
				if (floor.getIdFloor()==pass.getStartStory()) {
					floor.addPassenger(pass);
					
				}
			}
			
			transportationTasks.add(new TransportationTask(pass,controller));
			j++;
		}
		controller.getElevator().setElevatorCapacity(ConstantElevator.elevatorCapacity);
		
		/* check ending transportation and move elevator*/
		if(controller.getCreatedPassangers()!=ConstantElevator.passengersNumber){
			     controller.isReadyStart();
		}
		
		if(ConstantElevator.animationBoost>0){
		MainWindows window=new  MainWindows(floors,controller,transportationTasks);
		
		synchronized(controller){
			try {
				controller.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		
		while (controller.getElevator().getCountGoingOut()!=ConstantElevator.passengersNumber && !"ABORTED".equalsIgnoreCase(TransportationTask.getHandling())) {

			controller.movingElevator();
			
			try{
				Thread.currentThread().sleep(000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		controller.validation();
		
	}

}
