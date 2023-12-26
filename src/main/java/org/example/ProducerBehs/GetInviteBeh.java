package org.example.ProducerBehs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import org.example.HelperClasses.ProducersFunctions;
import org.example.HelperClasses.TimeTracker;

public class GetInviteBeh extends Behaviour {
    private ACLMessage RequestFromCons;

    public GetInviteBeh(ACLMessage requestFromCons) {
        RequestFromCons = requestFromCons;
    }

    private boolean isHavePower = false;
//    private boolean isAnswerSend = false;
    private int counter1 = 0;
    @Override
    public void action() {
        // Получение данных от поставщика для определения возможности участия в аукционе с такими условиями
//        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("IsPowerAvailable"));
//        if (RequestFromConsumer != null) {
        String content = RequestFromCons.getContent();
        String[] values = content.split(";");
        double RequiredPower = Double.parseDouble(values[0]);

            // Проверка доступности мощности для участия в аукционе и соответствующий ответ поставщику
        ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
        answer.setConversationId("AnsFromProducer");
        if (ProducersFunctions.ProducerFunc(getAgent().getLocalName()) >= RequiredPower) {
            answer.setContent("HavePower");
            isHavePower = true;
        } else {
            answer.setContent("NoPower");
        }
        answer.addReceiver(new AID(RequestFromCons.getSender().getLocalName(), false));
        getAgent().send(answer);
        System.out.println(TimeTracker.getCurrentHour() +".5    Производитель" + getAgent().getLocalName() + " отвечает провайдеру " + RequestFromCons.getSender().getLocalName() + " по поводу наличия у него мощности: " + answer.getContent() + " полагая, что " + ProducersFunctions.ProducerFunc(getAgent().getLocalName()) + " > " + RequiredPower);
//            isAnswerSend = true;
//        counter1++;
        counter1 = 5;
//        System.out.println(getAgent().getLocalName() + " принял запрос на " + RequiredPower + " в " + TimeTracker.getCurrentHour() + " часу, имея мощность = " + ProducersFunctions.ProducerFunc(getAgent().getLocalName()) + " и сказал " + answer.getContent());
//        }
    }

    @Override
    public boolean done() {
        return (counter1 >= 3); //isAnswerSend;
    }

    @Override
    public int onEnd() {
        return isHavePower ? 1 : 0;
    }
}
