package org.example.Agents;

import jade.core.Agent;
import org.example.HelperClasses.DfHelper;
import org.example.ProducerBehs.GetInviteBeh;

public class ProducerAgent extends Agent {
    protected void setup() {
        DfHelper.register(this, "Produce");
        System.out.println(getLocalName() + " готов вырабатывать");
        this.addBehaviour(new GetInviteBeh());
    }
}