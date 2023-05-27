import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Schedule {
    
    private HashMap<String,List<Integer>> cap;
    private List<Camper> campers;
    private int currentPeriod;
    private List<String> classRestrictions;
    private List<String> swimDesignation;
    private Integer numPeriods;
    
    public Schedule(List<List<String>> p, List<List<String>> a){

        campers = new ArrayList<>();
        int dataWidth = p.get(0).size();
        for (int i = 0; i < p.size(); i++){

            String last = p.get(i).get(2);
            String first = p.get(i).get(1);
            String name = first+" "+last;
            List<String> pref = new ArrayList<>();

            for(int j = 5; j < dataWidth; j++){
                pref.add(p.get(i).get(j));
            }
            int cabin = Integer.parseInt(p.get(i).get(4));
            int swim = 1;
            Camper c = new Camper(name,last,first,pref,cabin,swim);
            campers.add(c);
        }
        createCap(a);
    }

    
    public HashMap<String,List<Integer>> createCap(List<List<String>> a){
        cap = new HashMap<>();
        swimDesignation = new ArrayList<>();
        for(int i = 0; i < a.size(); i++){
            String temp = a.get(i).get(1);
            if (temp.equals("Swimming")){
                String [] holder = a.get(i).get(5).split(":");
                swimDesignation = new ArrayList<>();
                for(int x = 0; x < holder.length; x++){
                    swimDesignation.add(holder[x]);
                }
            }
            List<Integer> limits = new ArrayList<>();
            limits.add(Integer.parseInt(a.get(i).get(3)));
            limits.add(Integer.parseInt(a.get(i).get(2)));
            cap.put(temp, limits);
        }
        return cap;
    }

    public void createRestriction (List<List<String>> a){
        classRestrictions = new ArrayList<>();
        for(int i = 0; i < a.size(); i++){
            String curr = a.get(i).get(4);
            for(char c: curr.toCharArray()){
                if(c == ','){
                    continue;
                }
                if(Character.getNumericValue(c) == currentPeriod){
                    classRestrictions.add(a.get(i).get(1));
                }
            }
        }
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
    
    public List<Integer> periodSwim(boolean findNext){
        List<Integer> periodToSwim = new ArrayList<>();
        int p = currentPeriod-1;
        if(findNext == true && p < numPeriods-1){
            p++;
        }
        String [] temp = swimDesignation.get(p).split(",");
        for(int i = 0; i < temp.length; i++){
            periodToSwim.add(Integer.parseInt(temp[i]));
        }
        return periodToSwim;
    } 
    
    public HashMap<String, List<String>> period() throws FileNotFoundException{
        HashMap<String,List<String>> period = resetPeriod();
        List<Integer> cabinToSwim = periodSwim(false);
        List<Integer> nextSwim = periodSwim(true);
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
                         
                        if(done == false){
                            for(String key: period.keySet()){
                                if(key.equals("Sailing")){
                                    continue;
                                }
                                // if(key.equals("Advanced Ropes")){
                                //     continue;
                                // }
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
        return period;
    }

public static void main (String[] args) throws IOException{

    List<HashMap<String, List<String>>> periods = new ArrayList<>();

    SheetStart module  = new SheetStart();

    List<List<String>> data = module.getPref();
    List<List<String>> activities = module.getAct();

    Schedule s = new Schedule(data,activities);

    s.numPeriods = 5;

    for(int i = 0; i < s.numPeriods; i++){
        HashMap<String, List<String>> curr = new HashMap<String, List<String>>();
        s.currentPeriod = i+1;
        s.createRestriction(activities);
        curr = s.period();
        periods.add(curr);
        s.classRestrictions.clear();
        Collections.shuffle(s.campers);
    }

    module.clearData();
    module.writeOutput(s.campers);

}

}