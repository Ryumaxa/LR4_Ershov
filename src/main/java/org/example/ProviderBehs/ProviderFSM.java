package org.example.ProviderBehs;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;

public class ProviderFSM extends FSMBehaviour {
    private ACLMessage RequestFromCons;

    public ProviderFSM(ACLMessage requestFromCons) {
        RequestFromCons = requestFromCons;
    }

    private static final String RECEIVE_REQUEST = "receiveRequest", START_AUCTION = "startAuction", SEND_CONTRACT = "sendContract", END = "end";
    public void onStart() {

        // Создаем аргумент поставщику, чтобы хранить в нем текущие данные
        HashMap<String, Object> HashMapProviderInfoMap = new HashMap<>();
        HashMapProviderInfoMap.put("participants", null);
        HashMapProviderInfoMap.put("winner", null);
        HashMapProviderInfoMap.put("power", null);
        HashMapProviderInfoMap.put("price", null);
        Object[] argument = {HashMapProviderInfoMap};
        getAgent().setArguments(argument);

        this.registerFirstState(new ReceiveRequestBeh(RequestFromCons), RECEIVE_REQUEST); // OneShotBehaviour с ожиданием запроса от потребителя. Возвращает 1 или 0 в зависимости от успешности поиска производителей
        this.registerState(new StartAuctionBeh(), START_AUCTION); // Поведение создания топика и начала прочитывания сообщений в нем
        this.registerState(new SendContractBeh(), SEND_CONTRACT); // Отправка контракта
        this.registerLastState(new OneShotBehaviour() {
            @Override
            public void action() {
            }
        }, END);

        this.registerTransition(RECEIVE_REQUEST, START_AUCTION, 1); // Если есть, кому участвовать в аукционе
        this.registerDefaultTransition(START_AUCTION, SEND_CONTRACT); // Отправка контракта победителю и отчета потребителю
        this.registerDefaultTransition(SEND_CONTRACT, END);
        this.registerTransition(RECEIVE_REQUEST, END, 0); // Если участников не оказалось
    }
}
