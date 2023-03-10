import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class ReadFromFile {
    private String dataFilename;

    public ReadFromFile(String data){
        this.dataFilename = data;  
    }

    public List<List<String>> readData() throws FileNotFoundException{
        File dFile = new File(dataFilename);
        Scanner dScan = new Scanner(dFile);
        List<List<String>> data = new ArrayList<>();
        while(dScan.hasNextLine()){
            List<String> temp = Arrays.asList(dScan.nextLine().split(","));
            data.add(temp);
        }
        dScan.close();
        return data;
    }
}
