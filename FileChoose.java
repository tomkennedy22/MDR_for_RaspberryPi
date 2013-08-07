import com.pi4j.io.gpio.exception.GpioPinExistsException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.HashMap;
import java.awt.Font;

//Tom Kennedy Peter Tsongalis

public class FileChoose {

    //initialize all objects for proper functions
    public JFrame FileChooser;
    public List<JButton> buttons;
    public List<Command_Line_MDR> clm;
    public List<String> chosenFiles;
    public List<JLabel> filesLabel;
    public List<JPanel> panels;
    public boolean closed;
    public JPanel holderPanel;
    public Font myFont;
    public boolean[] filesWanted;
    private gpioPins gpio;
    
    
    //constructor
    public FileChoose(gpioPins gpio) {
        
        this.gpio = gpio;
        
    }
    
    public void ChooseFile(List<String> files) throws GpioPinExistsException{
        //create font to display text
        myFont = new Font("Arial", Font.BOLD, 16);

        //tell functions that this frame is still open
        setClosed(false);
        //create tuple for buttons to be paired with integers
        final HashMap<JButton, Integer> map = new HashMap<JButton, Integer>();
        //keep track of initial list size
        int size = files.size();
        //create frame for user to select desired files
        FileChooser = new JFrame("Please Choose Your Files");
        FileChooser.setLayout(new BorderLayout());
        //set frame to size of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        FileChooser.setBounds(0, 0, screenSize.width, screenSize.height);
        //declare all needed object containers
        //keeps files that user wants
        chosenFiles = new ArrayList<String>();
        //keeps all buttons that correspond with files
        buttons = new ArrayList<JButton>();
        //displays all files found
        filesLabel = new ArrayList<JLabel>();
        //displays buttons & labels
        panels = new ArrayList<JPanel>();
        //copies list of files found on usb
        final List<String> filesFound = files;
        //keeps track of buttons clicked
        filesWanted = new boolean[size]; 
        
        //creates button to allow user to continue
        JButton continueButton = new JButton("Continue");
        continueButton.setFont(myFont);

        //panel that holds all objects
        holderPanel = new JPanel();
        holderPanel.setLayout(new GridLayout(0, 1));

        //runs from 0 to number of files found on usb
        for (int x = 0; x < size; x++) {
            //adds holder panel to main panel
            panels.add(new JPanel());
            panels.get(x).setLayout(new FlowLayout());

            //adds new label with file name to label folder
            filesLabel.add(new JLabel(files.get(x)));
            //sets universal font to all labels
            filesLabel.get(x).setFont(myFont);
            //adds button to correspond with every label
            buttons.add(new JButton("Click"));
            buttons.get(x).setFont(myFont);
            //creates tuple map to hold button and corresponding integer
            map.put(buttons.get(x), new Integer(x));
            //adds labels and buttons to each panel
            panels.get(x).add(filesLabel.get(x));
            panels.get(x).add(buttons.get(x));
            //adds action to every button
            buttons.get(x).addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae2) {

                    //finds corresponding integer to each buttons
                    Integer index = map.get(ae2.getSource());
                    System.out.println(filesFound.get(index) + "clicked");
                    //sets button clicked to either true or false depedning on click number
                    filesWanted[index] = !filesWanted[index];
                    //sets label to blue if buttons clicked once
                    if(filesWanted[index] == true){
                        //chosenFiles.add(filesFound.get(index));
                        filesLabel.get(index).setForeground(Color.BLUE);
                    }
                    else{
                        filesLabel.get(index).setForeground(Color.BLACK);
                    }

                }
            });
            //adds panels to holder panel
            holderPanel.add(panels.get(x));
            //adds all panels to jframe
            FileChooser.add(holderPanel, BorderLayout.CENTER);
        }
        //adds continue button to jframe
        FileChooser.add(continueButton, BorderLayout.SOUTH);
        //adds action to continue
        continueButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("closing");
                //notifies other files that frame is closed
                setClosed(true);
                //closes frame
                gpio.deleteListeners(gpio.tab);
                gpio.deleteListeners(gpio.enter);
                
                FileChooser.setVisible(false);
            }
        });

        
        buttons.add(continueButton);
        //create gpio control objects
        gpio.createTabListenerFileChooser(buttons);
        gpio.createEnterListenerFileChooser(buttons);
        
        
        FileChooser.setVisible(true);

    }
    //returns all selected files to previous screen
    public List<String> returnFiles(List<String> files) {
        System.out.println("returning");
        for (int x = 0; x < filesWanted.length; x++) {
            //finds files wanted by user and adds them to list that returns to previous scrren
            if(filesWanted[x] == true){
                chosenFiles.add(files.get(x));
            }   
        }
        return chosenFiles;
    }

    public boolean getClosed() {
        return closed;
    }

    //alert that frame has closed
    public void setClosed(boolean close) {
        closed = close;
    }
}
