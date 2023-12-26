package org.example.ProviderBehs;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.DfHelper;
import org.example.HelperClasses.TimeTracker;

import java.util.ArrayList;
import java.util.List;

public class ReceiveRequestBeh extends OneShotBehaviour {
    boolean isInvitesSend = false;
    private ACLMessage RequestFromConsumer;
    public ReceiveRequestBeh(ACLMessage requestFromConsumer) {
        RequestFromConsumer = requestFromConsumer;
    }

    @Override
    public void action() {

        // Сохраняем информацию о величине закупаемой мощности
        String content = RequestFromConsumer.getContent();
        String[] values = content.split(";");
        double RequiredPower = Double.parseDouble(values[0]);
        ProviderFSM.setPowerInfo(RequiredPower);

        // Опрос производителей на тему доступной мощности
        List<AID> Found_Producers = DfHelper.search(getAgent(), "Produce");
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setConversationId("IsPowerAvailable");
        message.setContent(content);
        for (AID aid : Found_Producers) {
            message.addReceiver(new AID(aid.getLocalName(), false));
        }
        getAgent().send(message);
        System.out.println(TimeTracker.getCurrentHour() +".3    Провайдер" + getAgent().getLocalName() + " опрашивает производителей об их доступной мощности " + Found_Producers);

        // Ожидание ответов от производителей с информацией о возможности их участия в аукционе
        // Производителей с доступной мощностью заносим в лист
        ArrayList<String> TheyHavePower= new ArrayList<>();
        int counter = 0;
        while (counter < 3) {
            ACLMessage AnsFromProducer = getAgent().receive(MessageTemplate.MatchConversationId("AnsFromProducer"));
            if (AnsFromProducer != null) {
                System.out.println(TimeTracker.getCurrentHour() +".6    Провайдер " + getAgent().getLocalName() + " получает ответ от производителя " + AnsFromProducer.getSender().getLocalName() + " по поводу его доступной мощности ");
                counter++;
                String ans = AnsFromProducer.getContent();
                if (ans.equals("HavePower")) {
                    TheyHavePower.add(AnsFromProducer.getSender().getLocalName());
                }
            }
        }
        System.out.println(TheyHavePower);
        ProviderFSM.setAuctionParticipants(TheyHavePower); // Для передачи списка участников дальше по поведениям
        //Если нашлись агенты с доступной мощностью, отправляем им приглашение, содержащее имя топика
        if (!TheyHavePower.isEmpty()) {
            ACLMessage invite = new ACLMessage(ACLMessage.INFORM);
            invite.setConversationId("InviteToAuction");
            invite.setContent("Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
            for (String s : TheyHavePower) {
                invite.addReceiver(new AID(s, false));
            }
            getAgent().send(invite);
            System.out.println(TimeTracker.getCurrentHour() +".7    Провайдер " + getAgent().getLocalName() + " отправляет ПОДХОДЯЩИМ производителям" + TheyHavePower + " название топика для участия в аукционе " + invite.getContent());
            isInvitesSend = true;
        }
    }

    // Если нашлись получатели для приглашения на аукцион, возвращаем 1
    public int onEnd() {
        return isInvitesSend ? 1 : 0;
    }
}


