package epamlab.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import epamlab.Runnable.TransportationTask;
import epamlab.constant.ConstantElevator;
import epamlab.container.Floor;
import epamlab.people.Controller;

public class MainWindows extends JFrame implements Runnable {
	private Thread mainWindows;
	private List<Floor> floors = new ArrayList();
	private Controller controller;
	List<TransportationTask> transportationTask = new ArrayList<TransportationTask>();
	File file = new File("src/Resurces/LogFile/Log.txt");
	JButton start;


	public MainWindows(List<Floor> floors, Controller controller,
			List<TransportationTask> transportationTask)
			throws HeadlessException {
		super("MainWindows");
		Dimension size =Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		this.setSize(1024,740);
		setLocation((int)((size.getWidth()-1024)/2), 
				((int)(size.getHeight()-740)/2));
		setPreferredSize(size);
		this.setResizable(false);
		start = new JButton("START");
		ActionListener headPressStartButton = new HandButtonEvent();
		start.addActionListener(headPressStartButton);
		add(start, BorderLayout.SOUTH);
		BuildingGraphics build = new BuildingGraphics(floors, controller, start);
		JScrollPane scroll=new JScrollPane(build);
		add(scroll, BorderLayout.CENTER);
		scroll.setPreferredSize(new Dimension(0,ConstantElevator.storiesNumber*200));
		add(new MessageDailog(), BorderLayout.EAST);
		this.floors = floors;
		this.controller = controller;
		this.transportationTask = transportationTask;
		mainWindows = new Thread(this);
		mainWindows.start();

	}

	class HandButtonEvent implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			synchronized (controller) {
				controller.notify();
			}

			if ("VIEW LOG FILE".equals(start.getText())) {
				try {
					Runtime.getRuntime().exec(
							"notepad.exe src/Resurces/LogFile/Log.log");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if ("ABORT".equals(start.getText())){

				for (TransportationTask t : transportationTask) {
					try {
						t.stop();

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.err.println("Run was stoped Hummen Operator");
					}
				}
                controller.validation();
				start.setText("VIEW LOG FILE");
			}

			if ("START".equals(start.getText())) {
				start.setText("ABORT");
			}
		}

	}

	public JButton getStartBotton() {
		return start;
	}

	public void run() {
		// TODO Auto-generated method stub

		Thread window = Thread.currentThread();
		if (window == mainWindows) {

			setVisible(true);
		}
	}
}
