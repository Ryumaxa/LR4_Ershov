package org.example.Agents;

import jade.core.Agent;
import org.example.ConsumerBehs.ConsumerCyclicBeh;
import org.example.ConsumerBehs.ConsumerFSM;
import org.example.ConsumerBehs.ConsumerXmlParser;
import org.example.HelperClasses.TimeTracker;

public class ConsumerAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " готов работать");
        this.addBehaviour(new ConsumerXmlParser());
        this.addBehaviour(new ConsumerFSM());
        this.addBehaviour(new ConsumerCyclicBeh(TimeTracker.getCurrentHour()));
    }
}
