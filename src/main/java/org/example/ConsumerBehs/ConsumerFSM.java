package org.example.ConsumerBehs;

import jade.core.behaviours.FSMBehaviour;

public class ConsumerFSM extends FSMBehaviour {
    private static final String SEND_REQUEST = "sendRequest", WAIT_RESULTS = "waitResults", SEND_CORRECTED_REQUEST = "sendCorrectedRequest";
    public void onStart() {
        this.registerFirstState(new SendRequestBeh(), SEND_REQUEST); // Дополнить конструктором с неудовлетворительной ценой в случае неуспешного аукциона
        this.registerState(new ConsumerWaitingBeh(), WAIT_RESULTS);
        this.registerLastState(new SendRequestBeh(true), SEND_CORRECTED_REQUEST);

        this.registerDefaultTransition(SEND_REQUEST, WAIT_RESULTS);
        this.registerTransition(WAIT_RESULTS, SEND_CORRECTED_REQUEST, 0);
    }
}