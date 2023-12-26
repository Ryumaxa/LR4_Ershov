package org.example.ProviderBehs;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
public class ProviderFSM extends FSMBehaviour {
    private ACLMessage RequestFromCons;

    public ProviderFSM(ACLMessage requestFromCons) {
        RequestFromCons = requestFromCons;
    }

    @Getter
    @Setter
    private static List<String> AuctionParticipants = new ArrayList<>(); // Лист участников

    @Getter
    @Setter
    private static String winnerName; // Имя победителя

    @Getter
    @Setter
    private static double powerInfo; // Закупленная мощность

    @Getter
    @Setter
    private static double priceInfo;

    private static final String RECEIVE_REQUEST = "receiveRequest", START_AUCTION = "startAuction", SEND_CONTRACT = "sendContract", END = "end";
    public void onStart() {
        this.registerFirstState(new ReceiveRequestBeh(RequestFromCons), RECEIVE_REQUEST); // OneShotBehaviour с ожиданием запроса от потребителя. Возвращает 1 или 0 в зависимости от успешности поиска производителей
        this.registerState(new StartAuctionBeh(), START_AUCTION); // Поведение создания топика и начала прочитывания сообщений в нем
        this.registerLastState(new SendContractBeh(), SEND_CONTRACT); // Отправка контракта
        this.registerLastState(new OneShotBehaviour() {
            @Override
            public void action() {
                //System.out.println("Не нашлось откликнувшихся участников " + getAgent().getLocalName());
            }
        }, END);

        this.registerTransition(RECEIVE_REQUEST, START_AUCTION, 1); // Если есть, кому участвовать в аукционе
        this.registerDefaultTransition(START_AUCTION, SEND_CONTRACT); // Отправка контракта победителю и отчета потребителю
        this.registerTransition(RECEIVE_REQUEST, END, 0);
    }

}
