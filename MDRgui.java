//Tom Kennedy Peter Tsongalis
import com.pi4j.io.gpio.exception.GpioPinExistsException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.Exception;
import java.util.List;
import javax.swing.JScrollPane;
import java.util.ArrayList;

public class MDRgui extends JFrame {

    //strings
    public List<String> outputString;
    public List<String> names;
    //outputdisplay declarations
    public JTabbedPane tabbedPane;
    public List<JComponent> tabList;
    public JButton exit;
    public Font myFont;
    public JPanel myPanel;
    public JTable table;
    public gpioPins gpio;

    //display UI
    public void MDRgui() throws IOException, InterruptedException {

        gpio = new gpioPins();
        
        
        //opening screen
        OpenScreen o = new OpenScreen(gpio);
        //displays welcome screen until user clicks continue
        o.Welcome();
        while (o.nextClicked == false) {}
        //fileChooser running returns into list names
        List<String> files = new ArrayList<String>();
        FileChoose f = new FileChoose(gpio);
        GetFileFromUSB g = new GetFileFromUSB();
        g.mount();
        files = g.getFile();
        f.ChooseFile(files);

        //o.closeFrame();
        //keeps filechooser frame open until user is ready to continue
        while (f.getClosed() == false) {
            try {
                Thread.sleep(500);
                // names = f.returnFiles();
            } catch (Exception F) {
                System.out.println("error");
            }
        }
        //returns all wanted files
        names = f.returnFiles(files);

        /*for (int x = 0; x < names.size(); x++) {
         System.out.println(names.get(x) + "returned");
         }
         */

        //makes clmInit object
        clmInit cInit = new clmInit(gpio);

        /*fileChooser then goes to clmInit
         *clmInit returns the outputString that comes back 
         *here to display the results
         */
        outputString = cInit.takeList(names);

        //output display
        displayOutput(outputString);
    }

    public void displayOutput(List<String> outputString) {
        //creates tabs to display multiple outputs
        tabList = new ArrayList<JComponent>();
        tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("");


        for (int i = 0; i < outputString.size(); i++) {
            //adds tab to panel
            tabbedPane.addTab(names.get(i), icon, makeTextPanel(outputString.get(i)), "Does Nothing");
        }
        showScreen();
    }

    private JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);


        //System.out.println("making parse object");
        //creates object to parse output
        OutputParse parser = new OutputParse();
        List<String[]> parsedData = parser.Parse(text);
        System.out.println(parsedData.get(0)[0]);
        //displays all data in table
        table = new JTable(parser.makeTable(parsedData), parser.getColumnNames(parsedData));
        table.setFont(new Font("Arial", Font.PLAIN, 20));
        table.setRowHeight(40);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int column = 0; column < table.getColumnCount(); column++) {

            table.getColumnModel().getColumn(column).setPreferredWidth(50 + (50 * column));


        }

        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //end table instead

        panel.setLayout(new GridLayout(1, 1));
        panel.add(scrollPane); //if it doesnt work substitute "filler" here
        return panel;
    }

    private void showScreen() throws GpioPinExistsException{
        //creates font 
        myFont = new Font("Arial", Font.BOLD, 16);
        //sets frame to size of screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        int x = ((int) tk.getScreenSize().getWidth());
        int y = ((int) tk.getScreenSize().getHeight());
        //Create and set up the window.
        JFrame frame = new JFrame("Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(x, y);
        //Add content to the window.
        exit = new JButton("Shutdown");
        exit.setPreferredSize(new Dimension(300, 75));
        exit.setFont(myFont);
        //creates action for exit button
        PackageListener p1 = new PackageListener();;
        exit.addActionListener(p1);
        //creates panel and adds button
        myPanel = new JPanel();
        myPanel.add(exit);
        //adds all panels to frame
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(myPanel, BorderLayout.SOUTH);
        //Display the window.

        gpio.createEnterListener(exit);
        gpio.createTabListenerMDRGUI(table);
        gpio.createChangeToShutdownListenerMDRGUI(exit);
        

        //set frame visible
        frame.setVisible(true);
    }

    //from: http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MDRgui.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
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
                }
            } catch (Exception f) {

                System.out.println("error");
            }
        }
    }
}
