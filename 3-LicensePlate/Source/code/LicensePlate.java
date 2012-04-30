package code;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * An interface for determining the best pattern to use for creating license plates.
 * In the pattern 'A' represents any letter (A-Z), '#' represents any number(0-9),
 * if alphanumeric characters are enabled, then '*' represents any letter or number (A-Z, 0-9)
 * Enabling alphanumeric characters will always result in patterns of greater of equal efficiency and is recommended.
 * 
 * The resulting patterns can be permuted, provided all plates use the chosen permutation.
 * For instance, if the given pattern is 'AA##' you may use '#A#A' instead with the same number of excess plates
 * 3R8G and 2X5V would be valid, but AE37 would not be.
 * 
 * Note: I decided not to make patterns of the form "n letters, m numbers" because allowing permutations 
 * greatly increases the complexity of printing the patterns, especially when including alphanumeric characters.
 * @author Zach Kemphues
 */
public class LicensePlate {

	private static final int LETTER = 26;
	private static final int NUMBER = 10;
	private static final int ALPHANUMERIC = 36;
	
	private JFrame _frame = new JFrame("License Plate");
	private JTextArea _popArea = new JTextArea();
	private JTextField _popField = new JTextField();
	private JTextArea _patternArea = new JTextArea();
	private JTextArea _patternField = new JTextArea();
	private JTextArea _totalArea = new JTextArea();
	private JTextArea _totalField = new JTextArea();
	private JTextArea _excessArea = new JTextArea();
	private JTextArea _excessField = new JTextArea();
	private JCheckBox _enableAlphanumeric = new JCheckBox();
	private boolean _allowAlphanumeric = false;

	/**
	 * Opens the LicensePlate user interface
	 * @param args - none
	 */
	public static void main(String[] args) {
		LicensePlate l = new LicensePlate();
	}
	/**
	 * Creates an interface for inputting the target population and generating
	 * the best pattern from it, displaying the total number of plates that would
	 * be printed, and the number that would be wasted.
	 */
	public LicensePlate() {
		
		_frame.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		
		_popArea.setText("Population:");
		_popArea.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				0, 0, 1, 1,
				0, 0, 0.0, 0.2);
		_frame.add(_popArea, cons);
		
		_popField.addKeyListener(new KeyListener() {//Simple way of preventing non-digit characters from being entered into the population field
													//and recalculating the pattern every time the input field changes
			public void keyPressed(KeyEvent key) {}//do nothing
			public void keyReleased(KeyEvent key) {
				char c = key.getKeyChar();
				if(c >= '0' && c <= '9' || c == 127 || c == 8)//A number or delete/backspace
					refreshValues();
				else if(c > 31 && c < 127)//Any character that would appear in the text field
					_popField.setText(_popField.getText().substring(0,_popField.getText().length()-1));//remove the invalid character
					refreshValues();
				}
			public void keyTyped(KeyEvent key) {}//do nothing
		});
		cons = setConstraints(GridBagConstraints.BOTH,
				1, 0, 1, 1,
				0, 0, 1.0, 0.2);
		_frame.add(_popField, cons);
		
