package gui;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

public class LineDisplayTest {
    
    private static final int DISPLAY_SIZE = 10;
    private JPanel containerPanel;
    private LineDisplay lineDisplay;
    
    @Before
    public void initJPanel() {
    	containerPanel = new JPanel();
    	lineDisplay = new LineDisplay(DISPLAY_SIZE);
    	containerPanel.add(lineDisplay);
    }
    
	@Test
	public void test() {
		lineDisplay.setTextLine("1234567890\n");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testOverflow() {
		lineDisplay.setTextLine("12345678901\n");
	}

}
