package org.example.ProducerBehs;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProducerStartBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("IsPowerAvailable"));
        if (RequestFromConsumer != null) {
            System.out.println("4    Производитель получает вопрос о наличии мощности от провайдера");
            FSMBehaviour fsmBehaviour = new ProducerFSM(RequestFromConsumer);
            fsmBehaviour.reset();
            getAgent().addBehaviour(fsmBehaviour);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
