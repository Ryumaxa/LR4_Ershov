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
    String winnerName = "";
    AID topic;
    long time1;
    @Override
    public void onStart() {
        time1 = TimeTracker.getMillsUntilNextHour();
        // Тут создается топик с соответствующим именем
        try {
            topic = TopicHelper.register(getAgent(), "Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void action() {
        // Получение ставок и перезапись лучшей ставки и потенциального победителя
        ACLMessage mesFromTopic = getAgent().receive(MessageTemplate.MatchTopic(topic));
        if (mesFromTopic != null) {
            System.out.println(TimeTracker.getCurrentHour() +".12    Провайдер" + getAgent().getLocalName() + " получает цены производителей ");
            if (Double.parseDouble(mesFromTopic.getContent()) < bestPrice) {
                bestPrice = Double.parseDouble(mesFromTopic.getContent());
                winnerName = mesFromTopic.getSender().getLocalName();
                System.out.println(winnerName);
            }
        }
        if (time1 - TimeTracker.getMillsUntilNextHour() >= 1000) { // Завершение аукциона по таймеру
            ACLMessage stopper = new ACLMessage(ACLMessage.INFORM);
            stopper.setConversationId("Stopper");
            stopper.setContent(winnerName);
            ProviderFSM.setWinnerName(winnerName);
            List<String> Participants = ProviderFSM.getAuctionParticipants();
            System.out.println("PARTICIPANTS" + Participants);

            for (String agentName : Participants) {
                stopper.addReceiver(new AID(agentName, false));
            }

            getAgent().send(stopper);


            System.out.println(TimeTracker.getCurrentHour() +".13    Провайдер" + getAgent().getLocalName() + " останавливает аукцион по таймеру и отправляет производителям стоппер - победил " + winnerName);
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
        ProviderFSM.setWinnerName(winnerName);
        return super.onEnd();
    }
}
