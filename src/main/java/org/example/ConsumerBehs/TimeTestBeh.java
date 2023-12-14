package org.example.ConsumerBehs;

import jade.core.behaviours.Behaviour;
import org.example.HelperClasses.TimeTracker;

public class TimeTestBeh extends Behaviour {
    @Override
    public void action() {
        while (true) {
            try {
                System.out.println(TimeTracker.getCurrentHour());
                System.out.println(TimeTracker.getMillsUntilNextHour());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }




    }

    @Override
    public boolean done() {
        return false;
    }
}
