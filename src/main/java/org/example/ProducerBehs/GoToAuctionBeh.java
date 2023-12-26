package org.example.ProducerBehs;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.ProducersFunctions;
import org.example.HelperClasses.TopicHelper;

public class GoToAuctionBeh extends OneShotBehaviour {
    AID topic;
    double minPrice; //= ProducersFunctions.getPPminPrice(getAgent().getLocalName());
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
        System.out.println("8    Производитель получает название топика от провайдера");
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
        System.out.println("9    Производитель отправляет стартовую цену в топик:" + myFirstBid.getContent());
    }
    @Override
    public void action() {
        // Новые ставки на понижение до получения сообщения о завершении
        ACLMessage stopper = null;
        while (stopper == null) {
            ACLMessage otherBids = getAgent().receive(MessageTemplate.MatchConversationId("ProducerBid"));
            if (otherBids != null && !otherBids.getSender().equals(getAgent().getAID()) && Double.parseDouble(otherBids.getContent()) < currentPrice && Double.parseDouble(otherBids.getContent()) > minPrice) {
                System.out.println("10    Производитель получает цены своих коллег");
                currentPrice = Double.parseDouble(otherBids.getContent()) * (0.9 + Math.random() * 0.05);
                ACLMessage myBid = new ACLMessage(ACLMessage.INFORM);
                myBid.setConversationId("ProducerBid");
                myBid.addReceiver(topic);
                myBid.setContent(currentPrice + "");

                getAgent().send(myBid);
                System.out.println("11    Производитель отправляет новую цену:" + myBid.getContent());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            stopper = getAgent().receive(MessageTemplate.MatchConversationId("Stopper"));
        }
        //Если этот производитель победил в аукционе
        if (stopper.getContent() != null && stopper.getContent().equals(getAgent().getName())) {
            System.out.println("14    Производители получили стоппер");
            isImWinner = true;
        }
    }

    @Override
    public int onEnd() {
        return isImWinner ? 1 : 0;
    }
}
