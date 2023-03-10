import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class ReadFromFile {
    private String camperFilename;
    private String prefFilename;
    private String csFilename;

    public ReadFromFile(String ca, String p, String cs){
        this.camperFilename = ca;
        this.prefFilename = p;
        this.csFilename = cs;   
    }

    public List<String> readCamper() throws FileNotFoundException{
        File cFile = new File(camperFilename);
        Scanner cScan = new Scanner(cFile);
        List<String> camper = new ArrayList<>();
        while(cScan.hasNextLine()){
            camper.add(cScan.nextLine());
        }
        cScan.close();
        return camper;
    }

    public List<List<String>> readPref() throws FileNotFoundException{
        File pFile = new File(prefFilename);
        Scanner pScan = new Scanner(pFile);
        List<List<String>> pref = new ArrayList<>();
        while(pScan.hasNextLine()){
            List<String> pTemp = new ArrayList<>();
            String [] pTempLine = pScan.nextLine().split(",");
            for (String s: pTempLine){
                pTemp.add(s);
            }
            pref.add(pTemp);
        }
        pScan.close();
        return pref;

    }
    
    public List<List<Integer>> readCS() throws FileNotFoundException{
        File csFile = new File(csFilename);
        Scanner csScan = new Scanner(csFile);
        List<List<Integer>> cabin = new ArrayList<>();
        while(csScan.hasNextLine()){
            List<Integer> csTemp = new ArrayList<>();
            String [] csTempLine = csScan.nextLine().split(",");
            for(String s: csTempLine){
                csTemp.add(Integer.parseInt(s)); 
            }
            cabin.add(csTemp);
        }
        csScan.close();
        return cabin;
    } 

}
