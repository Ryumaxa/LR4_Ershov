package org.example.Agents;

import jade.core.Agent;
import org.example.ProviderBehs.ProviderFSM;
import org.example.ProviderBehs.ProviderStartBeh;
import org.example.ProviderBehs.ReceiveRequestBeh;

public class ProviderAgent extends Agent {

    protected void setup() {
        System.out.println(getLocalName() + " готов работать");
//        this.addBehaviour(new ReceiveRequestBeh());
        this.addBehaviour(new ProviderStartBeh());
    }
}

