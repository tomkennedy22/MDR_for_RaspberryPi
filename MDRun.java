
import java.io.IOException;
import java.util.List;

//package rpirun;
public class MDRun {

    public static void main(String[] args) throws IOException, InterruptedException {
       
        //test to see if this fixes first time detection problem
        GetFileFromUSB getFile = new GetFileFromUSB(); 
        getFile.mount();
        List<String>emptyFiles = getFile.getFile();
        //end test
        
        //execute driver file to begin process
        MDRgui go = new MDRgui();
        go.MDRgui();
    }
}
