package org.example.ProducerBehs;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;

public class ProducerStartBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("IsPowerAvailable"));
        if (RequestFromConsumer != null) {
            System.out.println(TimeTracker.getCurrentHour() +".4    Производитель" + getAgent().getLocalName() + " получает вопрос о наличии мощности от провайдера " + RequestFromConsumer.getSender().getLocalName());
//            FSMBehaviour fsmBehaviour = new ProducerFSM(RequestFromConsumer);
//            fsmBehaviour.reset();
//            getAgent().addBehaviour(fsmBehaviour);
            ParallelBehaviour parallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
            parallelBehaviour.addSubBehaviour(new ProducerStartBeh());
            parallelBehaviour.addSubBehaviour(new ProducerFSM(RequestFromConsumer));
            getAgent().addBehaviour(parallelBehaviour);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
