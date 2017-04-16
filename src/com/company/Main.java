package com.company;

import DataManager.DataHandler;

import javax.xml.crypto.Data;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        //generate_test_passwords();
        DataHandler.data_to_XML();
        DataHandler.data_to_DB();
    }

    private static void generate_test_passwords() {
        try {
            GregorianCalendar date = new GregorianCalendar(1920, 11, 28);
            XMLGregorianCalendar dateGregor = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
            System.out.println(DataHandler.code_Password("qwerty123", dateGregor)); //0A. Grigoriev
            System.out.println(DataHandler.code_Password("lalala76", dateGregor));//Romashka1910
            System.out.println(DataHandler.code_Password("trulala0", dateGregor));//Katusha
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
    }
}
