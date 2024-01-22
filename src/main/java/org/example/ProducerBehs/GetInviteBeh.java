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
    private boolean isAnswerSend = false;
    @Override
    public void action() {
        // Получение данных от поставщика для определения возможности участия в аукционе с такими условиями
        String content = RequestFromCons.getContent();
        String[] values = content.split(";");
        double RequiredPower = Double.parseDouble(values[0]);

        // Проверка доступности мощности для участия в аукционе и соответствующий ответ поставщику
        ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
        answer.setConversationId("AnsFromProducer");
        if (ProducersFunctions.ProducerFunc(getAgent().getLocalName()) - (Double) getAgent().getArguments()[0] >= RequiredPower) {
            answer.setContent("HavePower");
            isHavePower = true;
        } else {
            answer.setContent("NoPower");
        }
        answer.addReceiver(new AID(RequestFromCons.getSender().getLocalName(), false));
        getAgent().send(answer);
        System.out.println(TimeTracker.getCurrentHour() +"..5    Производитель" + getAgent().getLocalName() + " отвечает провайдеру " + RequestFromCons.getSender().getLocalName() + " по поводу наличия у него мощности: " + answer.getContent() + " полагая, что " + (ProducersFunctions.ProducerFunc(getAgent().getLocalName()) - (Double)getAgent().getArguments()[0]) + " > " + RequiredPower);
        isAnswerSend = true;
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
