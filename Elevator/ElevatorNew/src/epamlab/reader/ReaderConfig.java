package epamlab.reader;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class ReaderConfig {
	private int storiesNumber;
	private int elevatorCapacity;
	private int passengersNumber;
	private int animationBoost;

	/* Start read config file and find data */
	public ReaderConfig(ResourceBundle resourceBundle) {
		Enumeration<String> fileContext = resourceBundle.getKeys();
		while (fileContext.hasMoreElements()) {
			String element = (String) fileContext.nextElement();
			if ("storiesNumber".equals(element)) {
				storiesNumber = Integer.valueOf(resourceBundle
						.getString(element));
			} else if ("elevatorCapacity".equals(element)) {
				elevatorCapacity = Integer.valueOf(resourceBundle
						.getString(element));
			} else if ("passengersNumber".equals(element)) {
				passengersNumber = Integer.valueOf(resourceBundle
						.getString(element));
			} else if ("animationBoost".equals(element)) {
				animationBoost = Integer.valueOf(resourceBundle
						.getString(element));
			} else {
				System.err.println("Problem with resource file");
			}
		}
		try{
		checkRightMinNumbers( storiesNumber, elevatorCapacity,passengersNumber);
		checkRightMaxNumbers(storiesNumber, elevatorCapacity, passengersNumber, animationBoost);
		}catch(ExceptionInInitializerError e){
			System.err.println("Error: "+e);
			System.exit(1);
		}
	}

	public int getStoriesNumber() {
		return storiesNumber;
	}

	public int getElevatorCapacity() {
		return elevatorCapacity;
	}

	public int getPassengersNumber() {
		return passengersNumber;
	}

	public int getAnimationBoost() {
		return animationBoost;
	}
	
	private void checkRightMinNumbers(int storiesNumber,int elevatorCapacity,int passengersNumber){
		
		if(storiesNumber<=1){
		  throw new ExceptionInInitializerError("StoriesNumber <= 1 Do  it bigger !");
		}else if(elevatorCapacity<=0){
			
		throw new ExceptionInInitializerError("ElevatorCapacity <= 0 Do  it bigger !");
			
		}else if (passengersNumber<=0){
			
		throw new ExceptionInInitializerError("PassengersNumber <= 0 Do  it bigger ! ");	
		}		
		
	}
	
private void checkRightMaxNumbers(int storiesNumber,int elevatorCapacity,int passengersNumber,int animationBoost ){
		
		if(storiesNumber>1000){
		  throw new ExceptionInInitializerError("StoriesNumber > 1000 make less !");
		}else if(elevatorCapacity>500){
			
		throw new ExceptionInInitializerError("ElevatorCapacity > 500 make less !");
			
		}else if (passengersNumber>1500){
			
		throw new ExceptionInInitializerError("PassengersNumber > 1500 make less ! ");	
		}else if (animationBoost>100){
			
		throw new ExceptionInInitializerError("PassengersNumber > 100 make less ! ");	
		}		
		
	}
}
