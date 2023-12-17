package org.example.ConsumerBehs;

import jade.core.behaviours.FSMBehaviour;
import org.example.HelperClasses.TimeTracker;

public class ConsumerFSM extends FSMBehaviour {
    private static final String SEND_REQUEST = "sendRequest", WAIT_RESULTS = "waitResults";
    int previousHour = 0;
    public void onStart() {
        this.registerFirstState(new SendRequestBeh(), SEND_REQUEST); // Дополнить конструктором с неудовлетворительной ценой в случае неуспешного аукциона
        this.registerState(new ConsumerWaitingBeh(), WAIT_RESULTS);

        this.registerDefaultTransition(SEND_REQUEST, WAIT_RESULTS);
        this.registerTransition(WAIT_RESULTS, SEND_REQUEST, 0);

        previousHour = TimeTracker.getCurrentHour();
    }

    @Override
    public int onEnd() {
        while (previousHour == TimeTracker.getCurrentHour()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        reset();
        return super.onEnd();
    }

}
