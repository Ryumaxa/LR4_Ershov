package org.example.ProducerBehs;

import jade.core.behaviours.Behaviour;
import org.example.HelperClasses.TimeTracker;

public class ReservedPowerResetBeh extends Behaviour {
    private int hour;

    public ReservedPowerResetBeh(int hour) {
        this.hour = hour;
    }

    @Override
    public void action() {
        if (hour != TimeTracker.getCurrentHour()) {
            getAgent().setArguments(new Object[]{0.0});
            hour = TimeTracker.getCurrentHour();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
