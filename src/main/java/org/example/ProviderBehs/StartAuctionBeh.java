package org.example.ProviderBehs;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;
import org.example.HelperClasses.TopicHelper;

import java.util.List;

public class StartAuctionBeh extends Behaviour {
    boolean winnerHasFound = false;
    double bestPrice = 100;
    String winnerName;
    AID topic;
    @Override
    public void onStart() {
        System.out.println("Поведение StartAuctionBeh запускается" + TimeTracker.getCurrentHour() + getAgent().getLocalName());
        // Тут создается топик с соответствующим именем
        try {
            topic = TopicHelper.register(getAgent(), "Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
            System.out.println("Создан топик с именем " + topic.getLocalName());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void action() {
        // Получение ставок и перезапись лучшей ставки и потенциального победителя
        ACLMessage mesFromTopic = getAgent().receive(MessageTemplate.MatchTopic(topic));
        if (mesFromTopic != null) {
            if (Double.parseDouble(mesFromTopic.getContent()) < bestPrice) {
                bestPrice = Double.parseDouble(mesFromTopic.getContent());
                winnerName = mesFromTopic.getSender().getLocalName();
            }
        }
        if (TimeTracker.getMillsUntilNextHour() < 500) { // Завершение аукциона по таймеру
            ACLMessage stopper = new ACLMessage(ACLMessage.INFORM);
            stopper.setConversationId("Stopper");
            stopper.setContent(winnerName);
            System.out.println("Победитель = " + winnerName);
            ProviderFSM.setWinnerName(winnerName);
            List<String> Participants = ProviderFSM.getAuctionParticipants();
            for (String agentName : Participants) {
                stopper.addReceiver(new AID(agentName, false));
            }
            getAgent().send(stopper);
            winnerHasFound = true;
        }
    }

    // Когда победитель будет найден
    @Override
    public boolean done() {
        return winnerHasFound;
    }

    @Override
    public int onEnd() {
        ProviderFSM.setPriceInfo(bestPrice);
        return super.onEnd();
    }
}
