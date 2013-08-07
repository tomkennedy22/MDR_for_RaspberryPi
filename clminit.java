/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author PeterTsongalis
 */
public class clmInit {

  
    public int i = 0;
    //create all objects for use
    private List<Command_Line_MDR> clm;
    private List<preferenceEntry> pE;
    private List<String> outputString;
    String holder;
    public Font myFont;
    private gpioPins gpio;

    
    public clmInit(gpioPins gpio) {
        //allow for gpio usage
        this.gpio = gpio;
        
    }
    
    
    
    public List<String> takeList(List<String> names) throws InterruptedException {


        //init all lists
        pE = new ArrayList<preferenceEntry>();
        clm = new ArrayList<Command_Line_MDR>();
        outputString = new ArrayList<String>();

        for (int x = 0; x < names.size(); x++) {
            //gets preferences
            System.out.println(names.get(x) + " clmInt");
            pE.add(new preferenceEntry(names.get(x), gpio));

            //System.out.println("getting prefs for: " + names.get(x));

            pE.get(x).runPrefEnt();

            //keep screen open
            while (!pE.get(x).getClosed()) {}
                //run clm if parameters are met
                if (pE.get(x).getMin() != 0 && pE.get(x).getMax() != 0 && pE.get(x).getCV() != 0) {
                    clm.add(new Command_Line_MDR(pE.get(x).getMin(), pE.get(x).getMax(), pE.get(x).getCV(), names.get(x)));
                    System.out.println("added: " + names.get(x) + " to clm list");
                    Thread.sleep(100);
                }
        }

        outputString = runForLoop(clm);

        return outputString;

    }

    public List<String> runForLoop(List<Command_Line_MDR> clm) throws InterruptedException {

        for (i = 0; i < clm.size(); i++) {

            try {         
                holder = clm.get(i).run();
                
                //sets new maximum as mdr's are running              
                outputString.add(holder);
            } catch (Exception e) {

                System.out.print("Error");
            }
        }
        
        Thread.sleep(1000);
        
        return outputString;
    }
}
