
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;

public class OutputParse {

    //init lists to contain all output
    public List<String> DataList;
    public List<String[]> parsedData;
    //create list of column names
    public String[] columnNames =  {"Attributes", "CVC", "Bal. Acc. CV Training", 
                            "Bal. Acc. CV Testing", "Bal. Acc. Model Training", 
                            "Bal. Acc. Model Testing", "Bal. Acc. Overall"};
    

    public List<String[]> Parse(String outputString) {
        parsedData = new ArrayList<String[]>();
        String[] splitString = outputString.split("\\n");
        System.out.println(splitString[4]);
        DataList = new ArrayList<String>();
        //estimate difference of min and max
        int diff = (splitString.length - 14) / 4;
        //retrieves only the data output
        for (int x = 4; x <= (4 + (diff * 3)); x = x + 3) {

            String holder = splitString[x];
            System.out.println("holder= " + holder);
            DataList.add(holder);
        }
            //split at spaces
            for (int z = 0; z < DataList.size(); z++) {
                String newHolder = DataList.get(z);       
                parsedData.add(newHolder.split("\\s+"));    
            }
/*
                for (int a = 0; a < parsedData.size(); a++) {
                    for (int b = 0; b < parsedData.get(a).length; b++) {
                        System.out.println(parsedData.get(a)[b]);

                    }
                }
              */  
                
               return parsedData;
                
            }

    
    
    //makes the table for viewing
    public String[][] makeTable(List<String[]> pData) {
         
       String[][]  data = new String[7][pData.size() + 1];
     //copies column strings
         data [0][0] = columnNames[0];
         data [1][0] = columnNames[1];
         data [2][0] = columnNames[2];
         data [3][0] = columnNames[3];
         data [4][0] = columnNames[4];
         data [5][0] = columnNames[5];
         data [6][0] = columnNames[6];
         //moves data into table
         for (int x = 0; x < pData.size(); x++) {
             System.out.println("length "  + pData.get(x).length);
             for (int y = 0; y < pData.get(x).length; y++) {
                 
                 data[y][x+1] = pData.get(x)[y];
                 System.out.println(data[y][x]);
             }  
         }  
         return data;          
    }
   //retirves colum name
    public String[] getColumnNames(List<String[]> pData) {
        
        String[] names = new String[pData.size() + 1];

        for (int a = 0; a < names.length; a++) {
            
            names[a] = "Level " + Integer.toString(a);
           // System.out.println(names[a]);
        }
        names[0] = "";
        
        return names;
    }
}
   
