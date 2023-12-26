package org.example.ConsumerBehs;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import org.example.HelperClasses.TimeTracker;

import java.util.HashMap;
import java.util.Random;

public class SendRequestBeh extends OneShotBehaviour { // Отправка задания на закупку и определение MAX цены
    boolean need_to_upper_price = false;
    public SendRequestBeh() {
    }
    public SendRequestBeh(boolean need_to_upper_price) {
        this.need_to_upper_price = need_to_upper_price;
    }

    @Override
    public void action() {
        Random random = new Random();
        int min = 4;
        int max = 6;
        if (need_to_upper_price) {
            min = 6;
            max = 7;
        }
        // Генерация стартовой цены за кВт*ч
        float maxPrice = random.nextFloat(max - min + 1) + min;

        // Получение значения требуемой мощности в текущем часу
        HashMap<Integer, Double> hashMap = (HashMap<Integer, Double>) getAgent().getArguments()[0];
        double RequiredPower = hashMap.get(TimeTracker.getCurrentHour());

        // Отправка запроса поставщику
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setConversationId("RequestFromConsumer");
        message.setContent(RequiredPower + ";" + maxPrice);
        AID receiver = new AID("ProviderOf" + getAgent().getLocalName(), false);
        message.addReceiver(receiver);
        getAgent().send(message);
        System.out.println("1    Потребитель отправляет запрос провайдеру");
    }
}
