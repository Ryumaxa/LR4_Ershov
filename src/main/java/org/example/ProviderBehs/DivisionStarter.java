package org.example.ProviderBehs;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class DivisionStarter extends OneShotBehaviour {

    private ACLMessage RequestFromCons;
    FSMBehaviour divisionBeh = null;

    public DivisionStarter(ACLMessage requestFromCons) {
        RequestFromCons = requestFromCons;
    }

    @Override
    public void action() {
        int counter = 0;
        while (counter < 3) {
            if (divisionBeh != null) {
                getAgent().removeBehaviour(divisionBeh);
            }
            divisionBeh = new DivisionFSM(RequestFromCons);
            getAgent().addBehaviour(divisionBeh);
            counter++;
        }
    }
}
