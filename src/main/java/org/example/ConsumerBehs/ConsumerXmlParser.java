package org.example.ConsumerBehs;

import jade.core.behaviours.OneShotBehaviour;
import org.example.HelperClasses.PowerConsumptionAllData;
import org.example.HelperClasses.PowerConsumptionData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.HashMap;

public class ConsumerXmlParser extends OneShotBehaviour {

    @Override
    public void action() {
        HashMap<Integer, Double> hashMap = new HashMap<>();

        try {
            File file = new File("C:\\Users\\Roman\\IdeaProjects\\LR4_Ershov\\src\\main\\java\\org\\example\\Data\\" + getAgent().getLocalName() + ".xml");

            // Создание объекта Unmarshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(PowerConsumptionAllData.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Распарсинг XML-файла
            PowerConsumptionAllData powerData = (PowerConsumptionAllData) jaxbUnmarshaller.unmarshal(file);

            for (PowerConsumptionData data : powerData.getConsumptionDataList()) {
                hashMap.put(data.getHour(), data.getPower() * 14.6 / 100);
            }

            // Заполнение HashMap значениями из XML-файла
//            hashMap.put(powerData.getHour(), powerData.getPower());

            Object[] argument = {hashMap};
            getAgent().setArguments(argument);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
