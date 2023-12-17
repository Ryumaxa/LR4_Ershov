package org.example.ProviderBehs;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.example.HelperClasses.DfHelper;

import java.util.List;

public class ReceiveRequestBeh extends Behaviour {

    @Override
    public void action() {
        // Получение запроса от потребителя
        ACLMessage RequestFromConsumer = getAgent().receive(MessageTemplate.MatchConversationId("RequestFromConsumer"));
        if (RequestFromConsumer != null) {
            String content = RequestFromConsumer.getContent();
            String[] values = content.split(";");
            double RequiredPower = Double.parseDouble(values[0]);
            double maxPrice = Double.parseDouble(values[1]);


            // Отправка приглашений производителям
            List<AID> Found_Producers = DfHelper.search(getAgent(), "Produce");
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setConversationId("Invite");
            message.setContent(RequiredPower + ";" + maxPrice); // Передать сразу сюда без преобразований строки?????????
            for (AID aid : Found_Producers) {
                message.addReceiver(new AID(aid.getLocalName(), false));
            }
            getAgent().send(message);





        }



    }

    @Override
    public boolean done() {
        return false;
    }
}
