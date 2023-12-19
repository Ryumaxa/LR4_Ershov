package org.example.ProducerBehs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.ProducersFunctions;
import org.example.HelperClasses.TimeTracker;

public class GetInviteBeh extends Behaviour {
    private boolean isHavePower = false;
    private boolean isAnswerSend = false;
    @Override
    public void action() {
        // Получение данных от поставщика для определения возможности участия в аукционе с такими условиями
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("IsPowerAvailable"));
        if (RequestFromConsumer != null) {
            String content = RequestFromConsumer.getContent();
            String[] values = content.split(";");
            double RequiredPower = Double.parseDouble(values[0]);
//            double maxPrice = Double.parseDouble(values[1]);

            // Проверка доступности мощности для участия в аукционе и соответствующий ответ поставщику
            ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
            answer.setConversationId("AnsFromProducer");
            if (ProducersFunctions.ProducerFunc(getAgent().getLocalName()) >= RequiredPower) {
                System.out.println(getAgent().getLocalName() + " считает, что " + ProducersFunctions.ProducerFunc(getAgent().getLocalName()) + " > " + RequiredPower);
                answer.setContent("HavePower");
                isHavePower = true;
            } else {
                answer.setContent("NoPower");
            }
            answer.addReceiver(new AID(RequestFromConsumer.getSender().getLocalName(), false));
            getAgent().send(answer);
            isAnswerSend = true;
            System.out.println(getAgent().getLocalName() + " принял запрос на " + RequiredPower + " в " + TimeTracker.getCurrentHour() + " часу, имея мощность = " + ProducersFunctions.ProducerFunc(getAgent().getLocalName()) + " и сказал " + answer.getContent());
        }
    }

    @Override
    public boolean done() {
        return isAnswerSend;
    }

    @Override
    public int onEnd() {
        return isHavePower ? 1 : 0;
    }
}
