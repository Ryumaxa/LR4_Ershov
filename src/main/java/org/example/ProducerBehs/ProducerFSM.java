package org.example.ProducerBehs;

import jade.core.behaviours.FSMBehaviour;

public class ProducerFSM extends FSMBehaviour {
    private static final String GET_INVITE = "getInvite", GOTO_AUCTION = "goToAuctionAuction", GET_CONTRACT = "getContract";
    public void onStart() {
        this.registerFirstState(new GetInviteBeh(), GET_INVITE); // Проверка наличия мощности и подтверждение участия в аукционе
        this.registerState(new GoToAuctionBeh(), GOTO_AUCTION); // Поведение создания топика и начала прочитывания сообщений в нем
        this.registerLastState(new GoToAuctionBeh(), GOTO_AUCTION); // Поведение заключения контракта

        this.registerTransition(GET_INVITE, GOTO_AUCTION, 1); // Если мощность есть (1), идем на аукцион
        this.registerTransition(GOTO_AUCTION, GET_CONTRACT, 1); // Если мы победили, идем заключать контракт (или не заключать)
    }
}
