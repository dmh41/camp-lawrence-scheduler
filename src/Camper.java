import java.util.*;

public class Camper{

private String name;
private List<String> preferences;
private int cabin;
private int swim;
private List<String> classes;


public Camper(String n, List<String> list, List<Integer> cs){
    this.name = n;
    this.preferences = list;
    this.cabin = cs.get(0);
    this.swim = cs.get(1);
    this.classes = new ArrayList<>();
}

public String getName(){
    return name;
}

public List<String> getPreference(){
    return preferences;
}

public int getCabin(){
    return cabin;
}

public int getSwim(){
    return swim;
}

public List<String> getClasses(){
return classes;
}

}