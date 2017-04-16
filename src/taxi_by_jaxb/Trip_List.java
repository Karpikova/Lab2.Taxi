package taxi_by_jaxb;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/*
 * ${Classname}
 * 
 * Version 1.0 
 * 
 * 16.04.2017
 * 
 * Karpikova
 */
@XmlRootElement
public class Trip_List {
    private static List<Trip> list = new ArrayList<Trip>();

    public static void add(Trip trip){
        list.add(trip);
    }

    @XmlElementRef
    public List<Trip> getList() {
        return list;
    }
}
