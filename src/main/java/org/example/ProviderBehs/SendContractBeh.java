package org.example.ProviderBehs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;

public class SendContractBeh extends Behaviour {
    boolean isReady = false;
    boolean haveAccept = false;
    @Override
    public void onStart() {
        if (ProviderFSM.getWinnerName() != null) {
            // Отправка контракта победителю
            ACLMessage contractMessage = new ACLMessage(ACLMessage.INFORM);
            contractMessage.setConversationId("Contract");
            contractMessage.setContent("YouHaveNewContract");
            contractMessage.addReceiver(new AID(ProviderFSM.getWinnerName(), false));
            getAgent().send(contractMessage);
        }

    }

    @Override
    public void action() {
        ACLMessage LastWordOfProducer = getAgent().receive(MessageTemplate.MatchConversationId("ReactionForContract"));
        if (LastWordOfProducer != null) {
            isReady = true;
            if (LastWordOfProducer.getContent().equals("OK")) {
                haveAccept = true;
            }
        }
    }

    @Override
    public boolean done() {
        // Если получен ответ на контракт или превышено время ожидания
        return (isReady || TimeTracker.getMillsUntilNextHour() < 50);
    }

    @Override
    public int onEnd() {
        ACLMessage AnsToConsumer = new ACLMessage(ACLMessage.INFORM);
        AnsToConsumer.addReceiver(new AID(getAgent().getLocalName().replace("ProviderOf", "")));
        if (haveAccept) {
            AnsToConsumer.setContent("success;" + ProviderFSM.getPowerInfo() + ";" + ProviderFSM.getPriceInfo() + ";" + ProviderFSM.getWinnerName());
        } else {
            AnsToConsumer.setContent("fail");
        }
        return super.onEnd();
    }
}
