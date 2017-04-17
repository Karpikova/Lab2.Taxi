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

/**
 * DataHandler methods
 */
public class DataHandler {

    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    private static final Logger logger = Logger.getLogger(DataHandler.class);

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

    /**
     * Connection to local DataBase, postgresql
     *
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
        user_to_XML("xml\\user.xml");
        driver_to_XML("xml\\driver.xml");
        passenger_to_XML("xml\\passenger.xml");
        trip_to_XML("xml\\trip.xml");
    }

    /**
     * Loading data from XML to DataBase taxi
     */
    public static void data_to_DB() {
        XML_to_user("xml\\user.xml");
        XML_to_driver("xml\\driver.xml");
        XML_to_passenger("xml\\passenger.xml");
        XML_to_trip("xml\\trip.xml");
    }

    private static void trip_to_XML(String file_name) {
        Trip_List list = new Trip_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"Trip\" Trip LEFT JOIN " +
                    "PUBLIC.\"Driver\" Driver ON Driver.users_pkey_driver = Trip.driver_pkey LEFT JOIN " +
                    "PUBLIC.\"Passenger\" Passenger ON Passenger.users_pkey_pas = Trip.passenger_pkey");
            Trip trip;
            while (resultSet.next()) {
                trip = GetTripFrom_resultSet(resultSet);
                list.add(trip);
            }
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Trip_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void passenger_to_XML(String file_name) {
        Passenger_List list = new Passenger_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"Passenger\" Passenger LEFT JOIN " +
                    "PUBLIC.\"User\" Us ON Passenger.users_pkey_pas = Us.users_pkey");
            Passenger passenger;
            while (resultSet.next()) {
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

        try {
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Passenger_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void driver_to_XML(String file_name) {
        Driver_List list = new Driver_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"Driver\" Driver LEFT JOIN " +
                    "PUBLIC.\"User\" Us ON Driver.users_pkey_driver = Us.users_pkey");
            Driver driver;
            while (resultSet.next()) {
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

        try {
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Driver_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static Passenger GetPassangerFrom_resultSet(ResultSet resultSet) throws SQLException, DatatypeConfigurationException, ClassNotFoundException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Passenger element = new Passenger();

        ResultSet resultSet_user = getResultSet_User(resultSet, "users_pkey_pas");

        User user = GetUserFrom_resultSet(resultSet_user);

        element.setUsersPkey(user);
        gregorianCalendar.setTime(resultSet.getDate("birth"));
        XMLGregorianCalendar xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setBirth(xgcal);
        element.setFullName(resultSet.getString("full_name"));
        return element;
    }

    private static ResultSet getResultSet_User(ResultSet resultSet, String name_column) throws ClassNotFoundException, SQLException {
        Connection connection = initConnection();
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM PUBLIC.\"User\" WHERE users_pkey = ?");
        preparedStatement.setLong(1, resultSet.getLong(name_column));
        ResultSet resultSet_user = preparedStatement.executeQuery();
        resultSet_user.next();
        return resultSet_user;
    }


    private static Driver GetDriverFrom_resultSet(ResultSet resultSet) throws SQLException, DatatypeConfigurationException, ClassNotFoundException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Driver element = new Driver();

        ResultSet resultSet_user = getResultSet_User(resultSet, "users_pkey_driver");
        User user = GetUserFrom_resultSet(resultSet_user);

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

    private static Trip GetTripFrom_resultSet(ResultSet resultSet) throws SQLException, DatatypeConfigurationException, ClassNotFoundException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        Trip element = new Trip();
        Driver driver = GetDriverFrom_resultSet(resultSet);
        Passenger passenger = GetPassangerFrom_resultSet(resultSet);

        element.setAddressFrom(resultSet.getString("address_from"));
        element.setAddressTo(resultSet.getString("address_to"));
        gregorianCalendar.setTime(resultSet.getDate("date_start"));
        XMLGregorianCalendar xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setDateStart(xgcal);
        gregorianCalendar.setTime(resultSet.getDate("date_change"));
        xgcal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        element.setDateChange(xgcal);
        element.setDriverPkey(driver);
        element.setEstimate(resultSet.getBoolean("estimate"));
        element.setPassengerPkey(passenger);
        element.setPrice(resultSet.getInt("price"));
        element.setReport(resultSet.getString("report"));
        element.setStatus(Status.fromValue(resultSet.getString("status")));
        element.settrips_pkey(resultSet.getInt("trips_pkey"));

        return element;
    }


    private static void user_to_XML(String file_name) {
        User_List list = new User_List();
        try {
            Connection connection = initConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PUBLIC.\"User\"");
            User user;
            while (resultSet.next()) {
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

        try {
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(User_List.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(list, file);
        } catch (JAXBException e) {
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
            for (User user :
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

    private static void XML_to_passenger(String file_name) {
        try {
            Connection connection = initConnection();
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Passenger_List.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Passenger_List passenger_list = (Passenger_List) jaxbUnmarshaller.unmarshal(file);

            for (Passenger passenger :
                    passenger_list.getList()) {

                create_passenger_db(connection, passenger, 0);
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
            PreparedStatement preparedStatement;
            Connection connection = initConnection();
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Driver_List.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Driver_List driver_list = (Driver_List) jaxbUnmarshaller.unmarshal(file);

            for (Driver driver :
                    driver_list.getList()) {
                create_driver_db(connection, driver, 0);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void XML_to_trip(String file_name) {
        try {
            Connection connection = initConnection();
            File file = new File(file_name);
            JAXBContext jaxbContext = JAXBContext.newInstance(Trip_List.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Trip_List trip_list = (Trip_List) jaxbUnmarshaller.unmarshal(file);

            for (Trip trip :
                    trip_list.getList()) {
                PreparedStatement preparedStatement =
                        connection.prepareStatement("SELECT COUNT(*) count FROM PUBLIC.\"Trip\" " +
                                "WHERE trips_pkey = ?");
                preparedStatement.setLong(1, trip.gettrips_pkey());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                long trip_pkey = trip.gettrips_pkey();
                //дописать это!!!
                if (resultSet.getInt("count") == 0) {
                    long users_pkey_driver = trip.getDriverPkey().getUsersPkey().getUsersPkey();
                    boolean driverExists = check_exists_of_Driver(connection, users_pkey_driver);
                    long users_pkey_pas = trip.getPassengerPkey().getUsersPkey().getUsersPkey();
                    boolean passengerExists = check_exists_of_Passenger(connection, users_pkey_pas);
                    if (!driverExists) {
                        Driver driver = new Driver();
                        create_driver_db(connection, driver, users_pkey_driver);//dummy driver for multithreads
                    }
                    if (!passengerExists) {
                        Passenger passenger = new Passenger(); //dummy driver for multithreads
                        create_passenger_db(connection, passenger, users_pkey_pas);
                    }

                    preparedStatement =
                            connection.prepareStatement("INSERT INTO public.\"Trip\"(\n" +
                                    "trips_pkey, driver_pkey, passenger_pkey, address_from, address_to, date_start, " +
                                    "date_change, price, status, estimate, report)\n" +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::\"Status\", ?, ?)");
                } else {
                    preparedStatement =
                            connection.prepareStatement("UPDATE public.\"Trip\"\n" +
                                    "\tSET trips_pkey=?, driver_pkey=?, passenger_pkey=?, address_from=?, address_to=?, date_start=?, " +
                                    "date_change=?, price=?, status=?::\"Status\", estimate=?, report=?\n" +
                                    "WHERE trips_pkey = ?");
                    preparedStatement.setLong(12, trip_pkey);
                }
                preparedStatement.setLong(1, trip_pkey);
                preparedStatement.setLong(2, trip.getDriverPkey().getUsersPkey().getUsersPkey());
                preparedStatement.setLong(3, trip.getPassengerPkey().getUsersPkey().getUsersPkey());
                preparedStatement.setString(4, trip.getAddressFrom());
                preparedStatement.setString(5, trip.getAddressTo());
                java.sql.Date sqlDate = new java.sql.Date(trip.getDateStart().toGregorianCalendar().getTimeInMillis());
                preparedStatement.setDate(6, sqlDate);
                sqlDate = new java.sql.Date(trip.getDateChange().toGregorianCalendar().getTimeInMillis());
                preparedStatement.setDate(7, sqlDate);
                preparedStatement.setInt(8, trip.getPrice());
                preparedStatement.setString(9, trip.getStatus().value());
                preparedStatement.setBoolean(10, trip.isEstimate());
                preparedStatement.setString(11, trip.getReport());
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

    private static void create_user_db(Connection connection, User user) throws SQLException {
        PreparedStatement preparedStatement;
        java.sql.Date curr_date = new java.sql.Date((new java.util.Date()).getTime());
        java.sql.Date sqlDate_lastLogin;
        java.sql.Date sqlDate_registrationDate;
        if (!check_exisits_of_User(connection, user.getUsersPkey())) {
            preparedStatement =
                    connection.prepareStatement("INSERT INTO PUBLIC.\"User\" " +
                            "(login, users_password, last_login, users_pkey, registration_date)" +
                            "VALUES (?,?,?,?,?)");
        } else {
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
        } else {
            sqlDate_lastLogin = new java.sql.Date(user.getLastLogin().toGregorianCalendar().getTimeInMillis());
        }
        preparedStatement.setDate(3, sqlDate_lastLogin);
        preparedStatement.setLong(4, user.getUsersPkey());

        if (user.getRegistrationDate() == null) {
            sqlDate_registrationDate = curr_date;
        } else {
            sqlDate_registrationDate = new java.sql.Date(user.getRegistrationDate().toGregorianCalendar().getTimeInMillis());
        }
        preparedStatement.setDate(5, sqlDate_registrationDate);
        preparedStatement.executeUpdate();
    }

    private static Passenger create_passenger_db(Connection connection, Passenger passenger, long users_pkey_pas) throws SQLException {
        PreparedStatement preparedStatement;
        java.sql.Date curr_date = new java.sql.Date((new java.util.Date()).getTime());
        java.sql.Date sqlDate_birth;
        if (users_pkey_pas == 0)
        {
            users_pkey_pas = passenger.getUsersPkey().getUsersPkey();
        }

        if (!check_exists_of_Passenger(connection, users_pkey_pas)) {
            boolean userExists = check_exisits_of_User(connection, users_pkey_pas);
            if (!userExists) {
                User user = new User(); //dummy user for multithreads
                user.setUsersPkey(users_pkey_pas);
                create_user_db(connection, user);
            }
            preparedStatement =
                    connection.prepareStatement("INSERT INTO PUBLIC.\"Passenger\" " +
                            "(users_pkey_pas, full_name, birth)" +
                            "VALUES (?,?,?)");
        } else {
            preparedStatement =
                    connection.prepareStatement("UPDATE PUBLIC.\"Passenger\" " +
                            "SET users_pkey_pas=?, full_name=?, birth=?" +
                            "WHERE users_pkey_pas = ?");
            preparedStatement.setLong(4, users_pkey_pas);
        }
        preparedStatement.setLong(1, users_pkey_pas);
        preparedStatement.setString(2, passenger.getFullName());
        if (passenger.getBirth() == null) {
            sqlDate_birth = curr_date;
        } else {
            sqlDate_birth = new java.sql.Date(passenger.getBirth().toGregorianCalendar().getTimeInMillis());
        }
        preparedStatement.setDate(3, sqlDate_birth);
        preparedStatement.executeUpdate();
        return passenger;
    }

    private static Driver create_driver_db(Connection connection, Driver driver, long users_pkey_driver) throws SQLException {
        PreparedStatement preparedStatement;
        java.sql.Date curr_date = new java.sql.Date((new java.util.Date()).getTime());
        java.sql.Date sqlDate_birth;
        if (users_pkey_driver == 0){
            users_pkey_driver = driver.getUsersPkey().getUsersPkey();
        }

        if (!check_exists_of_Driver(connection, users_pkey_driver)) {
            boolean userExists = check_exisits_of_User(connection, users_pkey_driver);
            if (!userExists) {
                User user = new User(); //dummy user for multithreads
                user.setUsersPkey(users_pkey_driver);
                create_user_db(connection, user);
            }
            preparedStatement =
                    connection.prepareStatement("INSERT INTO PUBLIC.\"Driver\" " +
                            "(users_pkey_driver, full_name, car_number, car_description, passport, birth)" +
                            "VALUES (?,?,?,?,?,?)");
        } else {
            preparedStatement =
                    connection.prepareStatement("UPDATE PUBLIC.\"Driver\" " +
                            "SET users_pkey_driver=?, full_name=?, car_number=?, car_description=?, passport=?, birth=?" +
                            "WHERE users_pkey_driver = ?");
            preparedStatement.setLong(7, users_pkey_driver);
        }
        preparedStatement.setLong(1, users_pkey_driver);
        preparedStatement.setString(2, driver.getFullName());
        preparedStatement.setString(3, driver.getCarNumber());
        preparedStatement.setString(4, driver.getCarDescription());
        preparedStatement.setString(5, driver.getPassport());
        if (driver.getBirth() == null) {
            sqlDate_birth = curr_date;
        } else {
            sqlDate_birth = new java.sql.Date(driver.getBirth().toGregorianCalendar().getTimeInMillis());
        }
        preparedStatement.setDate(6, sqlDate_birth);
        preparedStatement.executeUpdate();
        return driver;
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

    private static boolean check_exists_of_Driver(Connection connection, long users_pkey_driver) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT COUNT(*) count FROM PUBLIC.\"Driver\" " +
                        "WHERE users_pkey_driver = ?");
        preparedStatement.setLong(1, users_pkey_driver);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if (resultSet.getInt("count") == 0){
            return false;
        }
        else {
            return true;
        }
    }

    private static boolean check_exists_of_Passenger(Connection connection, long users_pkey_pas) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT COUNT(*) count FROM PUBLIC.\"Passenger\" " +
                        "WHERE users_pkey_pas = ?");
        preparedStatement.setLong(1, users_pkey_pas);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if (resultSet.getInt("count") == 0) {
            return false;
        } else {
            return true;
        }
    }

}
