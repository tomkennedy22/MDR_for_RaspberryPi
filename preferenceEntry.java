
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Exception;
import java.util.List;
import javax.swing.SwingConstants;

import com.pi4j.io.gpio.exception.GpioPinExistsException;
import java.util.ArrayList;

/**
 *@author Tom Kennedy
 * @author PeterTsongalis
 */
public class preferenceEntry {

    public JFrame frame;
    public JFrame display_Frame;
    //initialize all UI fields
    private JLabel minLabel;
    private JLabel maxLabel;
    private JLabel cvLabel;
    public JTextArea textArea;
    private JTextField minTF;
    private JTextField maxTF;
    private JTextField cvTF;
    private JButton exit;
    private JButton go;
    public JPanel fieldPanel;
    private JPanel buttonPanel;
    private JPanel namePanel;
    private JLabel nameLabel;
    private int min;
    private int max;
    private int cv;
    private String name;
    private Font myFont;
    public boolean isClosed;
   
    public int[]Fields;
    public List<JButton>buttons;
    public List<JTextField>textFields;
    private gpioPins gpio;
    
    public preferenceEntry(String name, gpioPins gpio) {
        this.name = name;
        isClosed = false;
        this.gpio = gpio;
                

    }

    public void runPrefEnt() throws GpioPinExistsException {
        //indicate open frame
        isClosed = false;
        //creates font
        myFont = new Font("Arial", Font.BOLD, 18);

        //sets frame to size of screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        int x = ((int) tk.getScreenSize().getWidth());
        int y = ((int) tk.getScreenSize().getHeight());

        frame = new JFrame("Input Analysis Preferences");
        frame.setSize(x, y);
        frame.setResizable(false);
        frame.toFront();

        //initialize panels
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(3, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());


        //set text for Label
        minLabel = new JLabel("Minimum");
        minLabel.setFont(myFont);
        fieldPanel.add(minLabel);

        //create textfield
        minTF = new JTextField(5);
        minTF.setFont(myFont);
        fieldPanel.add(minTF);

        //set text for Label
        maxLabel = new JLabel("Maximum");
        maxLabel.setFont(myFont);
        fieldPanel.add(maxLabel);

        //create Text field
        maxTF = new JTextField(5);
        maxTF.setFont(myFont);
        fieldPanel.add(maxTF);

        //set text for label
        cvLabel = new JLabel("Cross Validation Count");
        cvLabel.setFont(myFont);
        fieldPanel.add(cvLabel);

        //create textfield
        cvTF = new JTextField(5);
        cvTF.setFont(myFont);
        fieldPanel.add(cvTF);


        //create button
        exit = new JButton("Shutdown");
        exit.setPreferredSize(new Dimension(150, 50));
        exit.setFont(myFont);
        buttonPanel.add(exit);


        //create button
        go = new JButton("GO!");
        go.setPreferredSize(new Dimension(150, 50));
        go.setFont(myFont);
        buttonPanel.add(go);

        //create nameLabel
        nameLabel = new JLabel(name);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(myFont);
        nameLabel.setPreferredSize(new Dimension(x, 150));
        namePanel.add(nameLabel);
        namePanel.setPreferredSize(new Dimension(x, 150));



        //add components to frame
        frame.add(namePanel, BorderLayout.NORTH);
        frame.add(fieldPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);


        //create actions for exit and go
        PackageListener p1 = new PackageListener();
        exit.addActionListener(p1);
        go.addActionListener(p1);

        //allow for gpio to control frame
        Fields = new int[5];
        buttons = new ArrayList<JButton>();
        textFields = new ArrayList<JTextField>();
        textFields.add(minTF);
        textFields.add(maxTF);
        textFields.add(cvTF);
        buttons.add(go);
        buttons.add(exit);
        
        
        
        //set visible
        frame.setVisible(true);

        //create gpio control objects
        gpio.createEncoderListener(Fields, textFields);
        gpio.createEnterListenerPrefEnt(buttons);
        gpio.createNegativeSwitchListener(textFields);
        gpio.createTabListenerForPref(textFields, buttons);
        
        


    }

    private class PackageListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                //finds which button was clicked
                if (e.getSource() == exit) {
                    //close when user asks
                    String cmd = "sudo shutdown -h now";
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec(cmd);
                } //run program when prompted
                else if (e.getSource() == go) {

                    frame.setVisible(false);

                    gpio.deleteListeners(gpio.enter);
                    gpio.deleteListeners(gpio.tab);
                    gpio.deleteListeners(gpio.encoder);
                    gpio.deleteListeners(gpio.negativeSwitch);
                    //find user inputted values
                    min = Integer.parseInt(minTF.getText());
                    max = Integer.parseInt(maxTF.getText());
                    cv = Integer.parseInt(cvTF.getText());

                    System.out.println(min + " " + max + " " + cv);
                    System.out.println("Collected preferences for: " + name);
                    isClosed = true;
                }
            } catch (Exception f) {
                System.out.println("Error");
            }


        }
    }

    public int getMin() {

        if (min > 0) {
            return min;
        } else {
            return 1;
        }
    }
    //return appropriate value
    public int getMax() {
        if (max<min){
            return min+1;
        }
        
        if (max > 0) {
            return max;
        } else {
            return 3;
        }
        
    }

    public int getCV() {

        if (cv > 0) {
            return cv;
        } else {
            return 10;
        }
    }

    public boolean getClosed() {

        return isClosed;
    }
}
