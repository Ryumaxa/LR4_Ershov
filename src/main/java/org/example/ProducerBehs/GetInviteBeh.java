package org.example.ProducerBehs;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GetInviteBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("Invite"));
        if (RequestFromConsumer != null) {
            String content = RequestFromConsumer.getContent();
            String[] values = content.split(";");
            double RequiredPower = Double.parseDouble(values[0]);
            double maxPrice = Double.parseDouble(values[1]);

            System.out.println(getAgent().getLocalName() + " принял " + RequiredPower +  ";" + maxPrice);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