		_patternArea.setText("Pattern:");
		_patternArea.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				0, 1, 1, 1,
				0, 0, 0.0, 0.2);
		_frame.add(_patternArea, cons);
		
		_patternField.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				1, 1, 1, 1,
				0, 0, 1.0, 0.2);
		_frame.add(_patternField, cons);
		
		_totalArea.setText("Total Plates:");
		_totalArea.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				0, 2, 1, 1,
				0, 0, 0.0, 0.2);
		_frame.add(_totalArea, cons);
		
		_totalField.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				1, 2, 1, 1,
				0, 0, 1.0, 0.2);
		_frame.add(_totalField, cons);
		
		_excessArea.setText("Excess Plates:  ");
		_excessArea.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				0, 3, 1, 1,
				0, 0, 0.0, 0.2);
		_frame.add(_excessArea, cons);
		
		_excessField.setFocusable(false);
		cons = setConstraints(GridBagConstraints.BOTH,
				1, 3, 1, 1,
				0, 0, 1.0, 0.2);
		_frame.add(_excessField, cons);
		
		_enableAlphanumeric.setText("Enable Alphanumeric Characters ");
		_enableAlphanumeric.setToolTipText("Allows patterns to include characters that may be either a number or a letter when checked.");
		_enableAlphanumeric.setFocusable(false);
		_enableAlphanumeric.addActionListener(new ActionListener() {//recalculates the pattern when the checkbox is toggled
			public void actionPerformed(ActionEvent arg0) {
				_allowAlphanumeric = !_allowAlphanumeric;
				refreshValues();
				}
			});
		_enableAlphanumeric.doClick();//enabled by default
		cons = setConstraints(GridBagConstraints.BOTH,
				0, 4, 2, 1,
				0, 0, 1.0, 0.2);
		_frame.add(_enableAlphanumeric, cons);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setResizable(false);
		_frame.setSize(250,120);
		_frame.setVisible(true);
		
		refreshValues();
	}
	
	//Updates all the fields with the current population target
	private void refreshValues() {
		long pop;
		try {
			pop = Long.parseLong(_popField.getText());
		} catch(NumberFormatException e) {
			pop = 0;
			_popField.setText("Enter A Number");
			_popField.selectAll();
		}
		Pattern p;
		p = createPattern(pop);
		_patternField.setText(p.getPattern());
		_totalField.setText(format(p.getTotal()));
		_excessField.setText(format(p.getTotal() - pop));
	}
	
	//convenient method for setting all the necessary properties of GridBagConstraints in one line
	private static GridBagConstraints setConstraints(int fillType, int col, int row, int width, int height, int xpad, int ypad, double weightx, double weighty) {
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = fillType;
		cons.gridx = col;
		cons.gridy = row;
		cons.gridwidth = width;
		cons.gridheight = height;
		cons.ipadx = xpad;
		cons.ipady = ypad;
		cons.weightx = weightx;
		cons.weighty = weighty;
		return cons;
	}
	
	//Returns the best pattern given a target population
	private Pattern createPattern(long target) {
		if(target <= NUMBER)//the pattern will be the minimum
			return addToPattern(new Pattern(),target, 10);
		return addToPattern(new Pattern(),target,1);
	}
	//Recursive method for finding the best pattern given a target population
	private Pattern addToPattern(Pattern p, long target, int type) {
		String s = "";
		switch(type){
		case NUMBER:
			s = "#";
			break;
		case LETTER:
			s = "A";
			break;
		case ALPHANUMERIC:
			s = "*";
			break;
		default://the very first call
			//do nothing
			break;
		}
		
		p = new Pattern(p.getTotal()*type,p.getPattern()+s);
		if(p.getTotal() >= target) 
			return p;
		
		Pattern l = addToPattern(p, target, LETTER);
		Pattern n = addToPattern(p, target, NUMBER);
		Pattern min = lesserOf(l,n);
		if(_allowAlphanumeric)
			min = lesserOf(min, addToPattern(p, target, ALPHANUMERIC));
		return min;		
	}

	/**
	 * Returns the pattern that will create the smallest number of plates
	 * @param a - the first pattern being checked
	 * @param b - the second pattern being checked
	 * @return the smaller of a or b
	 */
	public static Pattern lesserOf(Pattern a, Pattern b) {
		if(a.getTotal() > b.getTotal())
			return b;
		return a;
	}
	/**
	 * Used to beautify numbers by adding commas.  ie: 1000 returns 1,000
	 * @param n - the number being formatted
	 * @return the number with proper commas added as a string
	 */
	public static String format(long n){
		if(n == 0)
			return "0";
		String s = "";
		int i = 0;
		while(n!=0){
			if(i == 3){
				s = "," + s;
				i = 0;
			}
			s = n%10 + s;
			i++;
			n = n/10;
		}
		return s;
	}
}
