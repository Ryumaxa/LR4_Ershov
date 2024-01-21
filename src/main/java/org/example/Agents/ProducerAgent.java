package org.example.Agents;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import org.example.HelperClasses.DfHelper;
import org.example.HelperClasses.TimeTracker;
import org.example.ProducerBehs.ProducerStartBeh;
import org.example.ProducerBehs.ReservedPowerResetBeh;
import org.example.ProviderBehs.ProviderFSM;
import org.example.ProviderBehs.ProviderStartBeh;

public class ProducerAgent extends Agent {
    protected void setup() {
        DfHelper.register(this, "Produce");
        System.out.println(getLocalName() + " готов вырабатывать");
//        this.addBehaviour(new ProducerStartBeh());
        this.setArguments(new Object[]{0.0});

        ParallelBehaviour prodParallelBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
        prodParallelBehaviour.addSubBehaviour(new ProducerStartBeh());
        prodParallelBehaviour.addSubBehaviour(new ReservedPowerResetBeh(TimeTracker.getCurrentHour()));
        this.addBehaviour(prodParallelBehaviour);
    }
}