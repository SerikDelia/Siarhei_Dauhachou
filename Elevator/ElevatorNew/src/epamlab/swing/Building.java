package epamlab.swing;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import epamlab.constant.ConstantElevator;
import epamlab.container.Elevator;
import epamlab.container.Floor;
import epamlab.people.Controller;
import epamlab.people.Passenger;

public class Building extends JPanel implements ActionListener {
Timer buildintimer=new Timer(40, this);
//private Elevator elevator;
private Controller controller;
private List <Floor> floors=new ArrayList();
private JButton start;
Image imgBuilding=new ImageIcon("src/Resurces/Pictures/Floor.jpg").getImage();
Scrollbar scroll;
List <Image> full=new ArrayList();
Graphics g;
int y=200;

public Building(List <Floor> floors,Controller controller,JButton start) {
	super();
	setLayout(new BorderLayout());
	Scrollbar scroll=new Scrollbar(Scrollbar.VERTICAL,40,200,-(ConstantElevator.passengersNumber*200),(ConstantElevator.passengersNumber*200));	
	add(scroll,BorderLayout.EAST);
	this.floors=floors;
	this.controller=controller;
	this.start=start;
	buildintimer.start();
     
}

public void paint(Graphics g){
	g=(Graphics2D)g;
	int i=0;
	for( ;i!=ConstantElevator.storiesNumber;i++){	
	
		g.drawImage(imgBuilding,0,y*i,null);		
	
	//for(Floor f:floors){
  
	}
	
	
	if(!controller.isFloorEmpty()|| !controller.getElevator().getElevatorContainer().isEmpty()){
	    g.drawImage(controller.getElevator().imgLiftOpen,0,controller.liftY,null );
	   }else if(!controller.isFloorEmpty()|| !controller.getElevator().getElevatorContainer().isEmpty()){
		g.drawImage(controller.getElevator().imgLiftClose,0,controller.liftY,null );
	   }
	    
	synchronized (controller.getElevator().getElevatorContainer()) {
			
			
			for(Passenger p:controller.getElevator().getElevatorContainer()){
				int b=0;
			
					//b++;
				 if(p.getDestinationStory()==controller.getCurentLeve()) { 
				 
					 g.drawImage(p.imgPassenArrived,((260+p.getPassengerId())-250)+p.getX(),ConstantElevator.storiesNumber*200-(200*p.getDestinationStory()),100,159,null);
				 
				
				 }
				 
			}
	   }
	   
	    for(Floor f:floors){
			synchronized(f.getDispatchStoryContainer()){
			for(Passenger p :f.getDispatchStoryContainer()){
				//System.out.println("++++++"+p);
				g.drawImage(p.imgPassenger,(230+p.getPassengerId()*10)+p.getX(),(ConstantElevator.storiesNumber*200)-(200*p.getStartStory()),120,180,null);
				
				 		
				}
			}		
			
		}
	    
	    
	    
	
	 
	  
	
    
}

//@Override
public void actionPerformed(ActionEvent avent) {
	// TODO Auto-generated method stub
		if(controller.getElevator().getCountGoingOut()==ConstantElevator.passengersNumber){
			start.setText("VIEW LOG FILE");
		}
		controller.botherConsol();
		controller.botherConsolOut();
		repaint();
		
	
		
	
}

	
}
