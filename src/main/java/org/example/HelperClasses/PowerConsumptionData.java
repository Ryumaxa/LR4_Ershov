package org.example.HelperClasses;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class PowerConsumptionData {
    @XmlAttribute(name = "hour")
    private int hour;

    @XmlAttribute(name = "power")
    private double power;
}
