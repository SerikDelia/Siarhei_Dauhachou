package epamlab.swing;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import epamlab.constant.ConstantElevator;
import epamlab.container.Floor;
import epamlab.people.Controller;
import epamlab.people.Passenger;

public class BuildingGraphics extends JComponent /*JPanel*/ implements ActionListener {
	Timer buildintimer = new Timer(30, this);
	private Controller controller;
	private List<Floor> floors = new ArrayList();
	private JButton start;

	Image imgBuilding = new ImageIcon("src/Resurces/Pictures/Floor.jpg")
			.getImage();
	Scrollbar scroll;
	List<Image> full = new ArrayList();
	Graphics g=null;
	int y = 200;

	public BuildingGraphics(List<Floor> floors, Controller controller, JButton start) {
		super();

		setLayout(new BorderLayout());
		//JScrollBar scroll=new JScrollBar(Scrollbar.VERTICAL,0,0,0,0);
		// add(scroll,BorderLayout.EAST);
		// ----------------------------------
		Rectangle rect = new Rectangle(0, 200, 747, 514);
		//setFocusable(true);
		setPreferredSize(getMaximumSize());
		this.floors = floors;
		this.controller = controller;
		this.start = start;
		buildintimer.start();

	}

	public void paint(Graphics g) {
		g = (Graphics2D) g;
		int i = 0;
		for (; i != ConstantElevator.storiesNumber; i++) {

			g.drawImage(imgBuilding, 0, y * i, null);

		}

		if (!controller.isFloorEmpty()
				|| !controller.getElevator().getElevatorContainer().isEmpty()) {
			g.drawImage(controller.getElevator().imgLiftOpen, 0,
					controller.liftY, null);
		} else if (!controller.isFloorEmpty()
				|| !controller.getElevator().getElevatorContainer().isEmpty()) {
			g.drawImage(controller.getElevator().imgLiftClose, 0,
					controller.liftY, null);
		}

		synchronized (controller.getElevator().getElevatorContainer()) {

			for (Passenger p : controller.getElevator().getElevatorContainer()) {

				if (p.getDestinationStory() == controller.getCurentLeve()) {

					g.drawImage(p.imgPassenArrived,
							((260 + p.getPassengerId()) - 250) + p.getX(),
							ConstantElevator.storiesNumber * 200
									- (200 * p.getDestinationStory()), 100,
							159, null);

				}

			}
		}

		for (Floor f : floors) {
			synchronized (f.getDispatchStoryContainer()) {
				for (Passenger p : f.getDispatchStoryContainer()) {
					g.drawImage(p.imgPassenger, (230 + p.getPassengerId() * 10)
							+ p.getX(), (ConstantElevator.storiesNumber * 200)
							- (200 * p.getStartStory()), 120, 180, null);

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
		repaint();

	}

}
