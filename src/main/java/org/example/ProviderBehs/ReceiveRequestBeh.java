package org.example.ProviderBehs;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.DfHelper;
import org.example.HelperClasses.TimeTracker;
import org.example.ProducerBehs.ProducerStartBeh;
import org.example.ProducerBehs.ReservedPowerResetBeh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReceiveRequestBeh extends OneShotBehaviour {
    boolean isInvitesSend = false;
    private boolean isLastTry = false;
    private ACLMessage RequestFromConsumer;
    private String oldTopicName;
    private String newTopicName = null;

    public ReceiveRequestBeh(boolean isLastTry, ACLMessage requestFromConsumer, String newTopicName) {
        this.isLastTry = isLastTry;
        RequestFromConsumer = requestFromConsumer;
        this.newTopicName = newTopicName;
    }


    public ReceiveRequestBeh(ACLMessage requestFromConsumer) {
        RequestFromConsumer = requestFromConsumer;
    }
    private FSMBehaviour divisionBeh;
    @Override
    public void action() {

        // Сохраняем информацию о величине закупаемой мощности
        String content = RequestFromConsumer.getContent();
        String[] values = content.split(";");
        double RequiredPower = Double.parseDouble(values[0]);


        //ProviderFSM.setPowerInfo(RequiredPower);
        // Агент записал информацию о требуемой мощности
        HashMap<String, Object> myHashMap = (HashMap<String, Object>) getAgent().getArguments()[0];
        myHashMap.put("power", RequiredPower);
        getAgent().setArguments(new Object[]{myHashMap});

        // Опрос производителей на тему доступной мощности
        List<AID> Found_Producers = DfHelper.search(getAgent(), "Produce");
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setConversationId("IsPowerAvailable");
        message.setContent(content);
        for (AID aid : Found_Producers) {
            message.addReceiver(new AID(aid.getLocalName(), false));
        }
        getAgent().send(message);
        System.out.println(TimeTracker.getCurrentHour() +"..3    Провайдер" + getAgent().getLocalName() + " опрашивает производителей об их доступной мощности " + Found_Producers);

        // Ожидание ответов от производителей с информацией о возможности их участия в аукционе
        // Производителей с доступной мощностью заносим в лист
        ArrayList<String> TheyHavePower= new ArrayList<>();
        int counter = 0;
        while (counter < 3) {
            ACLMessage AnsFromProducer = getAgent().receive(MessageTemplate.MatchConversationId("AnsFromProducer"));
            if (AnsFromProducer != null) {
                System.out.println(TimeTracker.getCurrentHour() +"..6    Провайдер " + getAgent().getLocalName() + " получает ответ от производителя " + AnsFromProducer.getSender().getLocalName() + " по поводу его доступной мощности ");
                counter++;
                String ans = AnsFromProducer.getContent();
                if (ans.equals("HavePower")) {
                    TheyHavePower.add(AnsFromProducer.getSender().getLocalName());
                }
            }
        }
        System.out.println(TheyHavePower);

        //ProviderFSM.setAuctionParticipants(TheyHavePower); // Для передачи списка участников дальше по поведениям
        // Агент записал информацию об участниках
        myHashMap.put("participants", TheyHavePower);
        getAgent().setArguments(new Object[]{myHashMap});

        //Если нашлись агенты с доступной мощностью, отправляем им приглашение, содержащее имя топика
        if (!TheyHavePower.isEmpty()) {
            ACLMessage invite = new ACLMessage(ACLMessage.INFORM);
            invite.setConversationId("InviteToAuction");
            invite.setContent("Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
            oldTopicName = invite.getContent();
            if (newTopicName != null) {
                invite.setContent(newTopicName);
            }







            for (String s : TheyHavePower) {
                invite.addReceiver(new AID(s, false));
            }
            getAgent().send(invite);
            System.out.println(TimeTracker.getCurrentHour() +"..7    Провайдер " + getAgent().getLocalName() + " отправляет ПОДХОДЯЩИМ производителям" + TheyHavePower + " название топика для участия в аукционе " + invite.getContent());
            isInvitesSend = true;
        }
    }

    // Если нашлись получатели для приглашения на аукцион, возвращаем 1
    public int onEnd() {

        if (!isInvitesSend && !isLastTry) {
            System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
            // Деление мощности на 3 и запуск 3 поведений с делением
            String content = RequestFromConsumer.getContent();
            String[] values = content.split(";");
            double RequiredPower = Double.parseDouble(values[0]);
            RequestFromConsumer.setContent((RequiredPower/3) + "");

            for (int i = 1; i < 4; i++) {
                if (divisionBeh != null) {
                    getAgent().removeBehaviour(divisionBeh);
                }

                divisionBeh = new DivisionFSM(RequestFromConsumer, ("Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour()+"___"+i));
                getAgent().addBehaviour(divisionBeh);

            }



//            ACLMessage AnsToConsumer = new ACLMessage(ACLMessage.INFORM);
//            AnsToConsumer.setConversationId("AuctionResults");
//            AnsToConsumer.addReceiver(new AID(getAgent().getLocalName().replace("ProviderOf", ""), false));
//            AnsToConsumer.setContent("fail");
//            getAgent().send(AnsToConsumer);
//            System.out.println(TimeTracker.getCurrentHour() +"..19    Провайдер отправляет отчет потребителю " + "fail");
        }

        return isInvitesSend ? 1 : 0;
    }
}


