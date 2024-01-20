package org.example.ConsumerBehs;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import org.example.HelperClasses.TimeTracker;

public class ConsumerCyclicBeh extends Behaviour { // Поведение для вызова FSM в начале каждого часа
    private int hour;

    public ConsumerCyclicBeh(int hour) {
        this.hour = hour;
    }

    @Override
    public void action() {
        if (hour != TimeTracker.getCurrentHour()) {
            FSMBehaviour fsmBehaviour = new ConsumerFSM();
            fsmBehaviour.reset();
            getAgent().addBehaviour(fsmBehaviour);
            hour = TimeTracker.getCurrentHour();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
