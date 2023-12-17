package org.example.ProviderBehs;
import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.DfHelper;
import org.example.HelperClasses.TimeTracker;
import org.example.HelperClasses.TopicHelper;

import java.util.ArrayList;
import java.util.List;

public class ReceiveRequestBeh extends Behaviour {

    @Override
    public void action() {
        // Получение запроса от потребителя
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("RequestFromConsumer"));
        if (RequestFromConsumer != null) {
            String content = RequestFromConsumer.getContent();

            // Опрос производителей на тему доступной мощности
            List<AID> Found_Producers = DfHelper.search(getAgent(), "Produce");
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setConversationId("Invite");
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

            // Если нашлись агенты с доступной мощностью, открываем для них и для себя новый топик
//            if (!TheyHavePower.isEmpty()) {
//                try {
//                    // Создание топика и подключение к нему поставщика
//                    AID topic = TopicHelper.register(getAgent(), "Topic_of_" + getAgent().getLocalName() + "_hour_" + TimeTracker.getCurrentHour());
//                    TopicManagementHelper topicHelper = (TopicManagementHelper) getAgent().getHelper(TopicManagementHelper.SERVICE_NAME);
//                    topicHelper.register(topic);
//                    // Отправка имени топика участникам аукциона
////                    for (String s : TheyHavePower) {
////                        topicHelper.register(topic);
////                    }
//                } catch (ServiceException e) {
//                    throw new RuntimeException(e);
//                }
//
//
//            }




        }
    }

    @Override
    public boolean done() {
        return false;
    }
}


