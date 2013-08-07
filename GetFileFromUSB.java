import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.midi.SysexMessage;

public class GetFileFromUSB {

      //create strings to be used as terminal functions
    private String mount_drive = "sudo mount /dev/sda1 /mnt/MDR_Data_Drive";
    private String unmount_drive = "sudo umount /mnt/MDR_Data_Drive";
    public List<String> getFile() throws IOException {
        //creates list to hold all files found
        List<String> files = new ArrayList<String>();
        //creates string to mount usb to MDR_Data_Drive
        //String mount_drive = "sudo mount /dev/sda1 /mnt/MDR_Data_Drive";
        //finds all files in MDR_Data_Drive
        String list = "/mnt/MDR_Data_Drive";
        //executes mounting of usb
       // Process mount = Runtime.getRuntime().exec(mount_drive);
        //finds all files on drive
        File dir = new File(list);
        File[] children = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                //accepts only txt files
                return file.getName().endsWith(".txt");
            }
        });
        
        for (int i = 0; i < children.length; i++) {
            //adds all files found to files list
            System.out.println(children[i].getAbsolutePath());
            System.out.print("Simple Name= " + children[i].getName());
            files.add(children[i].getAbsolutePath());
        }
        //return files found to previous screen
        return files;
    }
    
    public void mount() throws IOException{
        //String mount_drive = "sudo mount /dev/sda1 /mnt/MDR_Data_Drive";
        //executes mounting of usb
        Process mount = Runtime.getRuntime().exec(mount_drive);
    }
    public void unmount()throws IOException{
        Process mount = Runtime.getRuntime().exec(unmount_drive);
    }
}
