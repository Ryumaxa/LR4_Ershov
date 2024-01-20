package org.example.ProviderBehs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;

public class SendContractBeh extends Behaviour {
    boolean isReady = false;
    boolean haveAccept = false;
    long time1;
    @Override
    public void onStart() {
        time1 = TimeTracker.getMillsUntilNextHour();
        if (ProviderFSM.getWinnerName() != null) {
            // Отправка контракта победителю
            ACLMessage contractMessage = new ACLMessage(ACLMessage.INFORM);
            contractMessage.setConversationId("Contract");
            contractMessage.setContent("YouHaveNewContract");
            contractMessage.addReceiver(new AID(ProviderFSM.getWinnerName(), false));
            getAgent().send(contractMessage);
            System.out.println(TimeTracker.getCurrentHour() +".15    Провайдер" + getAgent().getLocalName() + " отправляет контракт победителю " + ProviderFSM.getWinnerName());
        }

    }

    @Override
    public void action() {
        ACLMessage LastWordOfProducer = getAgent().receive(MessageTemplate.MatchConversationId("ReactionForContract"));
        if (LastWordOfProducer != null) {
            System.out.println(TimeTracker.getCurrentHour() +".18    Провайдер получает ответ на контракт от производителя");
            isReady = true;
            if (LastWordOfProducer.getContent().equals("OK")) {
                haveAccept = true;
            }
        }
    }

    @Override
    public boolean done() {
        // Если получен ответ на контракт или превышено время ожидания
        return (isReady || time1 - TimeTracker.getMillsUntilNextHour() >= 100);
    }

    @Override
    public int onEnd() {
        ACLMessage AnsToConsumer = new ACLMessage(ACLMessage.INFORM);
        AnsToConsumer.setConversationId("AuctionResults");
        AnsToConsumer.addReceiver(new AID(getAgent().getLocalName().replace("ProviderOf", ""), false));
        if (haveAccept) {
            AnsToConsumer.setContent("success;" + ProviderFSM.getPowerInfo() + ";" + ProviderFSM.getPriceInfo() + ";" + ProviderFSM.getWinnerName());
            System.out.println(AnsToConsumer.getContent());
        } else {
            AnsToConsumer.setContent("fail");
        }
        getAgent().send(AnsToConsumer);
        System.out.println(TimeTracker.getCurrentHour() +".19    Провайдер отправляет отчет потребителю " + AnsToConsumer.getContent());
        return super.onEnd();
    }
}
