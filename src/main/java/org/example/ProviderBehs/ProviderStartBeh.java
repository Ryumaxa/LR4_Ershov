package org.example.ProviderBehs;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProviderStartBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("RequestFromConsumer"));
        if (RequestFromConsumer != null) {
            System.out.println("2    Провайдер получает запрос от потребителя");
            FSMBehaviour fsmBehaviour = new ProviderFSM(RequestFromConsumer);
//            fsmBehaviour.reset();
            getAgent().addBehaviour(fsmBehaviour);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
