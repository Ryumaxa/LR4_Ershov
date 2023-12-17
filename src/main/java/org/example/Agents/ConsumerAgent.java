package org.example.Agents;

import jade.core.Agent;
import org.example.ConsumerBehs.ConsTestBeh;
import org.example.ConsumerBehs.ConsumerXmlParser;
import org.example.ConsumerBehs.SendRequestBeh;

public class ConsumerAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " готов работать");
        this.addBehaviour(new ConsumerXmlParser());
        this.addBehaviour(new ConsTestBeh());
        this.addBehaviour(new SendRequestBeh());
    }

    // отправляет задание агенту-поставщику на покупку мощности
    // получает отчет о результате произведения закупки от агента-поставщика
}
