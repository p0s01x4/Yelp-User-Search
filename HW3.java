
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;

public class HW3 {

	public static void main(String[] args) throws IOException {
		//Build the UI
		JFrame frame = new MainClass();
		frame.setTitle("Yelp Review Search");
		frame.setVisible(true);
		frame.setSize(1500, 1200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
	}
}

@SuppressWarnings("serial")
//MainClass is being inherited from JFrame
class MainClass extends JFrame {
	String SUBHEADINGCOLOR = "#FFFFCC";
	JList<String> categoryList;
	JList<String> subCategoryList;
	JList<String> attributeList;
	JList<String> resultList;
	JList<String> userList;

	String connectionString;
	String query;

	JButton businessQueryButton;
	JButton userQueryButton;
	JButton exitApplicationButton;

	JTable businessDisplayTable;
	JTable userDisplayTable;
	JTable reviewJTable;
	JTable userreviewJTable;

	JSplitPane resultPane;
	JSplitPane userresultPane;

	JPanel queryPanel;
	JPanel userPanel;

	DefaultTableModel resultTable;
	DefaultTableModel userresultTable;
	DefaultTableModel reviewTable;
	DefaultTableModel userreviewTable;

	JComboBox <String> fromdayComboBox;
	JComboBox <String> ANDOR;
	JComboBox <String> cheats;
	JComboBox <String> toComboBox;
	JComboBox <String> totimeComboBox;
	JComboBox <String> numCheckinComboBox;
	JComboBox <String> startsComboBox;
	JComboBox <String> votesComboBox;
	JComboBox <String> reviewCountLabelComboBox;
	JComboBox <String> friendsCountLabelComboBox;
	JComboBox <String> andOrLabelComboBox;
	JComboBox <String> avgStarComboBox;

	JTextField valueTextField;
	JTextField fromdateTextField;
	JTextField todateTextField;
	JTextField userValueTextField;
	JTextField votesValueTextField;
	JTextField countValueTextField;
	JTextField frndsCountValueTextField;
	JTextField memberSinceTextField;
	JTextField avgStarTextField;

	JSpinner.DateEditor memberSincede;
	JSpinner memberSincespinner;
	JSpinner.DateEditor fromdatede;
	JSpinner fromdatespinner;
	JSpinner.DateEditor todatede;
	JSpinner todatespinner;
	
	JTextArea queryField;

	DefaultListModel<String> addMainCategoryList = new DefaultListModel<String>();
	DefaultListModel<String> addSubCategoryList = new DefaultListModel<String>();
	DefaultListModel<String> addBidInList = new DefaultListModel<String>();
	DefaultListModel<String> addList = new DefaultListModel<String>();
	DefaultListModel<String> addAttributeList = new DefaultListModel<String>();
	DefaultListModel<String> addResultList = new DefaultListModel<String>();
	DefaultListModel<String> addUserResultList = new DefaultListModel<String>();

	//It provides us dynamic arrays in Java. It is helpful in programs where lots 
	//of manipulation in the array is needed.
	ArrayList<String> selectedMainCategoryList = new ArrayList<String>();
	ArrayList<String> selectedSubCategoryList = new ArrayList<String>();
	ArrayList<String> selectedAttributeList = new ArrayList<String>();
	ArrayList<String> selectedResultList = new ArrayList<String>();

	//To initialize the connection object to null.
	static Connection con = null;

	//Variable to define prepare statement for main category
	PreparedStatement ps = null;

	HashMap<Integer,String> bIDMap;
	HashMap<Integer,String> userMap;

	String StagePopulateCatQuery = "";
	String StagePopulateSubCatQuery = "";
	String StagePopulateAttriQuery = "";
	
	//Class to establish data connection
	public static void connect() 
	{
		try {
			//To register the oracle driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			//Use the connection object, since its already created during method creation.
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:ORCL", "books_admin", "MyPassword");
			if(con != null)
			{
				System.out.println("Connected to Database Successfully!");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	//Class to populate Main Category list

	//Default Constructor to provides the default 
	//values to the object like 0, null etc. depending on the type.
	//Class to build UI
	MainClass() throws IOException {

		bIDMap = new HashMap<Integer,String>();
		userMap = new HashMap<Integer,String>();

		categoryList = new JList<String>();
		JLabel mainCategoryLabel = new JLabel("Main Categories");
		Font f = mainCategoryLabel.getFont();
		mainCategoryLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel mainCategoryTitle = new JPanel();
		mainCategoryTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		mainCategoryTitle.add(mainCategoryLabel);
		JScrollPane mainCategoryContent = new JScrollPane();
		JSplitPane mainCategoryPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainCategoryTitle, mainCategoryContent);
		mainCategoryPane.setEnabled(false);
		mainCategoryContent.setViewportView(categoryList);

		//Pane Generation for Sub Category List 
		subCategoryList = new JList<>();
		JLabel subCategoryLabel = new JLabel("Sub-Categories");
		subCategoryLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel subCategoryTitle = new JPanel();
		subCategoryTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		subCategoryTitle.add(subCategoryLabel);
		JScrollPane subCategoryContents = new JScrollPane();
		JSplitPane subCategoryPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, subCategoryTitle, subCategoryContents);
		subCategoryPane.setEnabled(false);
		subCategoryContents.setViewportView(subCategoryList);

		//Pane Generation for Attribute List 
				attributeList = new JList<>();
				JLabel attributeLabel = new JLabel("Attributes");
				attributeLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
				JPanel attributeTitle = new JPanel();
				attributeTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
				attributeTitle.add(attributeLabel);
				JScrollPane attributeContents = new JScrollPane();
				JSplitPane attributePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, attributeTitle, attributeContents);
				attributePane.setEnabled(false);
				attributeContents.setViewportView(attributeList);
		
		
		// Pane generation for Check-in  
		JPanel CheckinPanel = new JPanel();
		CheckinPanel.setLayout(new BoxLayout(CheckinPanel, BoxLayout.Y_AXIS));
		JLabel CheckinLabel = new JLabel("AND / OR Selection");
		CheckinLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel CheckinTitle = new JPanel();
		CheckinTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		CheckinTitle.add(CheckinLabel);
		JScrollPane CheckinContents = new JScrollPane();
		JSplitPane CheckinPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, CheckinTitle, CheckinContents);
		CheckinPane.setEnabled(false);
		CheckinContents.setViewportView(CheckinPanel);

