import java.util.*;

public class Camper{

private String name;
private String first;
private String last;
private List<String> preferences;
private int cabin;
private int swim;
private List<String> classes;
private String campName;


public Camper(String n, String l, String f, List<String> list, int c, int s, String camp){
    this.name = n;
    this.first = f;
    this.last = l;
    this.preferences = list;
    this.cabin = c;
    this.swim = s;
    this.classes = new ArrayList<>();
    this.campName = camp;
}

public String getName(){
    return name;
}

public String getFirst(){
    return first;
}

public String getLast(){
    return last;
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

public String getCamp(){
    return campName;
}

}