package org.example.Agents;

import jade.core.Agent;
import org.example.ConsumerBehs.ConsumerFSM;
import org.example.ConsumerBehs.ConsumerXmlParser;

public class ConsumerAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " готов работать");
        this.addBehaviour(new ConsumerXmlParser());
        this.addBehaviour(new ConsumerFSM());
    }
}
