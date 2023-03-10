import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
public class WriteToFile {
    public WriteToFile(HashMap<String, List<String>> first, HashMap<String, List<String>> second, HashMap<String, List<String>> third,
    HashMap<String, List<String>> fourth, HashMap<String, List<String>> fifth, List<Camper> campers) throws IOException{ 
        write(first, "lib/results/1stPeriod.txt");
        write(second, "lib/results/2ndPeriod.txt");
        write(third, "lib/results/3rdPeriod.txt");
        write(fourth, "lib/results/4thPeriod.txt");
        write(fifth, "lib/results/5thPeriod.txt");
        writeCamper(campers);

    }

    public void write(HashMap<String, List<String>> period, String fileName) throws IOException{
        FileWriter writer = new FileWriter(fileName);
        writer.write(fileName.substring(12) + "\n"+"\n");
        int count = 0;
        for(String k: period.keySet()){
            writer.write(k+":  "+"\n");
            List<String> temp = period.get(k);
            for(String a: temp){
                writer.write(a+"\n");
                count++;
            }
            writer.write("\n");
        }
        System.out.println(count);
        writer.close();
    }

    public void writeCamper(List<Camper> campers) throws IOException{
        FileWriter writer = new FileWriter("lib/results/scheduleByCamper.txt");
        writer.write("Camper Schedule" + "\n"+"\n");
        writer.write("Name                   Cabin   Period 1            "+
        "Period 2            Period 3            Period 4            Period 5" + "\n\n");
        for (Camper c: campers){
            String line = c.getName();
            while(line.length()< 25){
                line = line + " ";
            }
            if(c.getCabin() >= 10){
                line = line + c.getCabin() + "    ";
            }
            else{
                line = line + c.getCabin() + "     ";
            }
            for(String s: c.getClasses()){
                String tempLine = s;
                while(tempLine.length()<20){
                    tempLine = tempLine+" ";
                }
                line = line + tempLine;
            }
            writer.write(line + "\n");
        }
        writer.close();
    }

    public void declaration(){
        System.out.println("All files have been written to text");
    }
    
}
