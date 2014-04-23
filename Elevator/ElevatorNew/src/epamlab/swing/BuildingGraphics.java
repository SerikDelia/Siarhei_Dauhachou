package epamlab.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import epamlab.constant.ConstantElevator;
import epamlab.container.Floor;
import epamlab.people.Controller;
import epamlab.people.Passenger;

public class BuildingGraphics extends JPanel implements ActionListener {
	Timer buildintimer = new Timer(10, this);
	private Controller controller;
	private List<Floor> floors = new ArrayList();
	private JButton start;
    private Image levelView= new ImageIcon("src/Resurces/Pictures/fon.jpg").getImage();
    private Image imgBuilding = new ImageIcon("src/Resurces/Pictures/Floor.jpg").getImage();
    private Image portal= new ImageIcon("src/Resurces/Pictures/portal.gif").getImage();
    private Image fireLift= new ImageIcon("src/Resurces/Pictures/ogon.gif").getImage();
    private Image fireLevel= new ImageIcon("src/Resurces/Pictures/fire.gif").getImage();
    private Image lableLevel= new ImageIcon("src/Resurces/Pictures/lable.gif").getImage();
    private Image lableLevelLift= new ImageIcon("src/Resurces/Pictures/Q3Cover.jpg").getImage();
    private Image skinDoorR= new ImageIcon("src/Resurces/Pictures/Q3CoverR.jpg").getImage();
    private Image skinDoorL= new ImageIcon("src/Resurces/Pictures/Q3CoverL.jpg").getImage();
    private Scrollbar scroll;
    private List<Image> full = new ArrayList();
    private Graphics g=null;
    private static final int sizeImegeFloor = 200;

	public BuildingGraphics(List<Floor> floors, Controller controller, JButton start) {
		super();
        Dimension size=new Dimension(740,(ConstantElevator.storiesNumber*200));
		setLayout(new BorderLayout());
		setPreferredSize(size);
		this.floors = floors;
		this.controller = controller;
		this.start = start;
		buildintimer.start();

	}

	public void paint(Graphics g) {
		g = (Graphics2D) g;
		int i = 0;
		//It does paint levels;
		
		for (; i != ConstantElevator.storiesNumber; i++) {

			
			g.drawImage(imgBuilding, 0, sizeImegeFloor * i,null);
			g.drawImage(levelView,200, sizeImegeFloor * i,700,120,null);
			g.drawImage(lableLevel,200, sizeImegeFloor * i,70,70,null);
			g.drawImage(lableLevel,400, sizeImegeFloor * i,70,70,null);
			g.drawImage(lableLevel,600, sizeImegeFloor * i,70,70,null);
			g.drawImage(lableLevelLift,5, (sizeImegeFloor * i)+2,95,198,null);
			g.drawImage(lableLevelLift,105, (sizeImegeFloor * i)+2,95,198,null);
			g.drawImage(fireLevel,250, (sizeImegeFloor * i),120,150,null);
			g.drawImage(fireLevel,460, (sizeImegeFloor * i),120,150,null);
		}

		//It does paint the opened door if method getOpen returns true else it does paint the closed door.
		
		if (controller.getOpen()) {
			g.drawImage(controller.getElevator().imgLiftOpen, 2,
					controller.liftY, null);
			g.drawImage(skinDoorL,10, controller.liftY+5,55,198,null);
			g.drawImage(skinDoorR,120, controller.liftY+5,80,198,null);
			g.drawImage(portal,40,
					controller.liftY+3,130,180,null);
			
		} else if (!controller.getOpen()) {
			
			g.drawImage(controller.getElevator().imgLiftClose, 2,
					controller.liftY, null);
			g.drawImage(lableLevelLift,4, controller.liftY+5,95,193,null);
			g.drawImage(lableLevelLift,105, controller.liftY+5,95,193,null);
			g.drawImage(fireLift,30, controller.liftY+50,45,65,null);
			g.drawImage(fireLift,130, controller.liftY+50,45,65,null);
	        g.drawString("AMOUNT PASSENGERS "+controller.getElevator().getElevatorContainer().size(),5,controller.liftY-2);
		}

		
		 //It does paint amount passengers what gone from elevator. 
		
		for (Floor f : floors) {
			synchronized (f.getArrivalStoryContainer()) {
				for (Passenger p : f.getArrivalStoryContainer()) {	
					g.drawString("ARRIVED PASSENGERS ON FLOOR ¹ "+p.getDestinationStory()+" AMOUNT"+f.getArrivalStoryContainer().size(),450,(ConstantElevator.storiesNumber * 200)+195
							- (200 * p.getDestinationStory()));
							

				}
			}

		}
		
		//It does paint passengers what need go from elevator
		
		synchronized (controller.getElevator().getElevatorContainer()) {
               
			for (Passenger p : controller.getElevator().getElevatorContainer()) {
                   
				if (p.getDestinationStory() == controller.getCurentLeve()) {
                   
					g.drawImage(Passenger.getimgPassenArrived(),
							((290 + p.getPassengerId()) - 285) + p.getX(),
							(ConstantElevator.storiesNumber * 200)-20
									- (200 * p.getDestinationStory()), 130,
							190, null);

				}

			}
		}

	    //It does paint passengers on levels. 
				
		for (Floor f : floors) {
			synchronized (f.getDispatchStoryContainer()) {
				for (Passenger p : f.getDispatchStoryContainer()) {
					g.drawImage(Passenger.getImgPassengerOnFloor(), (260 + p.getPassengerId() * 10)
							+ p.getX(), (ConstantElevator.storiesNumber * 200)
							- (200 * p.getStartStory())+10, 120, 180, null);
                    g.drawString("AMOUNT PASSENGERS ON FLOOR: "+f.getDispatchStoryContainer().size()+";",215,(ConstantElevator.storiesNumber * 200)
							- (200 * p.getStartStory())+195);
				}
			}

		}

	}

	// @Override
	public void actionPerformed(ActionEvent avent) {
		// TODO Auto-generated method stub
		if (controller.getElevator().getCountGoingOut() == ConstantElevator.passengersNumber) {
			start.setText("VIEW LOG FILE");
		}
		controller.botherConsol();
		controller.botherConsolOut();
		controller.botherConsolLiftUp();
		controller.botherConsolLiftDown();
		repaint();

	}

}
