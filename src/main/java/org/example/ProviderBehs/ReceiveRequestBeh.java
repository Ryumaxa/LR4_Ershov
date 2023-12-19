package org.example.ProviderBehs;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.DfHelper;
import org.example.HelperClasses.TimeTracker;

import java.util.ArrayList;
import java.util.List;

public class ReceiveRequestBeh extends Behaviour {
    boolean isInvitesSend = false;
    boolean isDone = false;
    @Override
    public void action() {
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("RequestFromConsumer"));
        // Получение запроса от потребителя
//        ACLMessage RequestFromConsumer = null;
//        while (RequestFromConsumer == null) {
//            RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("RequestFromConsumer"));
//        }
        if (RequestFromConsumer != null) {
            String content = RequestFromConsumer.getContent();

            // Опрос производителей на тему доступной мощности
            List<AID> Found_Producers = DfHelper.search(getAgent(), "Produce");
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setConversationId("IsPowerAvailable");
            message.setContent(content);
            for (AID aid : Found_Producers) {
                message.addReceiver(new AID(aid.getLocalName(), false));
            }
            getAgent().send(message);

            // Ожидание ответов от производителей с информацией о возможности их участия в аукционе
            // Производителей с доступной мощностью заносим в лист
            ArrayList<String> TheyHavePower= new ArrayList<>();
            for (AID aid : Found_Producers) {
                ACLMessage AnsFromProducer = getAgent().receive(MessageTemplate.MatchConversationId("AnsFromProducer"));
                if (AnsFromProducer != null) {
                    String ans = AnsFromProducer.getContent();
                    if (ans.equals("HavePower")) {
                        TheyHavePower.add(AnsFromProducer.getSender().getLocalName());
                    }
                }
            }

            //Если нашлись агенты с доступной мощностью, отправляем им приглашение, содержащее имя топика
            if (!TheyHavePower.isEmpty()) {
                ACLMessage invite = new ACLMessage(ACLMessage.INFORM);
                invite.setConversationId("InviteToAuction");
                invite.setContent("Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
                for (String s : TheyHavePower) {
                    invite.addReceiver(new AID(s, false));
                    System.out.println(s);
                }
                getAgent().send(invite);
                isInvitesSend = true;
            }
            isDone = true;
        }
    }

    @Override
    public boolean done() {
        return false;
    }

    // Если нашлись получатели для приглашения на аукцион, возвращаем 1
    public int onEnd() {
        return isInvitesSend ? 1 : 0;
    }
}


