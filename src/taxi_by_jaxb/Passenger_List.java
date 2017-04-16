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
public class Passenger_List {
    private static List<Passenger> list = new ArrayList<Passenger>();

    public static void add(Passenger passenger){
        list.add(passenger);
    }

    @XmlElementRef
    public List<Passenger> getList() {
        return list;
    }
}
