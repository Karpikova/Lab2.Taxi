package DataManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * DataHandler methods
 */
public class DataHandler {

    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    private static final Logger logger = Logger.getLogger(DataHandler.class);

    /**
     * Connection to local DataBase, postgresql
     *
     * @return Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection initConnection() {
        return ConnectionToDB.connection;
    }

    /**
     * Loading data from DataBase taxi to XML
     */
    public static void data_to_XML() {
        DataHandlerUser_toXML dataHandlerUser_toXML = new DataHandlerUser_toXML(initConnection(), "xml\\user.xml");
        Thread User_toXMLThread = new Thread(dataHandlerUser_toXML);
        User_toXMLThread.start();

        DataHandlerDriver_toXML dataHandlerDriver_toXML = new DataHandlerDriver_toXML(initConnection(), "xml\\driver.xml");
        Thread Driver_toXMLThread = new Thread(dataHandlerDriver_toXML);
        Driver_toXMLThread.start();

        DataHandlerPassenger_toXML dataHandlerPassenger_toXML = new DataHandlerPassenger_toXML(initConnection(), "xml\\passenger.xml");
        Thread Passenger_toXMLThread = new Thread(dataHandlerPassenger_toXML);
        Passenger_toXMLThread.start();

        DataHandlerTrip_toXML dataHandlerTrip_toXML = new DataHandlerTrip_toXML(initConnection(), "xml\\trip.xml");
        Thread Trip_toXMLThread = new Thread(dataHandlerTrip_toXML);
        Trip_toXMLThread.start();
    }

    /**
     * Loading data from XML to DataBase taxi
     */
    public static void data_to_DB() {
        DataHandlerXML_to_User dataHandlerXML_to_user = new DataHandlerXML_to_User(initConnection(), "xml\\user.xml");
        Thread XML_to_userThread = new Thread(dataHandlerXML_to_user);
        XML_to_userThread.start();

        DataHandlerXML_to_Driver dataHandlerXML_to_driver = new DataHandlerXML_to_Driver(initConnection(), "xml\\driver.xml");
        Thread XML_to_driverThread = new Thread(dataHandlerXML_to_driver);
        XML_to_driverThread.start();

        DataHandlerXML_to_Passenger dataHandlerXML_to_passenger = new DataHandlerXML_to_Passenger(initConnection(), "xml\\passenger.xml");
        Thread XML_to_passengerThread = new Thread(dataHandlerXML_to_passenger);
        XML_to_passengerThread.start();

        DataHandlerXML_to_Trip dataHandlerXML_to_trip = new DataHandlerXML_to_Trip(initConnection(), "xml\\trip.xml");
        Thread ML_to_tripThread = new Thread(dataHandlerXML_to_trip);
        ML_to_tripThread.start();
    }

    /**
     * The method codes a password to int. We need
     *
     * @param password
     * @param registration_date
     * @return encoded password
     */
    public static int code_Password(String password, XMLGregorianCalendar registration_date) {
        int secret_number = 1;
        String file_name = "D:\\1.txt";
        try {
            secret_number = Integer.parseInt((new BufferedReader(new FileReader(file_name)).readLine()));

        } catch (IOException e) {
            logger.error(e);
        }
        int result = (1 + (registration_date.toGregorianCalendar().get(Calendar.MONTH)))
                * (registration_date.toGregorianCalendar().get(Calendar.YEAR))
                * (registration_date.toGregorianCalendar().get(Calendar.DAY_OF_MONTH))
                * secret_number;
        for (char symbol :
                password.toCharArray()) {
            if (result > 32000) {//чтобы не произошло переполнение int
                result = (int) Math.sqrt((double) result);
            }
            result = result * (int) symbol;
        }
        result = result;
        return result;
    }

}
