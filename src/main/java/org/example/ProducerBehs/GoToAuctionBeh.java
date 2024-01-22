package org.example.ProducerBehs;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.ProducersFunctions;
import org.example.HelperClasses.TimeTracker;
import org.example.HelperClasses.TopicHelper;

public class GoToAuctionBeh extends OneShotBehaviour {
    AID topic;
    double minPrice;
    double currentPrice;
    boolean isImWinner = false;

    @Override
    public void onStart() {
        minPrice = ProducersFunctions.getPPminPrice(getAgent().getLocalName());
        currentPrice = minPrice * 2;

        // Подключаемся к топику при получении приглашения
        ACLMessage invite = null;
        while (invite == null) {
            invite = getAgent().receive(MessageTemplate.MatchConversationId("InviteToAuction"));
        }
        System.out.println(TimeTracker.getCurrentHour() +"..8    Производитель " + getAgent().getLocalName() + " получает название топика " + invite.getContent() + " от провайдера " + invite.getSender().getLocalName());
        try {
            topic = TopicHelper.register(getAgent(), invite.getContent());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        ACLMessage myFirstBid = new ACLMessage(ACLMessage.INFORM);
        myFirstBid.setConversationId("ProducerBid");
        myFirstBid.addReceiver(topic);
        myFirstBid.setContent(currentPrice + "");
        getAgent().send(myFirstBid);
        System.out.println(TimeTracker.getCurrentHour() +"..9    Производитель" + getAgent().getLocalName() + " отправляет стартовую цену " + myFirstBid.getContent() + " в топик: " + topic.getLocalName());
    }
    @Override
    public void action() {
        // Новые ставки на понижение до получения сообщения о завершении
        ACLMessage stopper = null;
        while (stopper == null) {
            ACLMessage otherBids = getAgent().receive(MessageTemplate.MatchConversationId("ProducerBid"));
            if (otherBids != null && !otherBids.getSender().equals(getAgent().getAID()) && Double.parseDouble(otherBids.getContent()) < currentPrice && Double.parseDouble(otherBids.getContent()) > minPrice) {
                System.out.println(TimeTracker.getCurrentHour() +"..10    Производитель" + getAgent().getLocalName() + " получает цены своих коллег ");
                currentPrice = Double.parseDouble(otherBids.getContent()) * (0.9 + Math.random() * 0.05);
                ACLMessage myBid = new ACLMessage(ACLMessage.INFORM);
                myBid.setConversationId("ProducerBid");
                myBid.addReceiver(topic);
                myBid.setContent(currentPrice + "");

                getAgent().send(myBid);
                System.out.println(TimeTracker.getCurrentHour() +"..11    Производитель" + getAgent().getLocalName() + " отправляет новую цену:" + myBid.getContent() + " в топик " + topic.getLocalName());
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            stopper = getAgent().receive(MessageTemplate.MatchConversationId("Stopper"));
        }
        //Если этот производитель победил в аукционе
        if (stopper.getContent().equals(getAgent().getLocalName())) {
            System.out.println(TimeTracker.getCurrentHour() +"..14    Производители получили стоппер");
            isImWinner = true;
        }
    }

    @Override
    public int onEnd() {
        return isImWinner ? 1 : 0;
    }
}
