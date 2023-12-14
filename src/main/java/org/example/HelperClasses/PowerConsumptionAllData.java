package org.example.HelperClasses;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

@Data
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class PowerConsumptionAllData {
    @XmlElementWrapper(name = "PowerAllData")
    @XmlElement(name = "PowerData")
    private ArrayList<PowerConsumptionData> ConsumptionDataList;
}
