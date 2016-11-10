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
import java.text.*;
//import java.io.*;

/*frames
frame
newPayFrame
*/

/*Panels
mainPanel
payControlsPanel
statsPanel
rightPanel
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
		payScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		payScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		FlowLayout payControls = new FlowLayout();
		JPanel payControlsPanel = new JPanel(payControls);
		payControlsPanel.add(new JButton("Add")); //button implements actionListener, updateStats
		payControlsPanel.add(new JButton("Remove")); //button implements actionListener, updateStats
		payControlsPanel.setBackground(Color.GREEN);
		payControlsPanel.setMaximumSize(new Dimension(350,50));
		
		//add payScroller to layout manager
		//create panel to control components?
		leftBox.add(payScroller);
		leftBox.add(payControlsPanel);	
		
		
		//create center stats area
		JPanel statsPanel = new JPanel();
		//create border to put into panel, because '90s
		TitledBorder statsBorder = new TitledBorder(new LineBorder(Color.BLACK), "Stats",TitledBorder.CENTER,TitledBorder.ABOVE_TOP);
		statsPanel.setMaximumSize(new Dimension(850,800));
		statsPanel.setPreferredSize(new Dimension(750, 600));
		statsPanel.setMinimumSize(new Dimension(750,600));
		statsBorder.setTitleColor(Color.BLACK);
		statsPanel.setBorder(statsBorder);
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.add(statsPanel, gbc);

		//create right comboBox and checkboxes
		JPanel rightPanel = new JPanel(new GridBagLayout());
		rightPanel.setMinimumSize(new Dimension(600,600));
		
			//create comboBox, need to create public class and implement actionListener, will updateStats
			String[] dateRanges = {"Today", "This Week","This Month","Last Month", "This Year"};
			JComboBox dateRange = new JComboBox(dateRanges);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			rightPanel.add(dateRange, gbc);
			
			//gridBagLayout for the scrolling list of checkboxes
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridheight = 2;
			gbc.ipady = 400;
			gbc.ipadx = 125;
			//**** for testing. These need to be generated from the payment objects.
			String[] payTypes = {"Fun","Food","Gas","Rent","Utilities", "Athletics","Health","Cocaine"};

				//typeBorder setBorder for scrollablePanel goes in typeScroller goes in rightPanel
				JPanel scrollablePanel = new JPanel();
				TitledBorder typeBorder = new TitledBorder(new LineBorder(Color.black),"Payment Types",TitledBorder.CENTER,TitledBorder.BELOW_TOP);
				scrollablePanel.setBorder(typeBorder);
				scrollablePanel.setLayout( new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
				//create a JCheckBox for each String in payTypes
				for(String s: payTypes){
					scrollablePanel.add( new JCheckBox(s) );
				}
				
			//add scrollablePanel to typeScroller	
			JScrollPane typeScroller = new JScrollPane(scrollablePanel);	
			typeScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			typeScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			//add typeScroller to rightPanel
			rightPanel.add(typeScroller, gbc);
		
		 
		 //Add base panels to frame
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.getContentPane().add(BorderLayout.WEST,leftBox);
		frame.getContentPane().add(BorderLayout.EAST,rightPanel);
		frame.setSize(1200,1000);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		 
		 
	}
	
	public void newPaymentGo(){
		
		//make a new panel and populate it with a new instance of a Payment. We want to return that payment.
		JFrame newPayFrame = new JFrame("New Payment");
		Payment newPayment = new Payment();
		JPanel newPaymentPanel = new JPanel();
		newPaymentPanel.setLayout( new BoxLayout(newPaymentPanel,BoxLayout.Y_AXIS));
		
		//add fields to take new Payment input
		
		JTextField newPayName = new JTextField();
		JTextField newPayAmount = new JTextField();
		//JTextField newPayType = new JTextField();
		JTextField newPayDate = new JTextField();
		JTextArea newPayNote = new JTextArea(6,20);
		JButton saveNewPayment = new JButton("Save Payment");
		JLabel newPayNameLabel = new JLabel("Payment Name: ");
		newPayNameLabel.setLabelFor(newPayName);
		JLabel newPayAmountLabel = new JLabel("Payment Amount: ");
		newPayAmountLabel.setLabelFor(newPayAmount);
		JLabel newPayDateLabel = new JLabel("Payment Date: ");
		newPayDateLabel.setLabelFor(newPayDate);
		JLabel newPayNoteLabel = new JLabel("Payment Note: ");
		newPayNoteLabel.setLabelFor(newPayNote);
		
		//add button to save
		//button will run a method to save all the inputs
		
		//build UI
		newPaymentPanel.add(newPayNameLabel);
		newPaymentPanel.add(newPayName);
		newPaymentPanel.add(newPayAmountLabel);
		newPaymentPanel.add(newPayAmount);
		newPaymentPanel.add(newPayDateLabel);
		newPaymentPanel.add(newPayDate);
		newPaymentPanel.add(newPayNoteLabel);
		newPaymentPanel.add(newPayNote);
		newPaymentPanel.add(Box.createRigidArea( new Dimension(95, 0) ) );
		newPaymentPanel.add(Box.createRigidArea( new Dimension(0, 25) ) );
		newPaymentPanel.add(saveNewPayment);
		newPayFrame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		newPayFrame.getContentPane().add(newPaymentPanel,gbc);
		
		newPayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newPayFrame.setSize(400,350);
		newPayFrame.setVisible(true);
		newPayFrame.setLocationRelativeTo(null);
		
	}//end newPaymentGo()
	
	
	
	class AddNewPayment implements ActionListener{
		public void actionPerformed(ActionEvent event){
					new budgetProgram().newPaymentGo();
		}
	}
	
	
	
	class RemovePayment implements ActionListener{
		public void actionPerformed(ActionEvent event){
			
		}
	}
	
	
	
	/*
	What goes into Payments? 
	float Amount, payType Type, string datePaid, string dateAdded, string payNote, string payName
	*/
	public class Payment {
	
		private String payName;
		private float Amount;
		private payType Type;
		private String datePaid;
		private String dateAdded;
		private String payNote;

		private Payment(){
			String dateAdded = new SimpleDateFormat("MM/dd/yyyy").format( new java.util.Date() );
		}

		public void setName(String n){
			//verification stuff here
			payName = n;
		}

		public void setAmount(float amt){
			Amount = amt;
		}

		public void setType(){
			
		}

		public void setDatePaid(){
			//date validation here
		}

		public void setDateAdded(){
			//might be unnecessary
		}

		public void setPayNote(String note){
			//some sort of validation here
			payNote = note;
		}

		public String getName(){
			return payName;
		}

		public float getAmount(){
			return Amount;
		}

		public void getType(){
			
		}

		public String getDatePaid(){
			return datePaid;
		}

		public String getDateAdded(){
			return dateAdded;
		}

		public String getPayNote(){
			return payNote;
		}
	
	//a valid Payment REQUIRES an amount. Other data is just helpful.
	
	
	}
	
	public class payType{
	
		private String typeName;
		private boolean isEnabled;
		private int numPayments;
	
	}
	
	public class totalStats{
	
	//contains avg, max, min floats.
	//contains a method for refreshing calculation of those values with inputs.
	//not sure what the inputs are yet. A list of Payments?
	
	}
	
	

}