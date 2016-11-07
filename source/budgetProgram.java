import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.io.*;

/*frames
frame
leftBox
*/

/*Panels
mainPanel
payControlsPanel
*/

/* Layout Managers
payControls
leftGridBag
*/


//Compile and Run
//cd ../source && javac -d ../classes budgetProgram.java && cd ../classes && java budgetProgram

public class budgetProgram{
	
	private JFrame frame; //frame for the entire program
	JPanel mainPanel; //create main panel until new panels made
	private JButton addPayment;
	private JButton delPayment;
	
	
	public static void main(String[] args){
		new budgetProgram().go();
	}
	
	
	public void go(){
		frame = new JFrame("Budget Yourself");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		Font titleFont = new Font("sanserif",Font.BOLD,36);
		 
		//insert contents
		//First, scrolling list of Payments. Empty for now.
		String[] testPayments = {"Payment 1", "Payment 2", "Payment 3"};
		JList<String> payList = new JList<String>(testPayments);
		 
		 
		//crate box layout manager containing scroller and flowlayout with buttons 
		Box leftBox = new Box(BoxLayout.Y_AXIS);
		
		
		//payScroller creation
		JScrollPane payScroller = new JScrollPane(payList);
		payScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		payScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		FlowLayout payControls = new FlowLayout();
		JPanel payControlsPanel = new JPanel(payControls);
		payControlsPanel.add(new JButton("Add"));
		payControlsPanel.add(new JButton("Remove"));
		payControlsPanel.setBackground(Color.GREEN);
		payControlsPanel.setMaximumSize(new Dimension(350,50));
		
		//add payScroller to layout manager
		//create panel to control components?
		leftBox.add(payScroller);
		leftBox.add(payControlsPanel);	
		
		
		//create center stats area
		JPanel statsPanel = new JPanel();
		//statsPanel.setBackground(Color.BLUE);
		//create border to put into panel, because '90s
		TitledBorder statsBorder = new TitledBorder(new LineBorder(Color.BLACK), "Stats",TitledBorder.CENTER,TitledBorder.ABOVE_TOP);
		statsPanel.setMaximumSize(new Dimension(850,800));
		statsPanel.setPreferredSize(new Dimension(750, 600));
		statsPanel.setMinimumSize(new Dimension(400,250));
		statsBorder.setTitleColor(Color.BLACK);
		statsPanel.setBorder(statsBorder);
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.add(statsPanel, gbc);


		//Dimension printDim = new Dimension(
		//System.out.println(Component.CENTER_ALIGNMENT);
		
		 
		 //Add simple panel to frame
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.getContentPane().add(BorderLayout.WEST,leftBox);
		frame.setSize(1000,800);
		frame.setVisible(true);
		 
		 
	}

}