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
public class User_List {
    private static List<User> list = new ArrayList<User>();

    public static void add(User user){
        list.add(user);
    }

    @XmlElementRef
    public List<User> getList() {
        return list;
    }

}
