import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
public class WriteToFile {
    public WriteToFile(HashMap<String, List<String>> first, HashMap<String, List<String>> second, HashMap<String, List<String>> third,
    HashMap<String, List<String>> fourth, HashMap<String, List<String>> fifth, List<Camper> campers) throws IOException{ 
        FileWriter writer = new FileWriter("lib/results/period.csv");
        write(first, 1, writer);
        write(second, 2, writer);
        write(third, 3, writer);
        write(fourth, 4, writer);
        write(fifth, 5, writer);
        writer.close();
        writeCamper(campers);

    }

    public void write(HashMap<String, List<String>> period, int p, FileWriter f) throws IOException{
        int count = 0;
        for(String k: period.keySet()){
            StringBuilder line = new StringBuilder();
            line.append(p+",");
            line.append(k+",");
            List<String> temp = period.get(k);
            int localCount = 0;
            for(String s: temp){
                if (localCount == temp.size() - 1){
                    line.append(s); 
                }
                else{
                    line.append(s+",");  
                }
                localCount++;
                count++;
            }
            f.write(line+"\n");
        }
        System.out.println(count);
    }

    public void writeCamper(List<Camper> campers) throws IOException{
        FileWriter writer = new FileWriter("lib/results/scheduleByCamper.csv");
        for (Camper c: campers){
            StringBuilder line = new StringBuilder();
            line.append(c.getLast()+",");
            line.append(c.getFirst()+",");
            line.append(c.getCabin()+",");
            List<String> activity = c.getClasses();
            for (int i = 0; i< activity.size(); i++){
                if(i == activity.size()-1){
                    line.append(activity.get(i));
                }
                else{
                    line.append(activity.get(i)+","); 
                }
            }
            writer.write(line + "\n");
        }
        writer.close();
    }

    public void declaration(){
        System.out.println("All files have been written to csv");
    }
    
}
