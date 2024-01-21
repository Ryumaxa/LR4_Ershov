package org.example.ProducerBehs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;

public class GetContractBeh extends Behaviour {
    @Override
    public void action() {
        ACLMessage messageContract = getAgent().receive(MessageTemplate.MatchConversationId("Contract"));
        if (messageContract != null) {
            System.out.println(TimeTracker.getCurrentHour() +"..16    Производитель получает контракт");
            ACLMessage ansToContract = new ACLMessage(ACLMessage.INFORM);
            ansToContract.setConversationId("ReactionForContract");
            // С вероятностью 95% контракт будет принят
            if (Math.random() <= 0.95) {
                ansToContract.setContent("OK");
                double powReserve1 = (Double) getAgent().getArguments()[0];
                double powReserve2 = powReserve1 + Double.parseDouble(messageContract.getContent());
                getAgent().setArguments(new Object[]{powReserve2});
            } else {
                ansToContract.setContent("NO");
            }
            ansToContract.addReceiver(new AID(messageContract.getSender().getLocalName(), false));
            getAgent().send(ansToContract);
            System.out.println(TimeTracker.getCurrentHour() +"..17    Производитель отвечает на контракт и новый резерв по мощности " + (Double) getAgent().getArguments()[0]);
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
