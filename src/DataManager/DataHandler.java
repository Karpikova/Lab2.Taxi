package DataManager;

import com.company.Main;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import taxi_by_jaxb.*;
import taxi_by_jaxb.Driver;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * DataHandler methods
 */
public class DataHandler {

    static {
        PropertyConfigurator.configure("log4j.properties");
    }
    private static final Logger logger =  Logger.getLogger(Main.class);

    /**
     * The method codes a password to int. We need
     * @param password
     * @param registration_date
     * @return encoded password
     */
    public static int code_Password(String password, XMLGregorianCalendar registration_date){
        int secret_number = 1;
        String file_name = "D:\\1.txt";
        try {
            secret_number = Integer.parseInt((new BufferedReader(new FileReader(file_name)).readLine()));

        } catch (IOException e) {
            logger.error(e);
        }
        int result = (1+(registration_date.toGregorianCalendar().get(Calendar.MONTH)))
                *(registration_date.toGregorianCalendar().get(Calendar.YEAR))
                *(registration_date.toGregorianCalendar().get(Calendar.DAY_OF_MONTH))
                *secret_number;
        for (char symbol:
             password.toCharArray()) {
            if (result > 32000){//чтобы не произошло переполнение int
                result = (int) Math.sqrt((double) result);
            }
            result = result*(int)symbol;
        }
        result = result ;
        return result;
    }

    /**
     * Connection to local DataBase, postgresql
     * @return Connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection initConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/taxi",
                "postgres", "79");
        return connection;
    }

    /**
     * Loading data from DataBase taxi to XML
     */
    public static void data_to_XML() {
      //  user_to_XML("xml\\user.xml");
     //   driver_to_XML("xml\\driver.xml");
        //  passenger_to_XML("xml\\passenger.xml");
    }

    /**
     * Loading data from XML to DataBase taxi
     */
    public static void data_to_DB() {
        XML_to_user("xml\\user.xml");
        XML_to_driver("xml\\driver.xml");
        XML_to_passenger("xml\\passenger.xml");
    }

