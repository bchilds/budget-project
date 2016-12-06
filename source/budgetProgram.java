import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.*;
import java.io.*;

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

public class budgetProgram implements Serializable{
	
	private JFrame frame; //frame for the entire program
	JPanel mainPanel; //create main panel until new panels made
	private JButton addPayment;
	private JButton delPayment;
	ArrayList<payType> payTypeList = new ArrayList<payType>();
	JPanel rightPanel = new JPanel(new GridBagLayout()); 
	JPanel scrollablePanel = new JPanel();
	private JComboBox dateRange;
	//ArrayList<JCheckBox> checkList = new ArrayList<JCheckBox>(); //could probably hash to a payType
	
	public static void main(String[] args){
		new budgetProgram().go();

	}
	
	
	public void go(){
		GridBagConstraints gbc = new GridBagConstraints();
		frame = new JFrame("Budget Yourself");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		Font titleFont = new Font("sanserif",Font.BOLD,36);

		//create the default model
		DefaultListModel<Payment> payListModel = new DefaultListModel<Payment>();
		
		//JList for Payments
		JList<Payment> payList = new JList<Payment> (payListModel);
		payList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION ); //prevents multiple selection
		
		//import any existing data. I wanted to make this its own method, but this worked best with the current design.
		try 
		{
			FileInputStream inFile = new FileInputStream("budgetYourselfData.data");
			ObjectInputStream is = new ObjectInputStream(inFile);
			JList inList = (JList) is.readObject();
			ListModel inModel = inList.getModel();
			for(int i = 0; i < inModel.getSize(); i++)
				{
					payListModel.addElement((Payment)inModel.getElementAt(i));
				}

			} catch (FileNotFoundException ex){
				//do nothing
				System.out.println("FileNotFound Exception.");
			} catch (InvalidClassException ex){
				//do nothing
				System.out.println("InvalidClass Exception.");
				ex.printStackTrace();
			} catch(Exception ex){
				ex.printStackTrace();
		}

		class rightPanelClass{
			
			public void createRightPanel()
			{	
				//RIGHT PANEL 

				//create right comboBox and checkboxes
				//JPanel rightPanel = new JPanel(new GridBagLayout());  INITIALIZED AT START
				//rightPanel.setMinimumSize(new Dimension(350,600));
		
				//create comboBox, need to create public class and implement actionListener, will updateStats
				String[] dateRanges = {"Today", "This Week","This Month","Last Week", "Last Month", "This Year", "Last Year"};
				if(!(dateRange == null))
				{
					//do nothing: JComboBox already created
				} else {
					dateRange = new JComboBox<String>(dateRanges); //build it if dateRange has not already been built
					dateRange.setSelectedIndex(2);
				}
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.insets = new Insets(0,30,0,30);
				rightPanel.add(dateRange, gbc);
		
		
				//gridBagLayout for the scrolling list of checkboxes
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.gridheight = 2;
				gbc.ipady = 50;
				//gbc.ipadx = 125;
				gbc.insets = new Insets(10,30,0,30);
		
		
				for(int i = 0; i < payListModel.getSize(); i++)
				{
					Payment tempPay = (Payment) payListModel.getElementAt(i);

					if( tempPay.getType() != null ) {
						if( !payTypeList.contains( tempPay.getType() ) ){
							payTypeList.add( tempPay.getType() );
						}
					}
			
				}

				//typeBorder setBorder for scrollablePanel goes in typeScroller goes in rightPanel
				//JPanel scrollablePanel = new JPanel();
				TitledBorder typeBorder = new TitledBorder(new LineBorder(Color.black),"Payment Types",TitledBorder.CENTER,TitledBorder.BELOW_TOP);
				scrollablePanel.setBorder(typeBorder);
				scrollablePanel.setLayout( new BoxLayout(scrollablePanel, BoxLayout.PAGE_AXIS));
		
		
				//create a JCheckBox for each payType in payTypeList
				scrollablePanel.removeAll(); //clean the list out to create all new components
				final JList<payType> checkList = new JList<payType>(createTypeArray(payTypeList));	
				checkList.setCellRenderer( new CheckListRenderer() );
				checkList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
				checkList.addMouseListener( new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e)
					{
						int index = checkList.locationToIndex( e.getPoint() );
						payType item = (payType) checkList.getModel().getElementAt(index);
						item.setSelected( !item.getSelected() );
						Rectangle rect = checkList.getCellBounds(index, index);
						checkList.repaint(rect);
					}
				});

				
				//add scrollablePanel to typeScroller
				JScrollPane typeScroller = new JScrollPane(checkList);	
				typeScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				typeScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				
				//add typeScroller to rightPanel
				scrollablePanel.add(typeScroller);
				rightPanel.add(scrollablePanel, gbc);

		
				gbc.gridx = 0;
				gbc.gridy = 2;
				gbc.gridheight = 1;
				gbc.insets = new Insets(0,0,0,0);
				
