import java.util.*;

public class Accuracy {
    
    private List<Camper> campers;

    public Accuracy(List<Camper> c){
        this.campers = c;
    }


    public Integer findAccClass(){
        double ave = 0.0;

        for(Camper c: campers){
            double camperTotal = 0.0;
            for(int i = 0; i < c.getClasses().size(); i++){
                if(c.getClasses().get(i).equals("Swimming")){
                    continue;
                }
                if(c.getPreference().contains(c.getClasses().get(i))){
                    camperTotal++;
                }
            }
            double camperAve = camperTotal/((c.getClasses().size()) - 1);
            ave = ave + camperAve;
        }
        double temp = ave/(campers.size());
        int result = ((int)(temp*100));
        return(result);
    }

    public HashMap<String, List<String>> missedPref(){
        HashMap<String, List<String>> missed = new HashMap<>();
        for(Camper c: campers){
            for(int i = 0; i < c.getClasses().size(); i++){
                if(c.getClasses().get(i).equals("Swimming")){
                    continue;
                }
                if(!(c.getPreference().contains(c.getClasses().get(i)))){
                    if(missed.keySet().contains(c.getName())){
                        missed.get(c.getName()).add(c.getClasses().get(i));
                    }
                    else{
                        List<String> place = new ArrayList<>();
                        place.add(c.getClasses().get(i));
                        missed.put(c.getName(), place);
                    }
                }
            }
        }
        return missed;
    }

}
