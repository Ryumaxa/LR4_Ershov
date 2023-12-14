package org.example.ProducerBehs;

import jade.core.behaviours.OneShotBehaviour;
import org.example.HelperClasses.ProducersFunctions;

public class ProdTestBeh extends OneShotBehaviour {
    @Override
    public void action() {
        System.out.println(getAgent().getLocalName() + " have POWER = " + ProducersFunctions.ProducerFunc(getAgent().getLocalName()));
    }
}
