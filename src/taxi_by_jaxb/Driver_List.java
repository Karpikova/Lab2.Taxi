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
public class Driver_List {
    private static List<Driver> list = new ArrayList<Driver>();

    public static void add(Driver driver){
        list.add(driver);
    }

    @XmlElementRef
    public List<Driver> getList() {
        return list;
    }
}
