//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.04.15 at 02:14:42 PM MSK 
//


package taxi_by_jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Driver complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Driver">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="users_pkey" type="{}User"/>
 *         &lt;element name="full_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="car_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="car_description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="passport" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birth" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Driver", propOrder = {
    "usersPkey_driver",
    "fullName",
    "carNumber",
    "carDescription",
    "passport",
    "birth"
})
@XmlRootElement
public class Driver {

    @XmlElement(name = "users_pkey_driver", required = true)
    protected User usersPkey_driver;
    @XmlElement(name = "full_name", required = true)
    protected String fullName;
    @XmlElement(name = "car_number", required = true)
    protected String carNumber;
    @XmlElement(name = "car_description", required = true)
    protected String carDescription;
    @XmlElement(required = true)
    protected String passport;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar birth;

    /**
     * Gets the value of the usersPkey property.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getUsersPkey() {
        return usersPkey_driver;
    }

    /**
     * Sets the value of the usersPkey property.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setUsersPkey(User value) {
        this.usersPkey_driver = value;
    }

    /**
     * Gets the value of the fullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullName(String value) {
        this.fullName = value;
    }

    /**
     * Gets the value of the carNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarNumber() {
        return carNumber;
    }

    /**
     * Sets the value of the carNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarNumber(String value) {
        this.carNumber = value;
    }

    /**
     * Gets the value of the carDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarDescription() {
        return carDescription;
    }

    /**
     * Sets the value of the carDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarDescription(String value) {
        this.carDescription = value;
    }

    /**
     * Gets the value of the passport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassport() {
        return passport;
    }

    /**
     * Sets the value of the passport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassport(String value) {
        this.passport = value;
    }

    /**
     * Gets the value of the birth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirth() {
        return birth;
    }

    /**
     * Sets the value of the birth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirth(XMLGregorianCalendar value) {
        this.birth = value;
    }

}