		ANDOR = new JComboBox<>();
		ANDOR.addItem("OR");
		ANDOR.addItem("AND");
		CheckinPanel.add(new JLabel("Search based on this!! "));
		CheckinPanel.add(ANDOR);
		ANDOR.setSelectedIndex(1);


		// Pane generation for Review
		JPanel reviewPanel = new JPanel();
		reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
		JLabel reviewLabel = new JLabel("Review");
		reviewLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel reviewTitle = new JPanel();
		reviewTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		reviewTitle.add(reviewLabel);
		JScrollPane reviewContents = new JScrollPane();
		JSplitPane reviewPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, reviewTitle, reviewContents);
		reviewPane.setEnabled(false);
		reviewContents.setViewportView(reviewPanel);

		//To get only date form JSpinner.

		JLabel fromdateLabel = new JLabel("From Date");
		reviewPanel.add(fromdateLabel);
		
		
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(1900, 04, 30);
		
		Calendar toCal = Calendar.getInstance();
		toCal.set(2018, 06, 02);
		
		Date fromdatedate = fromCal.getTime();
		SpinnerDateModel fromdatesm = new SpinnerDateModel(fromdatedate, null, null, Calendar.HOUR_OF_DAY);
		fromdatespinner = new JSpinner(fromdatesm);
		fromdatede = new JSpinner.DateEditor(fromdatespinner, "yyyy-MM-dd");
		fromdatespinner.setEditor(fromdatede);
		
		reviewPanel.add(fromdatespinner);
		
		
		JLabel todateLabel = new JLabel("To Date");
		reviewPanel.add(todateLabel);
		Calendar cal1 = Calendar.getInstance();
		Date todatedate = toCal.getTime();
		System.out.println(todatedate);
		SpinnerDateModel todatesm = new SpinnerDateModel(todatedate, null, null, Calendar.HOUR_OF_DAY);
		todatespinner = new JSpinner(todatesm);
		todatede = new JSpinner.DateEditor(todatespinner, "yyyy-MM-dd");
		todatespinner.setEditor(todatede);		
		reviewPanel.add(todatespinner);

		JLabel starsLabel = new JLabel("Stars:");
		reviewPanel.add(starsLabel);

		startsComboBox = new JComboBox<>();
		startsComboBox.addItem("=");
		startsComboBox.addItem(">");
		startsComboBox.addItem("<");

		reviewPanel.add(startsComboBox);
		startsComboBox.setSelectedItem(0);

		JLabel startValueLabel = new JLabel("Value");
		//fromLabel.setBounds(18,20,61,16);
		reviewPanel.add(startValueLabel);

		userValueTextField = new JTextField();
		reviewPanel.add(userValueTextField);

		JLabel votesLabel = new JLabel("Votes:");
		reviewPanel.add(votesLabel);

		votesComboBox = new JComboBox<>();
		votesComboBox.addItem("=");
		votesComboBox.addItem(">");
		votesComboBox.addItem("<");
		reviewPanel.add(votesComboBox);

		JLabel votesValueLabel = new JLabel("Value");
		//fromLabel.setBounds(18,20,61,16);
		reviewPanel.add(votesValueLabel);

		votesValueTextField = new JTextField();
		reviewPanel.add(votesValueTextField);

		// Pane Generation for Business Results
		resultTable = new DefaultTableModel();
		businessDisplayTable = new JTable();
		businessDisplayTable.setModel(resultTable);
		resultTable.addColumn("Business Name");
		resultTable.addColumn("City");
		resultTable.addColumn("State");
		resultTable.addColumn("Stars");

