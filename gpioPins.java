
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

/*
 * @author Tom Kennedy
 * @author PeterTsongalis
 */
public class gpioPins {

    public List<GpioPinDigitalInput> listPins;
    GpioController gpio;
    GpioPinDigitalInput encoder;
    GpioPinDigitalInput tab;
    GpioPinDigitalInput negativeSwitch;
    GpioPinDigitalInput enter;
    public int GUInumber = 0;
    public int filenumber = 0;
    
    
    public int numberEn = 0;
    

    public gpioPins() {

        listPins = new ArrayList<GpioPinDigitalInput>();


        gpio = GpioFactory.getInstance();

        encoder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        tab = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        negativeSwitch = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        enter = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);

        listPins.add(encoder);
        listPins.add(negativeSwitch);
        listPins.add(tab);
        listPins.add(enter);
        
    }

    //note for filechooser put buttons.get(number) in the parameter
    public void createEnterListenerFileChooser(final List<JButton> buttons) {
        
        enter.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                
                if (event.getState() == PinState.HIGH) {
                    buttons.get(filenumber).doClick();
                }
                
                
            }
        });
        
    }
    
    public void createTabListenerFileChooser(final List<JButton> filebuttons) {

        tab.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState() == PinState.HIGH) {

                    filenumber++;
                    filenumber = filenumber % filebuttons.size();
                    System.out.println("focus is on " + filenumber + " button");
                    filebuttons.get(filenumber).requestFocus();

                }


            }
        });

    }
    
    public void createEnterListenerPrefEnt(final List<JButton> buttons) {
        
        enter.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                
                if (event.getState() == PinState.HIGH) {
                    buttons.get(numberEn).doClick();
                }
                
                
            }
        });
        
    }
    
    public void createTabListenerForPref(final List<JTextField>textFields, final List<JButton>buttons ) {
         
        tab.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                if (event.getState() == PinState.HIGH) {
                    if (numberEn < 3) {
                        
                        numberEn++;
                        textFields.get(numberEn).requestFocus();
                        numberEn = numberEn % 5;
                        System.out.println("number = " + numberEn);
                    } else if (numberEn >= 3) {
                        
                        numberEn++;
                        buttons.get(numberEn-3).requestFocus();
                        numberEn = numberEn % 5;
                        System.out.println("number = " + numberEn);
                    }


                    System.out.println(" tab State Change: " + event.getPin() + " tabbed");
                }
            }
        });


    }
    
    
    public void createEnterListener(final JButton next) {

        enter.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                if (event.getState() == PinState.HIGH) {

                    next.doClick();

                }

            }
        });


    }

    public void createEncoderListener(final int[] Fields, final List<JTextField> textFields) {
        
        encoder.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                if (event.getState() == PinState.HIGH) {

                    Fields[numberEn] += (1);
                    if (numberEn < 3) {
                        textFields.get(numberEn).setText(Integer.toString(Fields[numberEn]));
                    }

                    System.out.println(" encoder State Change: " + event.getPin() + " = " + numberEn);
                }
            }
        });

    }

    public void createNegativeSwitchListener(final List<JTextField> textFields) {
        negativeSwitch.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState() == PinState.HIGH) {
                    textFields.get(numberEn).setText(Integer.toString(0));
                    numberEn = 0;
                    
                    

                }
            }
        });


    }

  
    public void createTabListenerMDRGUI(final JTable table) {
        tab.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState() == PinState.HIGH) {
                    GUInumber++;
                    GUInumber = GUInumber % table.getColumnCount();
                    table.requestFocus();
                    System.out.println("focus is on " + GUInumber + " column");
                    table.changeSelection(0, GUInumber, false, false);

                }
            }
        });
       
    }
    
    public void createChangeToShutdownListenerMDRGUI(final JButton exit) {
        
        negativeSwitch.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                 exit.requestFocus();
            }
        });
       
        
        
    }
    
    public void deleteListeners(GpioPinDigitalInput pin) {
        
        pin.removeAllListeners();
       
    }

}
