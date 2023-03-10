import java.util.*;

public class Accuracy {
    
    private List<Camper> campers;

    public Accuracy(List<Camper> c){
        this.campers = c;
    }

    public List<Double> findAccPref(){
        List <Double> result = new ArrayList<>();
        for(int i = 0; i<6;i++){
            result.add(0.0);
        }

        for(Camper c: campers){
            List<String> prefs = c.getPreference();
            for(int i = 0; i < prefs.size(); i++){
                if(c.getClasses().contains(prefs.get(i))){
                    double temp = result.get(i);
                    temp++;
                    result.set(i, temp);
                }
            }
        }

        List<Double> end = new ArrayList<>();
        for(Double d: result){
            d = (d/(campers.size()))*100;
            end.add(d);
        }

        return end;
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


}