		JLabel resultlabel = new JLabel("Business Results");
		resultlabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel resultTitle = new JPanel();
		resultTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		resultTitle.add(resultlabel);
		JScrollPane result = new JScrollPane(businessDisplayTable);
		resultPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, resultTitle, result);

		resultPane.setEnabled(false);


		//Pane generation for bottom pane
		//Pane Generation for user List 
		userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
		JLabel userLabel = new JLabel("Users");
		userLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel userTitle = new JPanel();
		userTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		userTitle.add(userLabel);
		JScrollPane userContents = new JScrollPane();
		JSplitPane userPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userTitle, userContents);
		//userPane.setDividerLocation(40);
		//userPane.setDividerSize(2);
		userPane.setEnabled(false);
		userContents.setViewportView(userPanel);

		JLabel memberSinceLabel = new JLabel("Member Since:");
		//fromLabel.setBounds(18,20,61,16);
		userPanel.add(memberSinceLabel);

		memberSinceTextField = new JTextField();
		userPanel.add(memberSinceTextField);

		JLabel reviewCountLabel = new JLabel("Review Count:");
		userPanel.add(reviewCountLabel);

		reviewCountLabelComboBox = new JComboBox<>();
		reviewCountLabelComboBox.addItem("=");
		reviewCountLabelComboBox.addItem(">");
		reviewCountLabelComboBox.addItem("<");
		userPanel.add(reviewCountLabelComboBox);

		JLabel countValueLabel = new JLabel("Value");
		userPanel.add(countValueLabel);

		countValueTextField = new JTextField();
		userPanel.add(countValueTextField);

		JLabel freindsCountLabel = new JLabel("Num of Friends:");
		userPanel.add(freindsCountLabel);

		friendsCountLabelComboBox = new JComboBox<>();
		friendsCountLabelComboBox.addItem("=");
		friendsCountLabelComboBox.addItem(">");
		friendsCountLabelComboBox.addItem("<");
		userPanel.add(friendsCountLabelComboBox);

		JLabel freindsValueLabel = new JLabel("Value");
		//fromLabel.setBounds(18,20,61,16);
		userPanel.add(freindsValueLabel);

		frndsCountValueTextField = new JTextField();
		userPanel.add(frndsCountValueTextField);

		JLabel avgStarsLabel = new JLabel("Avg. Stars:");
		userPanel.add(avgStarsLabel);

		avgStarComboBox = new JComboBox<>();
		avgStarComboBox.addItem("=");
		avgStarComboBox.addItem(">");
		avgStarComboBox.addItem("<");
		userPanel.add(avgStarComboBox);

		JLabel avgStarLabel = new JLabel("Value");
		//fromLabel.setBounds(18,20,61,16);
		userPanel.add(avgStarLabel);

		avgStarTextField = new JTextField();
		userPanel.add(avgStarTextField);

		JLabel andOrLabel = new JLabel("Select:");
		userPanel.add(andOrLabel);

		andOrLabelComboBox = new JComboBox<>();
		andOrLabelComboBox.addItem("AND");
		andOrLabelComboBox.addItem("OR");
		userPanel.add(andOrLabelComboBox);

		// Pane for Execute Query Box
		queryField = new JTextArea();

		JLabel queryLabel = new JLabel("Query");
		queryLabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel queryTitle = new JPanel();
		queryTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		queryTitle.add(queryLabel);
		JScrollPane queryContents = new JScrollPane();
		JSplitPane queryPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, queryTitle, queryContents);
		//queryPane.setDividerLocation(40);
		//queryPane.setDividerSize(2);
		queryPane.setEnabled(false);
		queryContents.setViewportView(queryField);

		//Pane for "execute query" button
		JPanel executePane = new JPanel();
		businessQueryButton = new JButton("Business Query");
		businessQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				executeQueryActionPerformed(evt);
			}
		});
		executePane.add(businessQueryButton);

		userQueryButton = new JButton("User Query");
		userQueryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				userQuerryButtonActionPerformed(evt);
			}
		});
		executePane.add(userQueryButton);

		//Pane for "Exit" button
		//JPanel closePane = new JPanel();
		exitApplicationButton = new JButton("Exit");
		exitApplicationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		executePane.add(exitApplicationButton);

		// Pane Generation for User Results
		userresultTable = new DefaultTableModel();
		userDisplayTable = new JTable();
		userDisplayTable.setModel(userresultTable);
		userresultTable.addColumn("Name");
		userresultTable.addColumn("Yelping Since");
		userresultTable.addColumn("Stars");

		JLabel userresultlabel = new JLabel("User Results");
		userresultlabel.setFont(new Font(f.getFontName(), Font.PLAIN, 20));
		JPanel userresultTitle = new JPanel();
		userresultTitle.setBackground(Color.decode(SUBHEADINGCOLOR));
		userresultTitle.add(userresultlabel);
		JScrollPane userresult = new JScrollPane(userDisplayTable);
		userresultPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userresultTitle, userresult);

		userresultPane.setEnabled(false);

		// Creating Top Panes
		// Creating a Pane for Main and Sub Categories and Attributes
		JSplitPane firstColumnPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainCategoryPane, subCategoryPane);
		firstColumnPane.setDividerLocation(200);
		firstColumnPane.setEnabled(false);

		JSplitPane secondAttriColumnPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, firstColumnPane, attributePane);
		secondAttriColumnPane.setDividerLocation(400);
		secondAttriColumnPane.setEnabled(false);		
		
		JSplitPane secondColumnPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, secondAttriColumnPane, CheckinPane);
		secondColumnPane.setDividerLocation(600);
		secondColumnPane.setEnabled(false);

		
		JSplitPane thirdColumnPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, secondColumnPane, reviewPane);
		thirdColumnPane.setDividerLocation(800);
		thirdColumnPane.setEnabled(false);

		JSplitPane mainTopPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, thirdColumnPane, resultPane );
		mainTopPane.setDividerLocation(1000);
		mainTopPane.setEnabled(false);

		// Creating Bottom Panes
		// Creating a Pane for Main and Sub Categories
		JSplitPane buttonSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, queryPane, executePane);
		buttonSplitPane.setDividerLocation(400);;
		buttonSplitPane.setEnabled(false);

		JSplitPane bottomFirstColumnPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userPane, buttonSplitPane);
		bottomFirstColumnPane.setDividerLocation(300);
		bottomFirstColumnPane.setEnabled(false);

		JSplitPane bottomSecondColumnPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bottomFirstColumnPane, userresultPane);
		bottomSecondColumnPane.setDividerLocation(300);

		JSplitPane mainBottomPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,bottomFirstColumnPane, bottomSecondColumnPane);
		mainBottomPane.setDividerLocation(600);
		mainBottomPane.setEnabled(false);

		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainTopPane, mainBottomPane);
		mainPane.setDividerLocation(400);
		mainPane.setEnabled(false);
		connectionString = "INTERSECT";
		//connectionString = "AND"

		
		//FInally adding all the panes to the main pane
		getContentPane().add(mainPane);
		connect();

		//Update AND or
		updateUnionIntersect();

		
		populateMainCategory();
		populateSubCategoryList();
		populateAttributeList();
		mouseLisetenerAttribute();
		
		openReviewFrame();
		populateUserResult();
		openUserFrame();
	}

	private void populateMainCategory() {
		
		userValueTextField.setText("");
		votesValueTextField.setText("");
		
		StagePopulateCatQuery = "";
		StagePopulateCatQuery = "SELECT DISTINCT C_NAME FROM B_MAIN_CATEGORY ORDER BY C_NAME\n";
		try {
			ResultSet rs11 = null;
			ps=con.prepareStatement(StagePopulateCatQuery);
			rs11 = ps.executeQuery(StagePopulateCatQuery);
			int i = 0;
			while(rs11.next())
			{
				if(!addMainCategoryList.contains(rs11.getString("C_NAME")))
				{
					addMainCategoryList.addElement(rs11.getString("C_NAME"));
				}
			}
			ps.close();
			rs11.close();
		} catch(Exception ex) {
			System.out.println(ex);
		}
		categoryList.setModel(addMainCategoryList);
	}
	
	
	private void populateSubCategoryList() {
		MouseListener mainCategoryMouseListener = new MouseAdapter() {			
			public void mouseClicked(MouseEvent e) {
				userValueTextField.setText("");
				votesValueTextField.setText("");

				if (businessDisplayTable.getRowCount() > 0) {
					for (int i = businessDisplayTable.getRowCount() - 1; i > -1; i--) {
						resultTable.removeRow(i);
					}
				}
				
				StagePopulateSubCatQuery = "";
				addSubCategoryList.clear();
				addAttributeList.clear();
				selectedMainCategoryList = (ArrayList<String>) categoryList.getSelectedValuesList();
				
				StagePopulateSubCatQuery = "SELECT DISTINCT BSC.C_NAME from b_main_category bmc\r\n" + 
						"inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n" + 
						"where BMC.C_NAME = '" + selectedMainCategoryList.get(0)+ "' ";
				
				if (connectionString == "INTERSECT") {
					for(int i=1;i<selectedMainCategoryList.size();i++) {
						StagePopulateSubCatQuery = StagePopulateSubCatQuery + " INTERSECT SELECT DISTINCT BSC.C_NAME from b_main_category bmc\r\n" + 
														"inner join b_sub_category bsc on bmc.bid=bsc.bid \r\n" + 
														"where BMC.C_NAME = '" + selectedMainCategoryList.get(i) + "'";
					}
				}
				else if (connectionString == "UNION") {
					for(int i=1;i<selectedMainCategoryList.size();i++) {
						StagePopulateSubCatQuery = StagePopulateSubCatQuery + " UNION SELECT DISTINCT BSC.C_NAME from b_main_category bmc\r\n" + 
														"inner join b_sub_category bsc on bmc.bid=bsc.bid \r\n" + 
														"where BMC.C_NAME = '" + selectedMainCategoryList.get(i) + "'";
					}
				} 	
				try {
				
					ResultSet rs12 = null;
					ps=con.prepareStatement(StagePopulateSubCatQuery);
					rs12 = ps.executeQuery(StagePopulateSubCatQuery);
					
					while(rs12.next())
					{
						if(!addSubCategoryList.contains(rs12.getString("C_NAME"))){
							addSubCategoryList.addElement(rs12.getString("C_NAME"));
						}
					}
					ps.close();
					rs12.close();
				}catch(Exception ex) {
						System.out.println(ex);
				}
				subCategoryList.setModel(addSubCategoryList);				
			}
		};
		categoryList.addMouseListener(mainCategoryMouseListener);
	}

	
	
	private void populateAttributeList() {
		MouseListener subCategoryMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				userValueTextField.setText("");
				votesValueTextField.setText("");

				StagePopulateAttriQuery= "";
				addAttributeList.clear();
				
				selectedSubCategoryList = (ArrayList<String>) subCategoryList.getSelectedValuesList();
				selectedMainCategoryList = (ArrayList<String>) categoryList.getSelectedValuesList();
				
				StagePopulateAttriQuery = "select distinct ba.a_name from b_main_category bmc\r\n" + 
						"inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n" +
						"inner join b_attributes ba on bmc.bid=ba.bid\r\n "
						+ "Where bmc.c_name = '" + selectedMainCategoryList.get(0) 
						+ "' and "
						+ "bsc.c_name = '" + selectedSubCategoryList.get(0) + "'\n";
				if (selectedSubCategoryList.size() > 0) {
				for(int i=0;i<selectedMainCategoryList.size();i++) {
					for(int j=1;j<selectedSubCategoryList.size();j++) {
						StagePopulateAttriQuery = StagePopulateAttriQuery + connectionString + " select distinct ba.a_name from b_main_category bmc\r\n"
									+ "inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n"
									+ "inner join b_attributes ba on bmc.bid=ba.bid\r\n "
									+ "Where bmc.c_name = '" + selectedMainCategoryList.get(i) 
									+ "' and "
									+ "bsc.c_name = '" + selectedSubCategoryList.get(j) + "' \n";
					}
					break;
				}
				}
				for(int i=1;i<selectedMainCategoryList.size();i++) {
					for(int j=0;j<selectedSubCategoryList.size();j++) {
						StagePopulateAttriQuery = StagePopulateAttriQuery + connectionString + " select distinct ba.a_name from b_main_category bmc\r\n"
									+ "inner join b_sub_category bsc on bmc.bid=bsc.bid\r\n"
									+ "inner join b_attributes ba on bmc.bid=ba.bid\r\n "
									+ "Where bmc.c_name = '" + selectedMainCategoryList.get(i) 
									+ "' and "
									+ "bsc.c_name = '" + selectedSubCategoryList.get(j) + "' \n";
					}
				}
				
				try {
					ResultSet rs12 = null;
					ps=con.prepareStatement(StagePopulateAttriQuery);
					rs12 = ps.executeQuery(StagePopulateAttriQuery);
					
					while(rs12.next())
					{
						if(!addAttributeList.contains(rs12.getString("A_NAME"))){
							addAttributeList.addElement(rs12.getString("A_NAME"));
						}
					}
					ps.close();
					rs12.close();
				}catch(Exception ex) {
						System.out.println(ex);
				}
				attributeList.setModel(addAttributeList);				
			}
		};
		subCategoryList.addMouseListener(subCategoryMouseListener);
	}
	
	
	
	private void mouseLisetenerAttribute() {
		MouseListener attributeMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				userValueTextField.setText("");
				votesValueTextField.setText("");
				selectedAttributeList = (ArrayList<String>) attributeList.getSelectedValuesList();
			}
		};
		attributeList.addMouseListener(attributeMouseListener);		
	}
	
	
	
	@SuppressWarnings("unlikely-arg-type")
	private void executeQueryActionPerformed(ActionEvent e) {

		if (businessDisplayTable.getRowCount() > 0) {
			for (int i = businessDisplayTable.getRowCount() - 1; i > -1; i--) {
				resultTable.removeRow(i);
			}
		}

		if(!selectedMainCategoryList.isEmpty()) {
		
			addResultList.clear();
			String firstFinalQuery = "";
			
			firstFinalQuery = "SELECT distinct BS.B_NAME, BS.CITY, BS.STATE, BS.STARS, BS.BID from business BS\r\n";
			
			if(!selectedMainCategoryList.isEmpty()) {
				firstFinalQuery += "inner join b_main_category bms on bs.bid=bms.bid\r\n";
				if(!selectedSubCategoryList.isEmpty()) {
					firstFinalQuery += "inner join b_sub_category bsc on bsc.bid=bms.bid\r\n";
					if(!selectedAttributeList.isEmpty()) 
						firstFinalQuery += "inner join b_attributes ba on ba.bid=bsc.bid\r\n";
				}
			}
			
			firstFinalQuery += " where \r\n";
			String subQuery = firstFinalQuery;
			
			if(!selectedMainCategoryList.isEmpty()) {
				for(int i=0; i<selectedMainCategoryList.size();i++) {	
					if(!selectedSubCategoryList.isEmpty()) {
						for(int j=0;j<selectedSubCategoryList.size();j++) {
							
							//if attribute is selected along with sub and cate
							if(!selectedAttributeList.isEmpty()) {
								for(int k=0;k<selectedAttributeList.size(); k++) {
									firstFinalQuery += " bms.c_name= '" +selectedMainCategoryList.get(i)+ "' and \r\n"+ 
											"bsc.c_name= '" +selectedSubCategoryList.get(j)+ "' and \r\n"+
											"ba.a_name= '" +selectedAttributeList.get(k)+ "' \r\n";
									if((i+1 < selectedMainCategoryList.size()) || 
											(j+1 < selectedSubCategoryList.size()) || 
											(k+1 < selectedAttributeList.size())) {
										firstFinalQuery += connectionString +" "+ subQuery;
									}
								}	
							}
							
							//if just sub and main is selected
							if(selectedAttributeList.isEmpty()) {
								firstFinalQuery += " bms.c_name= '" +selectedMainCategoryList.get(i)+ "' and \r\n"+ 
										"bsc.c_name= '" +selectedSubCategoryList.get(j)+ "' \r\n";
								if((i+1 < selectedMainCategoryList.size()) || 
										(j+1 < selectedSubCategoryList.size())) {
									firstFinalQuery += connectionString +" "+ subQuery;
								}
							}
						}
					}
					
					//if its just main category
					if(selectedSubCategoryList.isEmpty()) {
						firstFinalQuery += " bms.c_name= '" +selectedMainCategoryList.get(i)+ "' \r\n"; 
						if((i+1 < selectedMainCategoryList.size()))
							firstFinalQuery += connectionString +" "+ subQuery;
					}
				}
			}
					
			int tracker=0;
			String finalQuery="";
			
			//the below query
			
			if(!userValueTextField.getText().isEmpty() || !votesValueTextField.getText().isEmpty() ||
					(!fromdatede.getTextField().getText().toString().equals("1900-05-30")) ||
					(!todatede.getTextField().getText().toString().equals("2018-07-02"))) {
				int votesTracker=0;
				if (!votesValueTextField.getText().isEmpty()) {
					tracker=1;
					finalQuery = "select * from (" + firstFinalQuery + ") abc where abc.bid in (select r.bid from reviews r where r.FUNNY_VOTE + "
							+ "r.USEFUL_VOTE + r.COOL_VOTE " + votesComboBox.getSelectedItem() + votesValueTextField.getText() + ") \n";
					System.out.println("inside votes yes");
					votesTracker=1;
				}
			
				int fromDateFlag=0;	
				System.out.println("de ..."+todatede);
				
				if (!fromdatede.getTextField().getText().toString().equals("1900-05-30")) {
					fromDateFlag=1;
					tracker=1;
					if(votesTracker==1) {
						System.out.println("inside votes and from date yes");
						finalQuery = "select * from (" + firstFinalQuery + ") abc where abc.bid in (select r.bid from reviews r where r.FUNNY_VOTE + "
							+ "r.USEFUL_VOTE + r.COOL_VOTE " + votesComboBox.getSelectedItem() + votesValueTextField.getText() 
							+ " and r.R_DATE>to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				else {
					System.out.println("inside votes no fromdate yes");
					tracker=1;
				finalQuery += "select * from (" + firstFinalQuery + ") abc "
						+ "where abc.bid in (select r.bid from reviews r "
						+ "where R.R_DATE > to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd') \n";
				}
					fromDateFlag=1;
			}
			
			if(!todatede.getTextField().getText().toString().equals("2018-07-02")) {
				if(votesTracker==1 && fromDateFlag==1) {
					tracker=1;
					System.out.println("inside vote and fromdate yes and to date yes");
					finalQuery = "select * from (" + firstFinalQuery + ") abc where abc.bid in (select r.bid from reviews r where (r.FUNNY_VOTE "
							+ "+ r.USEFUL_VOTE + r.COOL_VOTE) " + votesComboBox.getSelectedItem() + votesValueTextField.getText()
							+ " and r.R_DATE>to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd') \n"
							+ "and r.R_DATE<to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				else if(votesTracker==1 && fromDateFlag==0) {
					System.out.println("inside votes yes fromdate no to date yes");
					tracker=1;
					finalQuery = "select * from (" + firstFinalQuery + ") abc where abc.bid in (select r.bid from reviews r where (r.FUNNY_VOTE "
							+ "+ r.USEFUL_VOTE + r.COOL_VOTE) " + votesComboBox.getSelectedItem() + votesValueTextField.getText()
							+ " and r.R_DATE < to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n ";
				}
				else if(votesTracker==0 && fromDateFlag==1) {
					System.out.println("inside votes no fromdate yes to date yes");
					tracker=1;
					finalQuery = "select * from (" + firstFinalQuery + ") abc "
							+ "where abc.bid in (select r.bid from reviews r "
							+ "where R.R_DATE > to_date('"+ fromdatede.getTextField().getText() +"','yyyy-MM-dd') \n"
							+ "and r.R_DATE < to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				else {
					System.out.println("insdie todate yes");
					tracker=1;
					finalQuery = "select * from (" + firstFinalQuery + ") abc "
							+ "where abc.bid in (select r.bid from reviews r "
							+ "where R.R_DATE < to_date('"+ todatede.getTextField().getText() +"','yyyy-MM-dd')) \n";
				}
				tracker=1;
			}
			
			if (!userValueTextField.getText().isEmpty()) {
				float starsCount = Float.parseFloat((String) userValueTextField.getText());
				if(tracker==1) {
					finalQuery += " and abc.stars " +startsComboBox.getSelectedItem() + starsCount +" \n"  ;
				}
				else
					finalQuery = "select * from (" + firstFinalQuery + ") abc where stars" + 
					 startsComboBox.getSelectedItem() + starsCount +" \n" ;
				tracker=1;
			}
			
			}	
			if(finalQuery != "") {
				firstFinalQuery = finalQuery;
			}
			
			System.out.println(firstFinalQuery);
			
			try {
				ResultSet rs13 = null;
				ps=con.prepareStatement(firstFinalQuery);
				rs13 = ps.executeQuery(firstFinalQuery);
				int i =0;
				String[] rowObj = new String[4];
				while(rs13.next())
				{
					rowObj = new String[] {rs13.getString("B_NAME"), rs13.getString("CITY"), rs13.getString("STATE"), rs13.getString("STARS")};
					resultTable.addRow(rowObj);
					bIDMap.put(i++, rs13.getString("BID"));
				}
				queryField.setText(firstFinalQuery);
				ps.close();
				rs13.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}	
	
	private void openReviewFrame() { 
		businessDisplayTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JFrame frame = new JFrame("JFrame Example");

					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout());
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					System.out.println("Inside Query");
					String reviewFrameQuery = "";
					reviewFrameQuery = "SELECT R.R_DATE, R.STARS, R.R_TEXT, Y.USER_NAME, R.USEFUL_VOTE, R.FUNNY_VOTE, R.COOL_VOTE\r\n" + 
							"FROM REVIEWS R, BUSINESS B, YELP_USER Y\r\n" + 
							"WHERE R.USER_ID = Y.USER_ID AND B.BID = R.BID AND B.BID = '"+bIDMap.get(row)+"'"; 
					
					if(!votesValueTextField.getText().isEmpty()) {
						reviewFrameQuery += " and ((r.FUNNY_VOTE + r.USEFUL_VOTE + r.COOL_VOTE) "
								+ votesComboBox.getSelectedItem() + votesValueTextField.getText()+" )";
					}
					if(!userValueTextField.getText().isEmpty()) {
						reviewFrameQuery += " and r.stars "+ startsComboBox.getSelectedItem() 
						+ userValueTextField.getText() +" \n"  ;
					}
									
					reviewJTable = new JTable();
					reviewJTable.setBounds(300,400,1000,1000);

					reviewTable = new DefaultTableModel();
					reviewJTable.setModel(reviewTable);
					reviewTable.addColumn("Review Date");
					reviewTable.addColumn("Stars");
					reviewTable.addColumn("Review");
					reviewTable.addColumn("User Name");
					reviewTable.addColumn("Useful Vote");
					reviewTable.addColumn("Funny Vote");
					reviewTable.addColumn("Cool Vote");

					JScrollPane reviewResultPane = new JScrollPane(reviewJTable);
			
					panel.add(reviewResultPane);
			
					String[] rowObj = new String[7];
					try {
						ResultSet rs13 = null;
						System.out.println(reviewFrameQuery);
						ps=con.prepareStatement(reviewFrameQuery);
						rs13 = ps.executeQuery(reviewFrameQuery);

						while(rs13.next())
						{

							rowObj = new String[] {rs13.getString("R_DATE"), rs13.getString("STARS"), rs13.getString("R_TEXT"), rs13.getString("USER_NAME"), rs13.getString("USEFUL_VOTE"), rs13.getString("FUNNY_VOTE"), rs13.getString("COOL_VOTE")};
				
							reviewTable.addRow(rowObj);


						}
						ps.close();
						rs13.close();
					} catch(Exception ex) {
						System.out.println(ex);
					}
					frame.add(panel);
					frame.setSize(1800, 1200);
					frame.setLocationRelativeTo(null);
			
					frame.setVisible(true);
				}
			}
		});

	}

	private void populateUserResult() {
		MouseListener userMouseListener = new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{

				if (e.getClickCount() == 1) {
					if (businessDisplayTable.getRowCount() > 0) {
						for (int i = businessDisplayTable.getRowCount() - 1; i > -1; i--) {
							resultTable.removeRow(i);
						}
					}
				}

				System.out.println("value of user field:" + countValueTextField.getText());
				if((Integer.parseInt((String) countValueTextField.getText())) !=0) {
					queryField.setText(" ");
					if (businessDisplayTable.getRowCount() > 0) {
						for (int i = businessDisplayTable.getRowCount() - 1; i > -1; i--) {
							resultTable.removeRow(i);
						}
					}

					String FinalSubQuery = "";
					int reviewCount = Integer.parseInt((String) countValueTextField.getText());

					System.out.println("Inside 4th Query");
					FinalSubQuery += "SELECT DISTINCT YU.USER_NAME, YU.YELPING_SINCE, YU.AVERAGE_STARS \r\n" + 
							"FROM YELP_USER YU \r\n" + 
							"WHERE YU.REVIEW_COUNT " + reviewCountLabelComboBox.getSelectedItem() +
							" " + reviewCount;


					System.out.println("--------");
					System.out.println(FinalSubQuery);
					System.out.println("--------");

					queryField.setText(FinalSubQuery);

					try {
						ResultSet rs13 = null;
						ps=con.prepareStatement(FinalSubQuery);
						rs13 = ps.executeQuery(FinalSubQuery);
						int i =0;
						String[] rowObj = new String[4];

						while(rs13.next()){
							rowObj = new String[] {rs13.getString("USER_NAME"), rs13.getString("YELPING_SINCE"), rs13.getString("AVERAGE_STARS")};
							//String data[][] = (String[][]) appendValue(myTwoDimensionalStringArray, rowObj);
							resultTable.addRow(rowObj);
							bIDMap.put(i++, rs13.getString("BID"));
						}
						ps.close();
						rs13.close();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}	
				}	
			}
		};
		userPanel.addMouseListener(userMouseListener);
	}

	private void userQuerryButtonActionPerformed(ActionEvent evt) {


		int tracker=0;
		
		if (userresultTable.getRowCount() > 0) {
			for (int i = userresultTable.getRowCount() - 1; i > -1; i--) {
				userresultTable.removeRow(i);
			}
		}
		
		String UserQuery= "select distinct y.user_name, y.yelping_since, y.average_stars from yelp_user y"
				+ " where\r\n";

		if (!memberSinceTextField.getText().isEmpty()) {
			UserQuery += " y.yelping_since > to_date('"  + memberSinceTextField.getText() +"','dd-mm-yyyy') \n";
			tracker=1;
			
		}
		
		if(!countValueTextField.getText().isEmpty()) {
			int reviewCount = Integer.parseInt((String) countValueTextField.getText());
			if(tracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " y.review_count "+ reviewCountLabelComboBox.getSelectedItem() +" "+ reviewCount;
			tracker=1;
		}
		
		if(!frndsCountValueTextField.getText().isEmpty()) {
			if(tracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " y.user_id in (SELECT F.USER_ID FROM  FRIENDS F WHERE F.USER_ID = Y.USER_ID \r\n" + 
					" group by f.user_id having count(F.user_id) " + friendsCountLabelComboBox.getSelectedItem() 
			 + frndsCountValueTextField.getText() +") ";
		}
		
		if(!avgStarTextField.getText().isEmpty()) {
			if(tracker == 1)
				UserQuery += " "+andOrLabelComboBox.getSelectedItem()+" ";
			UserQuery += " Y.average_stars " + avgStarComboBox.getSelectedItem() +
					" " + avgStarTextField.getText();
		}

		System.out.println("_____________________________");
		System.out.println(UserQuery);
		System.out.println("_____________________________");
		
		

			String FinalSubQuery = "";

			System.out.println("Inside 4th Query");

			System.out.println("--------");
			System.out.println(FinalSubQuery);
			System.out.println("--------");

			queryField.setText(UserQuery);

			try {
				ResultSet rs13 = null;
				ps=con.prepareStatement(UserQuery);
				rs13 = ps.executeQuery(UserQuery);
				int i = 0;
				String[] rowObj = new String[3];

				while(rs13.next()){
					rowObj = new String[] {rs13.getString("USER_NAME"), rs13.getString("YELPING_SINCE"), rs13.getString("AVERAGE_STARS")};
					userresultTable.addRow(rowObj);
					userMap.put(i++, rs13.getString("USER_NAME"));
				}
				ps.close();
				rs13.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}	
		}	
	

	private void openUserFrame() { 
		userDisplayTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JFrame userframe = new JFrame("User JFrame");

					JPanel userpanel = new JPanel();
					userpanel.setLayout(new FlowLayout());
					JTable usertarget = (JTable)e.getSource();
					int row = usertarget.getSelectedRow();
					System.out.println("Inside Query");
					System.out.println(userMap.get(row));

					String userquerry = "SELECT R.R_DATE, R.STARS, R.R_TEXT, Y.USER_NAME, Y.USER_ID, R.USEFUL_VOTE, R.FUNNY_VOTE, R.COOL_VOTE\r\n" + 
							"FROM REVIEWS R, YELP_USER Y\r\n" + 
							"WHERE R.USER_ID = Y.USER_ID AND Y.USER_NAME LIKE '" + userMap.get(row)+"'";

					userreviewJTable = new JTable();
					userreviewJTable.setBounds(300,400,1000,1000);

					userreviewTable = new DefaultTableModel();
					userreviewJTable.setModel(userreviewTable);
					userreviewTable.addColumn("Review Date");
					userreviewTable.addColumn("Stars");
					userreviewTable.addColumn("Review");
					userreviewTable.addColumn("User Name");
					userreviewTable.addColumn("Useful Vote");
					userreviewTable.addColumn("Funny Vote");
					userreviewTable.addColumn("Cool Vote");

					JScrollPane userreviewResultPane = new JScrollPane(userreviewJTable);
				
					System.out.println("--------");
					System.out.println(userquerry);
					System.out.println("--------");

					userpanel.add(userreviewResultPane);
					//  panel.add(button);
					String[] rowObj = new String[7];

					try {
						ResultSet rs13 = null;
						ps=con.prepareStatement(userquerry);
						rs13 = ps.executeQuery(userquerry);

						while(rs13.next())
						{

							rowObj = new String[] {rs13.getString("R_DATE"), rs13.getString("STARS"), rs13.getString("R_TEXT"), rs13.getString("USER_NAME"), rs13.getString("USEFUL_VOTE"), rs13.getString("FUNNY_VOTE"), rs13.getString("COOL_VOTE")};
							//String data[][] = (String[][]) appendValue(myTwoDimensionalStringArray, rowObj);
							userreviewTable.addRow(rowObj);

						}
						ps.close();
						rs13.close();
					} catch(Exception ex) {
						System.out.println(ex);
					}
					userframe.add(userpanel);
					userframe.setSize(1800, 1200);
					userframe.setLocationRelativeTo(null);
					//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					userframe.setVisible(true);
				}
			}
		});

	}

	private void updateUnionIntersect() {
		ANDOR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				categoryList.clearSelection();
				addSubCategoryList.clear();
				addAttributeList.clear();
				addResultList.clear();
				JComboBox<String> combo = (JComboBox<String>) event.getSource();
				String selectedBook = (String) combo.getSelectedItem();

				if (selectedBook.equals("AND")) {
					connectionString = "INTERSECT";
				} else if (selectedBook.equals("OR")) {
					connectionString = "UNION";
				}
			}
		});

	}
}
