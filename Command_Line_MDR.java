//package rpirun;
import java.awt.Event;
import java.io.*;

public class Command_Line_MDR {
    //initialize all necessary variables
    private int min = 1;
    private int max = 3;
    private int cv = 10;
    private String name = "Data File";
    int lineCounter = 0;
    int expectedLines;
    int percent;
    int expectedPercent;
    //create object to later show progress of MDR execution
    public progressScreen progress = new progressScreen();   
    
    Command_Line_MDR(int min, int max, int cv, String name) {
        //reads user inputted variables into construcor
        this.min = min;
        this.max = max;
        this.cv = cv;
        this.name = name;
    }
    public String run() throws IOException {
        //allows for progress bar to be on screen
        progress.initializeProgressBar();
        progress.setBottomLabelText(name);
        progress.makeVisible();
        
        //estimates how many lines will be outputted by terminal, allows for progress screen
        expectedLines = 4*(max - min) + 14;
        
        System.out.println(expectedLines);
        //create string to run desired command
        String MDRjar = "/home/pi/mdr.jar";
        //create string to run desired command (runs MDR with user inputted variables)
        String cmd = "java -jar " + MDRjar + " -min=" + min + " -max=" + max + " -cv=" + cv + " -table_data=true -minimal_output=true " + name;

        System.out.println(cmd);
        //String to be used to capture terminal output
        String string_var = "";

        try {
            Runtime runtime = Runtime.getRuntime();
            //run MDR
            InputStream input = runtime.exec(cmd).getInputStream();
            //find machine output results of MDR
            BufferedInputStream buffer = new BufferedInputStream(input);
            BufferedReader commandResult = new BufferedReader(new InputStreamReader(buffer));
            String line = "";
            try {
                
                int nullCount = 0;
                //read machine output of MDR and add to String, watch for 1 null line
                while (((line = commandResult.readLine()) != null) || (nullCount < 1)) {
                    //appends machine output to the rest of the output
                    string_var += (line + "\n");
                    setLineCounter(lineCounter + 1);
                    
                    
                    System.out.println(lineCounter + line);
                    
                    expectedPercent = (getLineCounter()*100) / expectedLines;
                    System.out.println(percent);
                    //displays single increasing progres bar
                    while(percent<expectedPercent){
                        Thread.sleep(100);
                        percent++;
                        progress.progressBar.setValue(percent);
                    }
                    //increments counter to detect null output
                    if (line == null) {
                        nullCount++;
                    }
                }              
                System.out.print("# of Lines= " + lineCounter);
                Thread.sleep(4000);
                //hides progress bar
                progress.makeInvisible();
                //send output to previous called function
                return string_var;
                
            } catch (Exception e) {
                System.out.println("Error");
                return "ERROR";
            }
      } catch (Exception e) {
            System.out.println("Error");
            return "ERROR";
        }
    }
    //returns number of lines
    public int getLineCounter() {
        return lineCounter;
    }
    //sets number of lines
    public void setLineCounter(int setter) {       
        lineCounter = setter;
    }
}
