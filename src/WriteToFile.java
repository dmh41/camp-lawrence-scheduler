import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    public WriteToFile(List<HashMap<String, List<String>>> periods, List<Camper> campers,
    HashMap<String, List<String>> missed, int ave) throws IOException{ 
        FileWriter writer = new FileWriter("lib/results/period.csv");
        int count = 1;
        for(HashMap<String, List<String>> m: periods){
            write(m, count, writer);
            count++;
        }
        writer.close();
        writeCamper(campers);
        writeAccuracy(missed, ave);

    }

    public void write(HashMap<String, List<String>> period, int p, FileWriter f) throws IOException{
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
            }
            f.write(line+"\n");
        }
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

    public void writeAccuracy(HashMap<String, List<String>> missed, int ave) throws IOException{
        FileWriter writer = new FileWriter("lib/results/accuracy.csv");
        for(String k: missed.keySet()){
            StringBuilder line = new StringBuilder();
            line.append(k+ ",");
            int count = 0;
            for(String s: missed.get(k)){
                if(count == missed.get(k).size() - 1){
                    line.append(s);
                }
                else{
                    line.append(s+","); 
                }
            }
            writer.write(line + "\n");
        }
        writer.write("Code Accuracy,"+ave);
        writer.close();
    }

    public void declaration(){
        System.out.println("All data has been written to csv.");
    }
    
}
