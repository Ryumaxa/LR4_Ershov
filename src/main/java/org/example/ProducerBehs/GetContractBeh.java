package org.example.ProducerBehs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GetContractBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage messageContract = getAgent().receive(MessageTemplate.MatchConversationId("Contract"));
        if (messageContract != null) {
            ACLMessage ansToContract = new ACLMessage(ACLMessage.INFORM);
            ansToContract.setConversationId("ReactionForContract");
            // С вероятностью 95% контракт будет принят
            if (Math.random() < 0.95) {
                ansToContract.setContent("OK");
            } else {
                ansToContract.setContent("NO");
            }
            ansToContract.addReceiver(new AID(messageContract.getSender().getLocalName(), false));
            getAgent().send(ansToContract);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
