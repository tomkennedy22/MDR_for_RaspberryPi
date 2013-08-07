import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;

//Tom Kennedy Peter Tsongalis


public class OpenScreen {
    //creates all objects to display

    private JButton exit;
    private Font myFont;
    private JPanel myPanel;
    private JLabel usb;
    public JFrame frame;
    private JButton next;
    private JPanel Panel2;
    private JLabel Welcome;
    public JFrame WelcomeFrame;
    private Font WelcomeFont;
    public boolean nextClicked;
    public JLabel MDRlogo;
    public ImageIcon icon;
    private gpioPins gpio;

    public OpenScreen(gpioPins gpio) {

        this.gpio = gpio;

    }

    public void Welcome() {
        //sets frame to size of screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        int x = ((int) tk.getScreenSize().getWidth());
        int y = ((int) tk.getScreenSize().getHeight());
        //show that next has not been clicked
        nextClicked = false;
        //creates icon with MDR logo
        icon = new ImageIcon("/home/pi/mdrlogo.png");

        MDRlogo = new JLabel(icon);

        //create and implement proper font
        WelcomeFont = new Font("Arial", Font.BOLD, 24);
        Welcome = new JLabel("Welcome to MDR!");
        Welcome.setFont(WelcomeFont);
        //Create and set up the window.
        WelcomeFrame = new JFrame("MDR Raspberry Pi");
        WelcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WelcomeFrame.setSize(x, y);
        //Add content to the window.
        next = new JButton("Continue");

        next.setPreferredSize(new Dimension(300, 100));
        next.setFont(WelcomeFont);
        //create and add action to the next button
        PackageListener p1 = new PackageListener();;
        next.addActionListener(p1);

        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());
        newPanel.add(Welcome);

        //adds and sets up panel


        Panel2 = new JPanel();
        Panel2.setLayout(new BorderLayout());
        Panel2.add(newPanel, BorderLayout.NORTH);
        Panel2.add(MDRlogo, BorderLayout.CENTER);
        Panel2.add(next, BorderLayout.SOUTH);



        //frame.add(tabbedPane, BorderLayout.CENTER);
        WelcomeFrame.add(Panel2);
        //Display the window.

        WelcomeFrame.setVisible(true);

        //give continue button focus
        next.requestFocus();

        //create gpio control object
        gpio.createEnterListener(next);
    }

    public void open() {

        usb = new JLabel("Please insert your USB Drive");
        myFont = new Font("Arial", Font.BOLD, 24);
        Font usbFont = new Font("Arial", Font.BOLD, 18);
        usb.setFont(usbFont);
        //usb.setPreferredSize(new Dimension(200, 100));
        Toolkit tk = Toolkit.getDefaultToolkit();
        int x = ((int) tk.getScreenSize().getWidth());
        int y = ((int) tk.getScreenSize().getHeight());
        //Create and set up the window.
        frame = new JFrame("MDR Raspberry Pi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(x, y);
        //Add content to the window.
        exit = new JButton("Exit Program");
        exit.setPreferredSize(new Dimension(300, 100));
        exit.setFont(WelcomeFont);
        //adds action to exit button
        PackageListener p1 = new PackageListener();;
        exit.addActionListener(p1);

        JPanel newPanel = new JPanel();
        newPanel.setLayout(new FlowLayout());
        newPanel.add(usb);

        //creates and sets up panel
        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
        myPanel.add(newPanel, BorderLayout.NORTH);
        myPanel.add(MDRlogo, BorderLayout.CENTER);
        myPanel.add(exit, BorderLayout.SOUTH);


        //add components to frame
        frame.add(myPanel);
        //Display the window.

        frame.setVisible(true);


    }

    private class PackageListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            try {
                //finds which button was clicked
                if (e.getSource() == exit) {
                    //close when user asks
                    String shtdwn = "sudo shutdown -h now";
                    Runtime runtime = Runtime.getRuntime();

                    InputStream input = runtime.exec(shtdwn).getInputStream();
                } else if (e.getSource() == next) {
                    gpio.deleteListeners(gpio.enter);
                    
                    WelcomeFrame.setVisible(false);
                    nextClicked = true;
                }
            } catch (Exception f) {

                System.out.println("error");
            }


        }
    }

    public boolean usbCheck() throws IOException {
        boolean filePresent = false;
        //mounts usb drive
        String mount_drive = "sudo mount /dev/sda1 /mnt/MDR_Data_Drive";
        Process mount = Runtime.getRuntime().exec(mount_drive);
        //checks for usb drive
        filePresent = new File("/dev/sda1").exists();

        return filePresent;
    }

    public void closeFrame() {
        frame.dispose();
    }
}
