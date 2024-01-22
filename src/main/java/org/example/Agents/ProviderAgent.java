package org.example.Agents;

import jade.core.Agent;
import org.example.ProviderBehs.ProviderStartBeh;

public class ProviderAgent extends Agent {

    protected void setup() {
        System.out.println(getLocalName() + " готов работать");
        this.addBehaviour(new ProviderStartBeh());
    }
}

