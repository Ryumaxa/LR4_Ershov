package org.example.ProviderBehs;

import jade.core.behaviours.FSMBehaviour;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
public class ProviderFSM extends FSMBehaviour {
    public static void setAuctionParticipants(List<String> auctionParticipants) {
        AuctionParticipants = auctionParticipants;
    }
    @Getter
    private static List<String> AuctionParticipants = new ArrayList<>();

    private static final String RECEIVE_REQUEST = "receiveRequest", START_AUCTION = "startAuction", SEND_CORRECTED_REQUEST = "sendCorrectedRequest";
    public void onStart() {
        this.registerFirstState(new ReceiveRequestBeh(), RECEIVE_REQUEST); // OneShotBehaviour с ожиданием запроса от потребителя. Возвращает 1 или 0 в зависимости от успешности поиска производителей
        this.registerState(new StartAuctionBeh(), START_AUCTION); // Поведение создания топика и начала прочитывания сообщений в нем

        this.registerTransition(RECEIVE_REQUEST, START_AUCTION, 1);
    }

}
