//TODO: Add 'Choose Directory' button and set up file chooser.
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserInterface implements ActionListener {
	private JFrame j;
	private JButton downloadButton;
	private JButton help;
	private JTextField paperYear;
	private JTextField personYear;
	private JComboBox<String> courseBox;
	private JCheckBox allBox;
	private JLabel paperLabel;
	private JLabel yearLabel;
	private JLabel courseLabel;
	private String year;
	private String standing;
	private int courseNum;
	private Downloader dl;
	private final int DEFAULT_COURSE_CS = 19;
	private final int CSL = 57;
	private final int PHARMACY = 16;
	private final int MSISS = 18;
	private final int LAW = 51;
	private final int NAT_SCIENCE = 15;
	private final int PCAM = 440;
	private final String[] COURSES = {"Integrated Computer Science", "Pharmacy", "Natural Sciences (General Science)", "Physics and Chemistry of Advanced Materials (Nanoscience)", "Computer Science, Linguistics and a Language", "MSISS", "Law"};
	private JLabel status;
	private String directory = "";

	public UserInterface() {
		j = new JFrame("TCD Exam Paper Downloader");
		downloadButton = new JButton("Get Papers");
		help = new JButton("Help");
		allBox = new JCheckBox("GET ALL YEARS ", false);
		allBox.addActionListener(this);
		downloadButton.addActionListener(this);
		help.addActionListener(this);
		courseBox = new JComboBox<String>(COURSES);
		courseBox.setSelectedIndex(0);
		courseBox.addActionListener(this);
		paperYear = new JTextField(10);
		personYear = new JTextField(10);
		paperLabel = new JLabel("  Year (1998 - 2012):");
		yearLabel = new JLabel("  Standing (JF = 1, SF = 2, JS = 3, SS = 4): ");
		courseLabel = new JLabel("  Select Course: ");
		status = new JLabel(" Enter request...");

		new JLabel("");
		courseNum = DEFAULT_COURSE_CS;
		GridLayout grid = new GridLayout(5, 1);
		grid.setHgap(10);
		grid.setVgap(10);
		j.setLocation(360, 200);
		j.setSize(600, 180);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setLayout(grid);
		j.add(courseLabel);
		j.add(courseBox);
		j.add(paperLabel);
		j.add(paperYear);
		j.add(yearLabel);
		j.add(personYear);
		j.add(allBox);
		j.add(status);
		j.add(help);
		j.add(downloadButton);
		j.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if(e.getSource() == downloadButton) {
				
				status.setForeground(Color.ORANGE);
				status.setText(" Processing Request...");
				standing = personYear.getText();
				String courseSetting = courseBox.getSelectedItem().toString();

				if(courseSetting.equalsIgnoreCase("pharmacy")) {
					courseNum = PHARMACY;
				} else if(courseSetting.equalsIgnoreCase("computer science linguistics and a language")) {
					courseNum = CSL;
				} else if(courseSetting.equalsIgnoreCase("msiss")) {
					courseNum = MSISS;
				} else if(courseSetting.equalsIgnoreCase("law")) {
					courseNum = LAW;
				} else if(courseSetting.equalsIgnoreCase("natural sciences (general science)")) {
					courseNum = NAT_SCIENCE;
				} else if(courseSetting.equalsIgnoreCase("physics and chemistry of advanced materials (nanoscience)")) {
					courseNum = PCAM;
				} else {
					courseNum = DEFAULT_COURSE_CS;
				}

				if(!allBox.isSelected()) {
					year = paperYear.getText();
					if((Integer.parseInt(year) > 1997 && Integer.parseInt(year) < 2013) && (Integer.parseInt(standing) > 0 && Integer.parseInt(standing) < 5) ) {
						dl = new Downloader(year, standing, courseNum, directory);
						System.out.println(year + standing + courseNum);
						boolean success = dl.getPapers();
						if(success) {
							status.setForeground(Color.green);
							status.setText(" Download Complete.");
						} else {
							status.setForeground(Color.RED);
							status.setText(" Error in request.");
							JOptionPane.showMessageDialog(j, "Download unsuccessful. You probably made a mistake entering year or standing.");
						}
					} else {
						status.setForeground(Color.RED);
						status.setText(" Error in request.");
						JOptionPane.showMessageDialog(j, "You didn't enter a valid year/standing.");
					}
				} else if(allBox.isSelected()) {
					if(Integer.parseInt(standing) > 0 && Integer.parseInt(standing) < 5 ) {
						for(int y = 1998; y < 2013; y++) {
							dl = new Downloader(("" + y), standing, courseNum, directory);
							boolean success = dl.getPapers();
							if(success) {
								status.setForeground(Color.green);
								status.setText("Download Complete");
							} else {
								status.setForeground(Color.RED);
								status.setText("Error in Request.");
								JOptionPane.showMessageDialog(j, "Download unsuccessful. You probably made a mistake entering year or standing.");
							}
						}
					}
				}
			} else if(e.getSource() == allBox) {
				if(allBox.isSelected()) {
					paperYear.setEnabled(false);
					paperLabel.setEnabled(false);
					paperLabel.setText("  Downloading all years.");
				} else if(!allBox.isSelected()) {
					paperYear.setEnabled(true);
					paperLabel.setEnabled(true);
					paperLabel.setText("  Year (1998 - 2012):");
				}
			} else if(e.getSource() == help) {
				JOptionPane.showMessageDialog(j, "HELP:\n1. *1st/2nd YEAR GENERAL SCIENCE STUDENTS* --> Choose \'Natural Sciences.\'"
						+ "\n2. *GENERAL SCIENCE STUDENTS AGAIN -->* It downloads papers for all the modules.\n     Sort them yourself."
						+ "\n3. *EVERYBODY* --> SAVE LOCATION - \"ExamPaperDownloader0_03/Standing/Year/paper.pdf\"\n     "
						+ "i.e. - ExamPaperDownloader0_03/JF/2012/XCS10011.pdf"
						+ "\n     Please don't tell me they didn't save before looking for them, they're probably there..."
						+ "\n4. If there is a paper missing, try download it manually. If you get a \'Page Not Found\'\n    "
						+ "or something similar, the link is dead. If not... sorry, my bad.");
			} 
		} catch(NumberFormatException nfe) {
			status.setForeground(Color.RED);
			status.setText(" Error in request.");
			JOptionPane.showMessageDialog(j, "Error. Did you leave a field blank?");
		}
	}
}
