package org.example.ProviderBehs;

import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.DfHelper;
import org.example.HelperClasses.TimeTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiveRequestBeh extends OneShotBehaviour {
    boolean isInvitesSend = false;
    private boolean isLastTry = false;
    private ACLMessage RequestFromConsumer;
    public ReceiveRequestBeh(boolean isLastTry, ACLMessage requestFromConsumer) {
        this.isLastTry = isLastTry;
        RequestFromConsumer = requestFromConsumer;
    }

    public ReceiveRequestBeh(ACLMessage requestFromConsumer) {
        RequestFromConsumer = requestFromConsumer;
    }

    @Override
    public void action() {

        // Сохраняем информацию о величине закупаемой мощности
        String content = RequestFromConsumer.getContent();
        String[] values = content.split(";");
        double RequiredPower = Double.parseDouble(values[0]);

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

        // Агент записал информацию об участниках
        myHashMap.put("participants", TheyHavePower);
        getAgent().setArguments(new Object[]{myHashMap});

        //Если нашлись агенты с доступной мощностью, отправляем им приглашение, содержащее имя топика
        if (!TheyHavePower.isEmpty()) {
            ACLMessage invite = new ACLMessage(ACLMessage.INFORM);
            invite.setConversationId("InviteToAuction");
            invite.setContent("Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
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
            // Деление мощности на 3 и запуск 3 поведений с делением
            String content = RequestFromConsumer.getContent();
            String[] values = content.split(";");
            double RequiredPower = Double.parseDouble(values[0]);
            RequestFromConsumer.setContent((RequiredPower/3) + "");
            getAgent().addBehaviour(new DivisionStarter(RequestFromConsumer));
        }

        return isInvitesSend ? 1 : 0;
    }
}