    private static void passenger_to_XML(String file_name) {
        Passenger_List list = new Passenger_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"Passenger\" Pass LEFT JOIN " +
                    "PUBLIC.\"User\" Us ON Pass.users_pkey = Us.users_pkey");
            Passenger passenger;
            while (resultSet.next()){
                passenger = GetPassangerFrom_resultSet(resultSet);
                list.add(passenger);
            }
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Passenger_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        }catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static Passenger GetPassangerFrom_resultSet(ResultSet resultSet) throws SQLException, DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Passenger element = new Passenger();
        User user = GetUserFrom_resultSet(resultSet);

        element.setUsersPkey(user);
        gregorianCalendar.setTime(resultSet.getDate("birth"));
        XMLGregorianCalendar xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setBirth(xgcal);
        element.setFullName(resultSet.getString("full_name"));
        return element;
    }


    private static void driver_to_XML(String file_name) {
        Driver_List list = new Driver_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"Driver\" Dr LEFT JOIN " +
                    "PUBLIC.\"User\" Us ON Dr.users_pkey = Us.users_pkey");
            Driver driver;
            while (resultSet.next()){
                driver = GetDriverFrom_resultSet(resultSet);
                list.add(driver);
            }
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Driver_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        }catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static Driver GetDriverFrom_resultSet(ResultSet resultSet) throws SQLException, DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Driver element = new Driver();
        User user = GetUserFrom_resultSet(resultSet);

        element.setUsersPkey(user);
        gregorianCalendar.setTime(resultSet.getDate("birth"));
        XMLGregorianCalendar xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setBirth(xgcal);
        element.setCarDescription(resultSet.getString("car_description"));
        element.setCarNumber(resultSet.getString("car_number"));
        element.setFullName(resultSet.getString("full_name"));
        element.setPassport(resultSet.getString("passport"));
        return element;
    }

    private static void user_to_XML(String file_name) {
        User_List list = new User_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"User\"");
            User user;
            while (resultSet.next()){
                user = GetUserFrom_resultSet(resultSet);
                User_List.add(user);
            }
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(User_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        }catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static User GetUserFrom_resultSet(ResultSet resultSet) throws SQLException, DatatypeConfigurationException {
        User element = new User();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        element.setLogin(resultSet.getString("login"));
        element.setUserPassword(resultSet.getInt("users_password"));
        gregorianCalendar.setTime(resultSet.getDate("last_login"));
        XMLGregorianCalendar xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setLastLogin(xgcal);
        element.setUsersPkey(resultSet.getInt("users_pkey"));
        gregorianCalendar.setTime(resultSet.getDate("registration_date"));
        xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setRegistrationDate(xgcal);
        return element;
    }

    private static void XML_to_user(String file_name) {
        try {
            Connection connection = initConnection();
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(User_List.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            User_List user_list = (User_List) jaxbUnmarshaller.unmarshal(file);
            for (User user:
                    user_list.getList()) {
                create_user_db(connection, user);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void create_user_db(Connection connection, User user) throws SQLException {
        PreparedStatement preparedStatement;
        java.sql.Date curr_date = new java.sql.Date((new java.util.Date()).getTime());
        java.sql.Date sqlDate_lastLogin;
        java.sql.Date sqlDate_registrationDate;
        if (!check_exisits_of_User(connection, user.getUsersPkey()))
        {
            preparedStatement =
                    connection.prepareStatement("INSERT INTO PUBLIC.\"User\" " +
                            "(login, users_password, last_login, users_pkey, registration_date)" +
                            "VALUES (?,?,?,?,?)");
        }
        else {
            preparedStatement =
                    connection.prepareStatement("UPDATE PUBLIC.\"User\" " +
                            "SET login=?, users_password=?, last_login=?, users_pkey=?, registration_date=? " +
                            "WHERE users_pkey = ?");
            preparedStatement.setLong(6, user.getUsersPkey());
        }
        preparedStatement.setString(1, user.getLogin() == null ? "" : user.getLogin());
        preparedStatement.setInt(2, user.getUserPassword());

        if (user.getLastLogin() == null) {
            sqlDate_lastLogin = curr_date;
        }
        else {
            sqlDate_lastLogin = new java.sql.Date(user.getLastLogin().toGregorianCalendar().getTimeInMillis());
        }
        preparedStatement.setDate(3, sqlDate_lastLogin);
        preparedStatement.setLong(4, user.getUsersPkey());

        if (user.getRegistrationDate() == null) {
            sqlDate_registrationDate = curr_date;
        }
        else {
            sqlDate_registrationDate = new java.sql.Date(user.getRegistrationDate().toGregorianCalendar().getTimeInMillis());
        }
        preparedStatement.setDate(5, sqlDate_registrationDate);
        preparedStatement.executeUpdate();
    }

    private static void XML_to_passenger(String file_name) {
        try {
            Connection connection = initConnection();
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Passenger_List.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Passenger_List passenger_list = (Passenger_List) jaxbUnmarshaller.unmarshal(file);

            for (Passenger passenger:
                    passenger_list.getList()) {
                PreparedStatement preparedStatement =
                        connection.prepareStatement("SELECT COUNT(*) count FROM PUBLIC.\"Passenger\" " +
                                "WHERE users_pkey = ?");
                preparedStatement.setLong(1, passenger.getUsersPkey().getUsersPkey());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                long users_pkey = passenger.getUsersPkey().getUsersPkey();
                if (resultSet.getInt("count") == 0)
                {
                    boolean userExists = check_exisits_of_User(connection, users_pkey);
                    if (!userExists)
                    {
                        User user = new User(); //dummy user for multithreads
                        user.setUsersPkey(users_pkey);
                        create_user_db(connection, user);
                    }
                    preparedStatement =
                            connection.prepareStatement("INSERT INTO PUBLIC.\"Passenger\" " +
                                    "(users_pkey, full_name, birth)" +
                                    "VALUES (?,?,?)");
                }
                else {
                    preparedStatement =
                            connection.prepareStatement("UPDATE PUBLIC.\"Passenger\" " +
                                    "SET users_pkey=?, full_name=?, birth=?" +
                                    "WHERE users_pkey = ?");
                    preparedStatement.setLong(4, users_pkey);
                }
                preparedStatement.setLong(1, users_pkey);
                preparedStatement.setString(2, passenger.getFullName());
                java.sql.Date sqlDate = new java.sql.Date(passenger.getBirth().toGregorianCalendar().getTimeInMillis());
                preparedStatement.setDate(3, sqlDate);
                preparedStatement.executeUpdate();
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void XML_to_driver(String file_name) {
        try {
            Connection connection = initConnection();
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Driver_List.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Driver_List driver_list = (Driver_List) jaxbUnmarshaller.unmarshal(file);

            for (Driver driver:
                    driver_list.getList()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT COUNT(*) count FROM PUBLIC.\"Driver\" " +
                            "WHERE users_pkey = ?");
            preparedStatement.setLong(1, driver.getUsersPkey().getUsersPkey());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long users_pkey = driver.getUsersPkey().getUsersPkey();
            if (resultSet.getInt("count") == 0)
            {
                boolean userExists = check_exisits_of_User(connection, users_pkey);
                if (!userExists)
                {
                    User user = new User(); //dummy user for multithreads
                    user.setUsersPkey(users_pkey);
                    create_user_db(connection, user);
                }
                preparedStatement =
                        connection.prepareStatement("INSERT INTO PUBLIC.\"Driver\" " +
                                "(users_pkey, full_name, car_number, car_description, passport, birth)" +
                                "VALUES (?,?,?,?,?,?)");
            }
            else {
                preparedStatement =
                        connection.prepareStatement("UPDATE PUBLIC.\"Driver\" " +
                                "SET users_pkey=?, full_name=?, car_number=?, car_description=?, passport=?, birth=?" +
                                "WHERE users_pkey = ?");
                preparedStatement.setLong(7, users_pkey);
            }
                preparedStatement.setLong(1, users_pkey);
                preparedStatement.setString(2, driver.getFullName());
                preparedStatement.setString(3, driver.getCarNumber());
                preparedStatement.setString(4, driver.getCarDescription());
                preparedStatement.setString(5, driver.getPassport());
                java.sql.Date sqlDate = new java.sql.Date(driver.getBirth().toGregorianCalendar().getTimeInMillis());
                preparedStatement.setDate(6, sqlDate);
                preparedStatement.executeUpdate();
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean check_exisits_of_User(Connection connection, long user_users_pkey) {
        try {
            connection = initConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT COUNT(*) count FROM PUBLIC.\"User\" " +
                            "WHERE users_pkey = ?");
            preparedStatement.setLong(1, user_users_pkey);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt("count") == 0) {
                return false;
            } else {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