				frame.getContentPane().add(BorderLayout.EAST,rightPanel);
			}
			
			private payType[] createTypeArray(ArrayList<payType> list){
				int n = list.size();
				payType[] typeArray = new payType[n];
				for(int i = 0; i < n; i++)
				{
					typeArray[i] = list.get(i); //this gets the same element. Do I need to create a new one?
				}
				
				return typeArray;
			}
			
			class CheckListRenderer extends JCheckBox implements ListCellRenderer
			{
				public CheckListRenderer()
				{
					setBackground(UIManager.getColor("List.textBackground"));
     				setForeground(UIManager.getColor("List.textForeground"));
				}
				
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus ) 
				{
					setEnabled( list.isEnabled() );
					setSelected( ((payType) value).getSelected() );
					setFont( list.getFont() );
					setText( value.toString() );
					return this;
				}
			}
		}
		

		//create center stats area
		JPanel statsPanel = new JPanel();
		JPanel graphPanel = new JPanel();
		//create border to put into panel, because '90s
		TitledBorder statsBorder = new TitledBorder(new LineBorder(Color.BLACK), "Stats",TitledBorder.CENTER,TitledBorder.ABOVE_TOP);
		statsBorder.setTitleColor(Color.BLACK);
		TitledBorder graphBorder  = new TitledBorder( new LineBorder(Color.BLUE), "Graph",TitledBorder.CENTER, TitledBorder.ABOVE_TOP);
		graphBorder.setTitleColor(Color.BLACK);
		
		//statsPanel.setMaximumSize(new Dimension(650,400));
		statsPanel.setPreferredSize(new Dimension(650, 300));
		statsPanel.setMinimumSize(new Dimension(650,300));
		JPanel statsPanelLeft = new JPanel();
		JPanel statsPanelRight = new JPanel();
		statsPanel.setLayout( new BoxLayout(statsPanel, BoxLayout.LINE_AXIS) );
		//statsPanelLeft.setBackground(Color.green);
		statsPanelLeft.setLayout( new GridBagLayout() );
		statsPanelLeft.setPreferredSize( new Dimension(325,300) );
		statsPanelRight.setPreferredSize( new Dimension(325,1200) );
		//statsPanelRight.setMinimumSize( new Dimension(225, 300) );
		//statsPanelRight.setMaximumSize( new Dimension(325, 2000) );
		statsPanelRight.setLayout( new GridBagLayout() );
		statsPanel.add(statsPanelLeft);
		statsPanel.add( new JSeparator(SwingConstants.VERTICAL) );
		
		//create scroller for right stats panel
		JScrollPane statsScroller = new JScrollPane(statsPanelRight);	
		statsScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		statsScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		statsScroller.setBorder( null );
		//add the right panel
		statsPanel.add(statsScroller);
		
		
		graphPanel.setMaximumSize(new Dimension(650,500) );
		graphPanel.setPreferredSize(new Dimension(650,500) );
		graphPanel.setMinimumSize(new Dimension(650,500) );
		
		statsPanel.setBorder(statsBorder);
		graphPanel.setBorder(graphBorder);
		mainPanel.setLayout(new GridBagLayout());

		
		mainPanel.add(graphPanel, gbc);
		gbc.gridy = 1;
		gbc.insets = new Insets(10,0,0,0);
		mainPanel.add(statsPanel, gbc);


		//initial creation of rightPanel
		//placed here to initialize needed typeList values in totalStats class.
		new rightPanelClass().createRightPanel();

		//create stats class
		class totalStats
		{//Inner class of budgetProgram.go()
	
		//contains avg, max, min floats.
		//contains a method for refreshing calculation of those values with inputs.
		//not sure what the inputs are yet. A list of Payments?
	
			private double avg;
			private double max;
			private double min;
			private double selAvg;
			private double selMax;
			private double selMin;
			private int selCount;
			private JLabel totalAvgLabel;
			private JLabel totalMaxLabel;
			private JLabel totalMinLabel;
			private JLabel selAvgLabel;
			private JLabel selMaxLabel;
			private JLabel selMinLabel;
			ArrayList<payType> enabledTypes = new ArrayList<payType>();
			private JList selectedList;
			private String selectedDate;
			private Calendar calBefore;
			private Calendar calAfter;
			private Calendar calToday;
			private Calendar calPay;
			
			
			public totalStats(){
				//update stats based on payList and the enabled types
				this.updateStats(payList);
			}
		
			public void updateStats(JList payList)
			{
				avg = 0;
				max = 0;
				min = 0;
				selAvg = 0;
				selMax = 0;
				selMin = 0;
				selCount = 0;
				int typeIndex = -1;
				
				//"Today", "This Week","This Month","Last Week", "Last Month", "This Year", "Last Year"
				//Calendar section: Determine which selection was made and what the appropriate date range is
				calToday = Calendar.getInstance();
				calToday.set(Calendar.HOUR_OF_DAY,0);
				calToday.clear(Calendar.MINUTE);
				calToday.clear(Calendar.SECOND);
				calToday.clear(Calendar.MILLISECOND);
				calPay = (Calendar)calToday.clone();
				//zeros out the calendar to midnight, since we only care about day
				
				//get the selected time period
				selectedDate = dateRange.getSelectedItem().toString();
				switch ( selectedDate ) 
				{
				//logic will be: if(!( paymentDate < calBefore && paymentDate > calAfter  ) )
					case "Today": 
						//!(before today and after today)
						calBefore = (Calendar)calToday.clone();
						calAfter = (Calendar)calToday;
						break;
					case "This Week":
					//!(before first day of this week and after last day of this week)
						calBefore = (Calendar)calToday.clone();	//copy calToday's format
						calBefore.set(Calendar.DAY_OF_WEEK, calBefore.getFirstDayOfWeek());		//get the first day of this week
						calAfter = (Calendar)calBefore.clone();		//copy first day of this week
						calAfter.add(Calendar.DAY_OF_WEEK,6);	//add 6 more days to it to get the last day of this week
						break;
					case "This Month":
						calBefore = (Calendar)calToday.clone();	//copy calToday format, get today
						calBefore.set(Calendar.DAY_OF_MONTH,1); //set before to first day of this month
						calAfter = (Calendar)calBefore.clone();
						calAfter.add(Calendar.MONTH,1); //set calAfter to first day of next month
						calAfter.add(Calendar.DATE,-1); //set calAfter to last day of this month
						break;
					case "Last Week":
					//!(before first day of last week and after last day of last week)
						calBefore = (Calendar)calToday.clone();		//copy calToday's format
						calBefore.set(Calendar.DAY_OF_WEEK, calBefore.getFirstDayOfWeek());		//get the first day of this week
						calAfter = (Calendar)calBefore.clone(); 	//sets calAfter to the first day of this week
						calBefore.add(Calendar.DATE,-7); //gets the first day of last week
						calAfter.add(Calendar.DATE,-1);		//gets last day of last week
						break;
					case "Last Month":
					//!(before the first day of last month, after the last day of last month)
						calBefore = (Calendar)calToday.clone();	//copy calToday format, get today
						calBefore.set(Calendar.DAY_OF_MONTH,1); //set calBefore to first day of this month
						calAfter = (Calendar)calBefore.clone();	//set calAfter to the first day of this month
						calBefore.add(Calendar.MONTH,-1); //subtract a month from the first day of this month = first of last month
						calAfter.add(Calendar.DATE,-1);	//subtract a day from first day of this month, becomes last day of last month
						break;
					case "This Year":
						calBefore = (Calendar)calToday.clone();	//copy calToday format, get today
						calBefore.set(Calendar.MONTH,Calendar.JANUARY);
						calBefore.set(Calendar.DAY_OF_MONTH,1); //Jan 1 of this year
						calAfter = (Calendar)calBefore.clone();
						calAfter.set(Calendar.MONTH,Calendar.DECEMBER); 
						calAfter.set(Calendar.DAY_OF_MONTH,31);	//Dec. 31 of this year
						break;
					case "Last Year":
						calBefore = (Calendar)calToday.clone();	//copy calToday format, get today
						calBefore.set(Calendar.MONTH,Calendar.JANUARY);
						calBefore.add(Calendar.YEAR,-1);
						calBefore.set(Calendar.DAY_OF_MONTH,1); //Jan 1 of last year
						calAfter = (Calendar)calBefore.clone();
						calAfter.set(Calendar.MONTH,Calendar.DECEMBER); 
						calAfter.set(Calendar.DAY_OF_MONTH,31);	//Dec. 31 of last year
						break;
						
					//implement default:	
					//break;
				}
				
				//calBefore and calAfter are now set. Implement logic after payType.getSelected() to determine if the payment date is in range
				
				//create arrayList for enabled types
				enabledTypes.clear();
				for(payType pt: payTypeList)
				{
					if(pt.getSelected())
					{
						enabledTypes.add(pt);
					}
				}
				
			//create array of length of enabledTypes
				int sizeNum = enabledTypes.size();
				double typeStats[][] = new double[sizeNum][5];
			
				//create array of arrays for avg, max, min, totals of each type
				ListModel model = payList.getModel();
			
				//go through JList and do math on entire list of payments
				//go through JList and do math on entire list of SELECTED payments

				double total = 0;
				double selTotal = 0;
				
				for(int i=0; i < model.getSize(); i++)
				{
					Payment pay = (Payment) model.getElementAt(i); //gets the payment
				
					double  currentPay =  pay.getAmount();
					total = total + currentPay;

					if(currentPay > max)
					{
						max = currentPay;
					}
				
					if(i==0)
					{
						//for the first item, set baseline value
						min = currentPay;
					}
				
					if(currentPay < min)
					{
						min = currentPay;
					}
				
				
				
					//insert logic for checking to see if payment date is within selected range
					SimpleDateFormat newDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
					try{
						calPay.setTime(newDate.parse(pay.getDatePaid()));
					} catch (NullPointerException ex)
					{
						System.out.println("calPay null");
					} catch(Exception ex)
					{
						ex.printStackTrace();
					} 
					
					//System.out.println( !( calPay.before(calBefore) || calPay.after(calAfter) ));
				//daterange logic
				if( !(calPay.before(calBefore) || calPay.after(calAfter) ) ){
					try{
						if( pay.getType().getSelected() ) //if the payment type is selected
						{
					

					
							for(int j=0;j<enabledTypes.size();j++) //this section determines which index this payment type is in typeStats by name matching
							{
								if(pay.getType().getTypeName() == enabledTypes.get(j).getTypeName())
								{
									typeIndex = j;
								}
							}

							selCount = selCount+1;
							selTotal = selTotal + currentPay;
							if(i==0)
							{
								//for the first item, set baseline value
								selMin = currentPay;
							}
					
							if(currentPay > selMax)
							{
								selMax = currentPay;
							} 
					
							if (currentPay < selMin)
							{
								selMin = currentPay;
							}
					
							//now to do the typeStats-specific numbers. 0 = avg, 1 = max, 2 = min, 3 = total, 4 = # of items
							typeStats[typeIndex][0] = typeStats[typeIndex][0] + currentPay;
					
							if(currentPay > typeStats[typeIndex][1])
							{
								typeStats[typeIndex][1] = currentPay;
							}
							if((typeStats[typeIndex][2] == 0 && currentPay > 0) || currentPay < typeStats[typeIndex][2])
							{
								typeStats[typeIndex][2] = currentPay;
							}
					
							typeStats[typeIndex][3] = typeStats[typeIndex][3] + currentPay;
							typeStats[typeIndex][4] = typeStats[typeIndex][4] + 1;
						}	//end if
						} catch(ArrayIndexOutOfBoundsException ex) 
							{ 		
							ex.printStackTrace();
							}
						}
					}//end forloop iterate through all payments
				
					//add post-iterate logic
					if(model.getSize() > 0)
					{
						avg = total / model.getSize();
					}
					if(selCount > 0)
					{
						selAvg = selTotal / selCount;
					}
					
					//get the average for each selected type
					for(int k = 0; k < typeStats.length; k++)
					{
						if(typeStats[k][4] > 0) //is 0 by default, so no worries about null
						{
							typeStats[k][0] = typeStats[k][0]/typeStats[k][4];
						}
					}				
				
					//build UI with calculated values - could have used an array, didn't.
					JLabel totalAvgLabel = new JLabel("<HTML>The total average: " + NumberFormat.getCurrencyInstance().format(avg) + "</HTML>");
					JLabel totalMaxLabel = new JLabel("<HTML>The total max: " + NumberFormat.getCurrencyInstance().format(max) + "</HTML>");
					JLabel totalMinLabel = new JLabel("<HTML>The total min: " + NumberFormat.getCurrencyInstance().format(min) + "</HTML>");
					JLabel totalTotalLabel = new JLabel("<HTML>The total expenses are: " + NumberFormat.getCurrencyInstance().format(total) + "</HTML>");
					JLabel selAvgLabel = new JLabel("<HTML>The selected average: " + NumberFormat.getCurrencyInstance().format(selAvg) + "</HTML>");
					JLabel selMaxLabel = new JLabel("<HTML>The selected max: " + NumberFormat.getCurrencyInstance().format(selMax) + "</HTML>");
					JLabel selMinLabel = new JLabel("<HTML>The selected min: " + NumberFormat.getCurrencyInstance().format(selMin) + "</HTML>");
					JLabel selTotalTotalLabel = new JLabel("<HTML>The selected total expenses are: " + NumberFormat.getCurrencyInstance().format(selTotal) + "</HTML>");
				
					//reset GBC
					
					gbc.fill = GridBagConstraints.VERTICAL;
					gbc.weightx = 0;
					gbc.weighty = 0;
					gbc.insets = new Insets(10,0,0,0);
					gbc.gridheight = 1;
					gbc.gridwidth = 1;
					gbc.ipady = 0;
					gbc.ipadx = 0;
					gbc.gridy = 0;
					gbc.gridx = 0;
					gbc.anchor = GridBagConstraints.WEST;
					statsPanelLeft.add(totalAvgLabel,gbc);
					gbc.gridx = 0;
					gbc.gridy = 1;
					statsPanelLeft.add(totalMaxLabel,gbc);
					gbc.gridx = 0;
					gbc.gridy = 2;
					statsPanelLeft.add(totalMinLabel,gbc);
					gbc.gridx = 0;
					gbc.gridy = 3;		
					statsPanelLeft.add(totalTotalLabel,gbc);
					gbc.insets = new Insets(35,0,0,0);
					gbc.gridx = 0;
					gbc.gridy = 5;
					statsPanelLeft.add(selAvgLabel,gbc);
					gbc.insets = new Insets(10,0,0,0);
					gbc.gridx = 0;
					gbc.gridy = 6;
					statsPanelLeft.add(selMaxLabel,gbc);
					gbc.gridx = 0;
					gbc.gridy = 7;
					statsPanelLeft.add(selMinLabel,gbc);
					gbc.gridx = 0;
					gbc.gridy = 8;
					statsPanelLeft.add(selTotalTotalLabel,gbc);		
					
					//build the statsPanelRight
					gbc.gridy = 0;
					gbc.weightx = 1.0;
					gbc.weighty = 0;
					gbc.ipady = 5;
					gbc.anchor = GridBagConstraints.PAGE_START;
					gbc.insets = new Insets(0,0,0,0);
					enabledTypes.forEach(item->   //enabledTypes needs to be refreshed with the refresh action
					{
						//need to pull item index, get matching index in typeStats, build 4 labels and add to panel
						int getIndex = enabledTypes.indexOf(item);
						
						statsPanelRight.add( new JLabel("<HTML><u>" + item.getTypeName() + " stats:</u></HTML>"), gbc );
						gbc.gridy = gbc.gridy+1;
						statsPanelRight.add( new JLabel("Average: " + NumberFormat.getCurrencyInstance().format(typeStats[getIndex][0])), gbc);
						gbc.gridy = gbc.gridy+1;
						statsPanelRight.add( new JLabel("Max: " + NumberFormat.getCurrencyInstance().format(typeStats[getIndex][1])), gbc);
						gbc.gridy = gbc.gridy+1;
						statsPanelRight.add( new JLabel("Min: " + NumberFormat.getCurrencyInstance().format(typeStats[getIndex][2])), gbc);
						gbc.gridy = gbc.gridy+1;
						statsPanelRight.add( new JLabel("Total: " + NumberFormat.getCurrencyInstance().format(typeStats[getIndex][3])), gbc);
						gbc.gridy = gbc.gridy+1;
						statsPanelRight.add( new JLabel("Count: " + (int)typeStats[getIndex][4]), gbc);
						gbc.gridy = gbc.gridy+1;
						statsPanelRight.add( new JLabel(""),gbc);
						gbc.gridy = gbc.gridy+1;

												
					});
					gbc.weighty = 1;
					gbc.gridy = gbc.gridy+1;
					statsPanelRight.add( new JLabel(""),gbc); //fills the empty void and anchors to top
					gbc.weighty = 0; //resets to 0 for future layout			
			}//end updateStats
		
		
		}//end totalStats
		
		//Create content for the stats panel to display. Will use this to refresh stats. Might refer for graph?
		totalStats currentStats = new totalStats();
		
		
		class RefreshStats implements ActionListener//, ItemListener  //cannot use this inside of newPaymentGo() :(
		{
			public void actionPerformed(ActionEvent event)
			{	
				statsPanelLeft.removeAll();
				statsPanelRight.removeAll();
				currentStats.updateStats(payList);
				statsPanelLeft.revalidate();
				statsPanelLeft.repaint();
				statsPanelRight.revalidate();
				statsPanelRight.repaint();
				rightPanel.removeAll();
				new rightPanelClass().createRightPanel();
				rightPanel.revalidate();
				rightPanel.repaint();
			}
		}
		gbc.weighty = 1;
		gbc.gridy = 3;
		gbc.insets = new Insets(0,0,0,0);
		gbc.anchor = GridBagConstraints.PAGE_END;
		JButton refreshStatsButton = new JButton("Refresh");
		refreshStatsButton.addActionListener( new RefreshStats() );
		mainPanel.add(refreshStatsButton, gbc);
		
		
		//Will want to add a listener here for dateRange to refresh stats on new date select
		
		


		//crate box layout manager containing scroller and flowlayout with buttons 
		Box leftBox = new Box(BoxLayout.PAGE_AXIS);
		
		//payScroller creation
		JScrollPane payScroller = new JScrollPane(payList);
		payScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		payScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		FlowLayout payControls = new FlowLayout();
		JPanel payControlsPanel = new JPanel(payControls);
		
		//create actionListeners for buttons
		class AddNewPayment implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
					new budgetProgram().newPaymentGo(payListModel, payList, payTypeList); //add payTypeList
			}
		}
		
		class RemovePayment implements ActionListener{
			public void actionPerformed(ActionEvent event){
				//may want to add confirmation message? 
				//single selection is forced.
				if( !payList.isSelectionEmpty() )
				{
					payListModel.remove( payList.getSelectedIndex() );
					new budgetProgram().exportData(payList);
				}
			}
		}
		
		//create the Add button and its functionality
		addPayment = new JButton("Add");
		addPayment.addActionListener( new AddNewPayment() );
		payControlsPanel.add(addPayment);
		
		//create the Remove button and its  functionality
		delPayment = new JButton("Remove");
		delPayment.addActionListener ( new RemovePayment() );
		payControlsPanel.add(delPayment); //button implements actionListener, updateStats
		
		
		//payControlsPanel.setBackground(Color.GREEN);
		payControlsPanel.setMaximumSize(new Dimension(450,50));
		
		//add payScroller to layout manager
		leftBox.add(payScroller);
		leftBox.add(payControlsPanel);	
		
		
		 //Add base panels to frame
		frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
		frame.getContentPane().add(BorderLayout.WEST,leftBox);
		frame.setSize(1200,1000);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		
	 
	}//end go()
	
	public void newPaymentGo(DefaultListModel model, JList payList, ArrayList payTypeList)
	{
		
		//make a new panel and populate it with a new instance of a Payment. We want to return that payment.
		JFrame newPayFrame = new JFrame("New Payment");
		String today = new SimpleDateFormat("MM/dd/yyyy").format( new java.util.Date() );
		Payment newPayment = new Payment();
		JPanel newPaymentPanel = new JPanel();
		newPaymentPanel.setLayout( new BoxLayout(newPaymentPanel,BoxLayout.PAGE_AXIS));
		//add fields to take new Payment input
		
		//first, create actionListener for payType combo box
		
		class payTypeSelectListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				JComboBox cb = (JComboBox)event.getSource();
				if( ( !payTypeList.contains( cb.getSelectedItem() ) ) && ( event.getActionCommand().equals("comboBoxEdited") ) )
				{
					//Validate that type is not empty string so that we don't create empty Types by accident.
					if(!(cb.getSelectedItem().toString().trim().equals("")))
					{
						payType newType = new payType( (String)cb.getSelectedItem() );
						payTypeList.add(newType);
						cb.setSelectedItem(newType);
					} else {
						JOptionPane.showMessageDialog( null, "Please enter a valid type name", "Error", JOptionPane.ERROR_MESSAGE );
					}
					
				} else {
					//do nothing for now
				}
			}
		}

		JTextField newPayName = new JTextField();
		JTextField newPayAmount = new JTextField();
		JComboBox newPayType = new JComboBox();
			newPayType.setEditable(true);
			payTypeList.forEach( item-> newPayType.addItem(item) ); //adds all the payTypes into the combo box
			newPayType.addActionListener( new payTypeSelectListener() );
		JTextField newPayDate = new JTextField(today);
		JTextArea newPayNote = new JTextArea(6,20);
		newPayNote.setLineWrap(true);
		JScrollPane textAreaScroller = new JScrollPane(newPayNote);
		JLabel newPayNameLabel = new JLabel("Payment Name: ");
			newPayNameLabel.setLabelFor(newPayName);
		JLabel newPayAmountLabel = new JLabel("Payment Amount: ");
			newPayAmountLabel.setLabelFor(newPayAmount);
		JLabel newPayTypeLabel = new JLabel("Payment Type: ");
			newPayTypeLabel.setLabelFor(newPayType);
		JLabel newPayDateLabel = new JLabel("Payment Date: ");
			newPayDateLabel.setLabelFor(newPayDate);
		JLabel newPayNoteLabel = new JLabel("Payment Note: ");
			newPayNoteLabel.setLabelFor(newPayNote);
		
		//add button to save
		//button will run a method to save all the inputs
		
		//calls all the setter methods of the new payment from the fields in newPaymentGo() and adds it to JList
		
		class paymentSaveListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
				{
					
					//create regdata
					RegistrationData regData = new RegistrationData();
					regData.setName( newPayName.getText() );
					regData.setAmount( newPayAmount.getText() );
					regData.setDate1( newPayDate.getText() );
					regData.setType( (payType)newPayType.getSelectedItem() );
					//Validation
					//create list of validation rules to try
					ArrayList<RegistrationRule> rulesList = new ArrayList<RegistrationRule>();
					rulesList.add( new NameValidationRule() );
					rulesList.add( new AmountValidationRule() );
					rulesList.add( new DateValidationRule() );
					rulesList.add( new TypeValidationRule() );
					
				try //this is continuing to run even if error caught above. Can stop that? Could use boolean return from Validate instead of void
				{
					//try list of validation rules
					for( RegistrationRule rule: rulesList )
					{
						rule.validate(regData);
					}
				
					newPayment.setName( newPayName.getText() );
					newPayment.setAmount( Double.parseDouble( newPayAmount.getText() ) );
					newPayment.setType( (payType)newPayType.getSelectedItem() );
					newPayment.setPayNote( newPayNote.getText() );
					newPayment.setDatePaid( newPayDate.getText() );
					model.addElement(newPayment); //this adds the NAME to the list, meaning something gets added.
					//now we need to add it to the JList of Payments. Name displays with a ListCellRenderer or toString
					
					//SERIALIZATION OF DATA INTO SAVE FILE
					new budgetProgram().exportData(payList);
					
						//close JFrame
						newPayFrame.setVisible(false);
						newPayFrame.dispose();
				
				} catch(NumberFormatException ex) { //will only trigger on the Amount conversion to double
					JOptionPane.showMessageDialog( null, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE );
				
				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog( null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
				
				} catch(Exception ex) {
					ex.printStackTrace(); //to catch any errors that happen above after validation
				}
 
 					//eliminated Finally block to prevent errors being saved.
 
				}//close actionPerformed
		}//close paymentSaveListener
		
		
		
		JButton saveNewPayment = new JButton("Save Payment");
		saveNewPayment.addActionListener( new paymentSaveListener() );
		
		//build UI
		newPaymentPanel.add(newPayNameLabel);
		newPaymentPanel.add(newPayName);
		newPaymentPanel.add(newPayAmountLabel);
		newPaymentPanel.add(newPayAmount);
		newPaymentPanel.add(newPayTypeLabel);
		newPaymentPanel.add(newPayType);
		newPaymentPanel.add(newPayDateLabel);
		newPaymentPanel.add(newPayDate);
		newPaymentPanel.add(newPayNoteLabel);
		newPaymentPanel.add(textAreaScroller);
		newPaymentPanel.add(Box.createRigidArea( new Dimension(95, 0) ) );
		newPaymentPanel.add(Box.createRigidArea( new Dimension(0, 25) ) );
		newPaymentPanel.add(saveNewPayment);
		newPayFrame.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		newPayFrame.getContentPane().add(newPaymentPanel,gbc);
		
		newPayFrame.setSize(500,450);
		newPayFrame.setMaximumSize( new Dimension(500,450));
		newPayFrame.setLocationRelativeTo(null);
		newPayFrame.setVisible(true);
	
	}//end newPaymentGo()
	
	//data validation classes
	//create functional interface
	public interface RegistrationRule
	{
		public void validate(RegistrationData regData);
	}
	
	public class RegistrationData
	{
		private String name;
		private String amount;
		private String date1; //for if I want to do date validation
		private payType type;
		
		public void setName(String n)
		{
			name = n;
		}
		
		public void setAmount(String a)
		{
			amount = a;
		}
		
		public void setDate1(String d)
		{
			date1 = d;
		}
		
		public void setType(payType t)
		{
			type = t;
		}
	}
	
	public class NameValidationRule implements RegistrationRule
	{
		@Override
		public void validate(RegistrationData regData)
		{
			if(regData.name == null || regData.name.trim().equals(""))
			{
				throw new IllegalArgumentException("Please enter a name");
			}
		}
	}
	
	public class AmountValidationRule implements RegistrationRule
	{
		@Override
		public void validate(RegistrationData regData)
		{
			if(regData.amount == null || regData.amount.trim().equals(""))
			{
				throw new IllegalArgumentException("Please enter an amount");
			}
			
		}
	}
	
	public class DateValidationRule implements RegistrationRule
	{
		@Override
		public void validate(RegistrationData regData)
		{
			if(regData.date1 == null || regData.date1.trim().equals(""))
			{
				throw new IllegalArgumentException("Please enter a date");
			}
		}
	}
	
	public class TypeValidationRule implements RegistrationRule
	{
		@Override
		public void validate(RegistrationData regData)
		{
			if(regData.type == null || regData.type.toString().equals(""))
			{
				throw new IllegalArgumentException("Please select a type");
			}
		}
	}
	
	
	
	/*
	What goes into Payments? 
	double Amount, payType Type, string datePaid, string dateAdded, string payNote, string payName
	*/
	public class Payment implements Serializable
	{
	
		private String payName;
		private double Amount;
		private payType Type;
		private String datePaid;
		private String dateAdded;
		private String payNote; //when/if I implement an interface for viewing payment details, can be viewed.

		private Payment()
		{

		}

		public void setName(String n)
		{
			//validation stuff here
			payName = n;
		}

		public void setAmount(double amt)
		{
			Amount = amt;
		}

		public void setType(payType t){
			Type = t;
		}

		public void setDatePaid(String date)
		{
			//date validation here
			datePaid = date;
		}
		
		public void setDateAdded(String date)
		{
			//date validation here
			dateAdded = date;
		}

		public void setPayNote(String note)
		{
			//some sort of validation here
			payNote = note;
		}

		public String getName()
		{
			return payName;
		}

		public double getAmount()
		{
			return Amount;
		}

		public payType getType()
		{
			return Type;
		}

		public String getDatePaid()
		{
			return datePaid;
		}

		public String getDateAdded()
		{
			return dateAdded;
		}

		public String getPayNote()
		{
			return payNote;
		}
	
		public String toString()
		{
			return payName + " - " + NumberFormat.getCurrencyInstance().format(Amount) + " - " + datePaid;
		}
	
	//a valid Payment REQUIRES an amount and a name.
	}
	
	public class payType implements Serializable
	{
	
		private String typeName;
		private boolean isSelected;
		
		public payType(String s)
		{
			this.typeName = s;
			isSelected = true;
		}
		
		public String getTypeName()
		{
			return typeName;
		}
		
		public boolean getSelected()
		{
			return isSelected;
		}
		
		public void setSelected(boolean b)
		{
			isSelected = b;
		}
		
		public String toString()
		{
			return typeName;
		}
	
	}
	

	
	//Need a method for export
	public void exportData(JList payList)
	{
			//write with outputstream
		try {
			FileOutputStream f_out = new FileOutputStream("budgetYourselfData.data");
			ObjectOutputStream o_out = new ObjectOutputStream(f_out);
			o_out.writeObject(payList);
			o_out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}