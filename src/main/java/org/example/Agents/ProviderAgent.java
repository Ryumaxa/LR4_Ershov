package org.example.Agents;

import jade.core.Agent;

public class ProviderAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " готов работать");
    }
}

