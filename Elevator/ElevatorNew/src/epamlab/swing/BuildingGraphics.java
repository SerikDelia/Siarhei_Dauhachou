package epamlab.swing;

import java.awt.BorderLayout;
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
	Timer buildintimer = new Timer(30, this);
	private Controller controller;
	private List<Floor> floors = new ArrayList();
	private JButton start;
    Image levelView= new ImageIcon("src/Resurces/Pictures/fon.jpg").getImage();
	Image imgBuilding = new ImageIcon("src/Resurces/Pictures/Floor.jpg").getImage();
	Image portal= new ImageIcon("src/Resurces/Pictures/portal.gif").getImage();
	Image fireLift= new ImageIcon("src/Resurces/Pictures/ogon.gif").getImage();
	Image fireLevel= new ImageIcon("src/Resurces/Pictures/fire.gif").getImage();
	Image lableLevel= new ImageIcon("src/Resurces/Pictures/lable.gif").getImage();
	Image lableLevelLift= new ImageIcon("src/Resurces/Pictures/Q3Cover.jpg").getImage();
	Image skinDoorR= new ImageIcon("src/Resurces/Pictures/Q3CoverR.jpg").getImage();
	Image skinDoorL= new ImageIcon("src/Resurces/Pictures/Q3CoverL.jpg").getImage();
	Scrollbar scroll;
	List<Image> full = new ArrayList();
	Graphics g=null;
	int y = 200;

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
		for (; i != ConstantElevator.storiesNumber; i++) {

			
			g.drawImage(imgBuilding, 0, y * i,null);
			g.drawImage(levelView,200, y * i,700,120,null);
			g.drawImage(lableLevel,200, y * i,70,70,null);
			g.drawImage(lableLevel,400, y * i,70,70,null);
			g.drawImage(lableLevel,600, y * i,70,70,null);
			g.drawImage(lableLevelLift,5, (y * i)+2,95,198,null);
			g.drawImage(lableLevelLift,105, (y * i)+2,95,198,null);
			g.drawImage(fireLevel,250, (y * i),120,150,null);
			g.drawImage(fireLevel,460, (y * i),120,150,null);
		}

		if (controller.open()) {
			g.drawImage(controller.getElevator().imgLiftOpen, 2,
					controller.liftY, null);
			g.drawImage(skinDoorL,10, controller.liftY+5,55,198,null);
			g.drawImage(skinDoorR,120, controller.liftY+5,80,198,null);
			g.drawImage(portal,40,
					controller.liftY+3,130,180,null);
			
		} else if (!controller.open()) {
			
			g.drawImage(controller.getElevator().imgLiftClose, 2,
					controller.liftY, null);
			g.drawImage(lableLevelLift,4, controller.liftY+5,95,193,null);
			g.drawImage(lableLevelLift,105, controller.liftY+5,95,193,null);
			g.drawImage(fireLift,30, controller.liftY+50,45,65,null);
			g.drawImage(fireLift,130, controller.liftY+50,45,65,null);
		}

		synchronized (controller.getElevator().getElevatorContainer()) {
               
			for (Passenger p : controller.getElevator().getElevatorContainer()) {
                   
				if (p.getDestinationStory() == controller.getCurentLeve()) {
                   
					g.drawImage(p.imgPassenArrived,
							((290 + p.getPassengerId()) - 285) + p.getX(),
							(ConstantElevator.storiesNumber * 200)-20
									- (200 * p.getDestinationStory()), 130,
							190, null);

				}

			}
		}

		for (Floor f : floors) {
			synchronized (f.getDispatchStoryContainer()) {
				for (Passenger p : f.getDispatchStoryContainer()) {
					g.drawImage(p.imgPassenger, (260 + p.getPassengerId() * 10)
							+ p.getX(), (ConstantElevator.storiesNumber * 200)
							- (200 * p.getStartStory())+10, 120, 180, null);

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
