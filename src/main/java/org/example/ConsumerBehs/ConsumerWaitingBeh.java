package org.example.ConsumerBehs;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.TimeTracker;

public class ConsumerWaitingBeh extends Behaviour { // СЧИТАЮ ЭТО ПОВЕДЕНИЕ РАБОЧИМ
    String result = null;
    boolean ansReceived = false;
    @Override
    public void action() {
        // Ожидает результат аукциона от своего поставщика
        ACLMessage AuctionResults = getAgent().receive(MessageTemplate.MatchConversationId("AuctionResults"));
        if (AuctionResults != null) {
            ansReceived = true;
            String content = AuctionResults.getContent();
            String[] values = content.split(";");
            if (values.length > 1) {
                // Если аукцион завершился успешно, сообщение будет содержать полную информацию
                result = values[0];
                double requiredPower = Double.parseDouble(values[1]);
                double price = Double.parseDouble(values[2]);
                String winner = values[3];
                System.out.println(getAgent().getLocalName() + " успешно закупил " + requiredPower + " кВт*ч по цене " + price + " руб. за кВт*ч у " + winner + ", час = " + TimeTracker.getCurrentHour());
            } else {
                System.out.println("Неудачная попытка закупки мощности за " + TimeTracker.getCurrentHour() + " час");
            }
        }
    }

    @Override
    public boolean done() {
        return ansReceived;
    }

    public int onEnd() {
        return result.equals("success") ? 1 : 0;
    }


}
