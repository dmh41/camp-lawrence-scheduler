import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Schedule {
    
    private HashMap<String,List<Integer>> cap;
    private List<Camper> campers;
    private int currentPeriod;
    private List<String> classRestrictions;
    
    public Schedule() throws FileNotFoundException{
        ReadFromFile r = new ReadFromFile("lib/data/data.csv");
        List<List<String>> data = r.readData();
        campers = new ArrayList<>();
        int dataWidth = data.get(0).size();
        for (int i = 0; i < data.size(); i++){

            String last = data.get(i).get(0);
            String first = data.get(i).get(1);
            String name = first+" "+last;
            List<String> pref = new ArrayList<>();

            for(int j = 4; j < dataWidth; j++){
                pref.add(data.get(i).get(j));
            }
            int cabin = Integer.parseInt(data.get(i).get(2));
            int swim = Integer.parseInt(data.get(i).get(3));
            Camper c = new Camper(name,last,first,pref,cabin,swim);
            campers.add(c);
        }
        createCap();
    }

    
    public HashMap<String,List<Integer>> createCap() throws FileNotFoundException{
        cap = new HashMap<>();
        File classes = new File("lib/data/classes.csv");
        Scanner scan = new Scanner(classes);
        while(scan.hasNextLine()){
            String[] temp = scan.nextLine().split(",");
            List<Integer> i = new ArrayList<>();
            i.add(Integer.parseInt(temp[1]));
            i.add(Integer.parseInt(temp[2]));
            cap.put(temp[0],i);
        }
        scan.close();
        return cap;
    }
    

    public HashMap<String, List<String>> resetPeriod(){
        HashMap<String, List<String>> period = new HashMap<>();
        for(String k: cap.keySet()){
            List<String> temp = new ArrayList<>();
            period.put(k, temp);
        }
        return period;
    }

    public Camper findCamper(String name){
        for(Camper c: campers){
            if (c.getName() == name){
                return (c);
            }
            else{
                continue;
            }
        }
        Camper temp = new Camper(null,null,null,null,-1,-1);
        return temp;  
    }
    
    public List<Integer> periodToSwim(boolean findNext){
        List<Integer> periodToSwim = new ArrayList<>();
        int p = currentPeriod;
        if(findNext == true){
            p++;
        }
        if (p == 1){
            periodToSwim.add(20);
            periodToSwim.add(17);
            periodToSwim.add(16);
        }
        else if(p == 2){
            //periodToSwim.add(16);
            periodToSwim.add(15);
            periodToSwim.add(11);
            periodToSwim.add(10);
        }
        else if(p == 3){
            periodToSwim.add(9);
            periodToSwim.add(8);
            periodToSwim.add(7);
            
        }
        else if(p == 4){
            periodToSwim.add(13);
            periodToSwim.add(5); 
            periodToSwim.add(6);
        }
        else{
            periodToSwim.add(1);
            periodToSwim.add(4);
            periodToSwim.add(3);
        }
        return periodToSwim;
    } 
    
    public HashMap<String, List<String>> period() throws FileNotFoundException{
        HashMap<String,List<String>> period = resetPeriod();
        List<Integer> cabinToSwim = periodToSwim(false);
        List<Integer> nextSwim = periodToSwim(true);
        for (Camper c : campers){
            int count = 0;
            boolean done = false;
            if(cabinToSwim.contains(c.getCabin()) && c.getSwim() == 1){
                period.get("Swimming").add(c.getName());
                c.getClasses().add("Swimming");
                done = true;
            }
            else if(c.getClasses().size() == currentPeriod){
                period.get("Sailing").add(c.getName());
                done = true;
            }
            else{
                while (done == false){
                    if (count == c.getPreference().size()){
                        for(String key: period.keySet()){
                            if(done == true || c.getClasses().contains(key)){
                                continue;
                            }
                            if((period.get(key).size() < cap.get(key).get(0))){
                                period.get(key).add(c.getName());
                                c.getClasses().add(key);
                                System.out.println(c.getName());
                                System.out.println(currentPeriod);
                                done = true;
                            }
                        }
                    }
                    else if(c.getClasses().contains(c.getPreference().get(count))){
                        count++;
                    }
                    else if(classRestrictions.contains(c.getPreference().get(count))){
                        count++;
                    }
                    else{ 
                        String pref = c.getPreference().get(count);
                        if(pref.equals("Sailing")){
                            if((currentPeriod == 2 || currentPeriod == 4) &&
                            (!(nextSwim.contains(c.getCabin())) && c.getSwim() == 1)){
                                if (period.get(pref).size() < cap.get(pref).get(0)){
                                    period.get(pref).add(c.getName());
                                    c.getClasses().add(pref);
                                    c.getClasses().add(pref);
                                    done = true;
                                }
                                else{
                                    count++;
                                }
                            }
                            else{
                                count++;
                            }
                        }          
                        else{
                            if (period.get(pref).size() < cap.get(pref).get(0)){
                                period.get(pref).add(c.getName());
                                c.getClasses().add(pref);
                                done = true;
                            }
                            else{
                                count++;
                            }
                        }
                    }     
                }
            }    
        }
        ///* 
        for(String k: period.keySet()){
            if(period.get(k).size() < cap.get(k).get(1)){
                List<String> tester = period.get(k);
                for(String name: tester){
                    Camper temp = findCamper(name);
                    if(temp.getName() == null){
                        System.out.println("NULL");
                    }
                    else{
                        boolean done = false;
                        for (int i = 0; i < temp.getPreference().size();i++){
                            String tempPref = temp.getPreference().get(i);
                            if(classRestrictions.contains(tempPref)){
                                continue;
                            }
                            if(period.get(tempPref).size() == 0 || temp.getClasses().contains(tempPref)){
                                continue;
                            }
                            if(tempPref.equals("Sailing")){
                                if((currentPeriod == 3 || currentPeriod == 5) || 
                                (nextSwim.contains(temp.getCabin()) && temp.getSwim() == 1)){
                                    continue;
                                }
                                temp.getClasses().remove(temp.getClasses().size() - 1);
                                period.get(tempPref).add(temp.getName());
                                done = true;
                                temp.getClasses().add(tempPref);
                                temp.getClasses().add(tempPref);
                                break;
                            }
                            if(period.get(tempPref).size() < cap.get(tempPref).get(0)){
                                temp.getClasses().remove(temp.getClasses().size() - 1);
                                period.get(tempPref).add(temp.getName());
                                done = true;
                                temp.getClasses().add(tempPref);
                                break; 
                            }
                        }
                         
                        // Comment following if and loop to see how accurate code runs on data  
                        if(done == false){
                            //remove comment on line below to see who doesn't get preference and which period
                            System.out.print(currentPeriod + ": ");
                            System.out.println(temp.getName());
                            for(String key: period.keySet()){
                                if(key.equals("Sailing")){
                                    continue;
                                }
                                if(key.equals("Advanced Ropes")){
                                    continue;
                                }
                                if(done == true || temp.getClasses().contains(key)){
                                    continue;
                                }
                                if((period.get(key).size() < cap.get(key).get(0)) && (period.get(key).size() != 0)){
                                    temp.getClasses().remove(temp.getClasses().size() - 1);
                                    period.get(key).add(temp.getName());
                                    temp.getClasses().add(key);
                                    done = true;
                                }
                            }
                        }
                        
                    }
                }
                period.get(k).clear();
            }
        }
        // */
        return period;
    }

public static void main (String[] args) throws IOException{

    Schedule s = new Schedule();
    HashMap<String, List<String>> first = new HashMap<String, List<String>>();
    s.currentPeriod = 1;
    s.classRestrictions = new ArrayList<>();
    s.classRestrictions.add("Frisbee");
    s.classRestrictions.add("Rugby");
    s.classRestrictions.add("Advanced Ropes");
    s.classRestrictions.add("SuperRopes");
    first = s.period();
    s.classRestrictions.clear();
    Collections.shuffle(s.campers);

    HashMap<String, List<String>> second = new HashMap<String, List<String>>();
    s.currentPeriod = 2;
    s.classRestrictions.add("Frisbee");
    s.classRestrictions.add("Soccer");
    //s.classRestrictions.add("Advanced Ropes");
    s.classRestrictions.add("Super Ropes");
    second = s.period();
    s.classRestrictions.clear();
    Collections.shuffle(s.campers);

    HashMap<String, List<String>> third = new HashMap<String, List<String>>();
    s.currentPeriod = 3;
    s.classRestrictions.add("Soccer");
    s.classRestrictions.add("Rugby");
    s.classRestrictions.add("Super Ropes");
    third = s.period();
    s.classRestrictions.clear();
    Collections.shuffle(s.campers);

    HashMap<String, List<String>> fourth = new HashMap<String, List<String>>();
    s.currentPeriod = 4;
    s.classRestrictions.add("Soccer");
    s.classRestrictions.add("Rugby");
    s.classRestrictions.add("Junior Ropes");
    //s.classRestrictions.add("NoviceRopes");
    fourth = s.period();
    s.classRestrictions.clear();
    Collections.shuffle(s.campers);

    HashMap<String, List<String>> fifth = new HashMap<String, List<String>>();
    s.currentPeriod = 5;
    s.classRestrictions.add("Frisbee");
    s.classRestrictions.add("Rugby");
    //s.classRestrictions.add("Junior Ropes");
    //s.classRestrictions.add("Novice Ropes");
    fifth = s.period();

    WriteToFile result = new WriteToFile(first, second, third, fourth, fifth, s.campers);
    result.declaration();

    Accuracy a = new Accuracy(s.campers);
    Double ave = a.findAccClass();
    System.out.println(ave);
}

}