
import java.awt.*;
import javax.swing.*;

public class progressScreen {
    
    //create display objects
    public JFrame frame;
    public JLabel label;
    public JProgressBar progressBar;
    public Font myFont;
    public int max = 100;
    public JPanel panel;
    public JPanel bottomPanel;
    public JLabel bottomLabel;
    
    
    progressScreen(){
        
        //font to be displayed
        myFont = new Font("Arial", Font.BOLD, 20);
        
        //init display objects
        frame = new JFrame();
        panel = new JPanel();
        label = new JLabel();
        
        bottomLabel = new JLabel();
        bottomPanel = new JPanel();

        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        //set frame to size of screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        int x = ((int) tk.getScreenSize().getWidth());
        int y = ((int) tk.getScreenSize().getHeight());
        
        frame.setSize(x, y);
        frame.setTitle("Analyzing");
        
        label.setText("Running MDR Analysis");
        label.setFont(myFont); 
        
        
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(x, 200));
        panel.add(label);
            
 
        bottomPanel.setPreferredSize(new Dimension(x, 100));
        //arrange panels
        frame.add(panel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

    }
    
    //shows frame
    public void makeVisible(){
        
        frame.setVisible(true);
        
    }
    //hides frame
    public void makeInvisible(){
        
        frame.setVisible(false);
    }
    
    
    public void setBottomLabelText(String text) {
        
        bottomLabel.setText("Running: " + text);
        bottomLabel.setFont(myFont);
        bottomPanel.add(bottomLabel);
        
    }
    
    public void initializeProgressBar() {
        
        progressBar = new JProgressBar(0, max);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setMaximum(max);
        
        
        frame.add(progressBar, BorderLayout.CENTER);
        
    }
 
}
