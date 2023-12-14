package org.example.ConsumerBehs;

import jade.core.behaviours.OneShotBehaviour;

import java.util.HashMap;

public class ConsTestBeh extends OneShotBehaviour {
    @Override
    public void action() {
        HashMap<Integer, Double> hashMap = (HashMap<Integer, Double>) getAgent().getArguments()[0];
        System.out.println(getAgent().getLocalName() + " has power of time: " + hashMap);
    }
}
