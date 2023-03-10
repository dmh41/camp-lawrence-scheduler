import java.util.*;

public class Accuracy {
    
    private List<Camper> campers;

    public Accuracy(List<Camper> c){
        this.campers = c;
    }


    public Double findAccClass(){
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
        
        return(ave/(campers.size()));
    }

    public List<HashMap<String, List<String>>> missedPref(){
        List<HashMap<String, List<String>>> missed = new ArrayList<>();
        for(Camper c: campers){
            for(int i = 0; i < c.getClasses().size(); i++){
                if(c.getClasses().get(i).equals("Swimming")){
                    continue;
                }
                if(!(c.getPreference().contains(c.getClasses().get(i)))){
                    
                }
            }

        }
        return missed;
    }

}
