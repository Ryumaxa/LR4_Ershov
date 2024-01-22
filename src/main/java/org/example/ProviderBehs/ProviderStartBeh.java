package org.example.ProviderBehs;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;

public class ProviderStartBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("RequestFromConsumer"));
        if (RequestFromConsumer != null) {
            System.out.println(TimeTracker.getCurrentHour() +"..2    Провайдер" + getAgent().getLocalName() + " получает запрос от потребителя " + RequestFromConsumer.getSender().getLocalName());
            ParallelBehaviour parallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
            parallelBehaviour.addSubBehaviour(new ProviderStartBeh());
            parallelBehaviour.addSubBehaviour(new ProviderFSM(RequestFromConsumer));
            getAgent().addBehaviour(parallelBehaviour);
        }


    }

    @Override
    public boolean done() {
        return false;
    }
}
