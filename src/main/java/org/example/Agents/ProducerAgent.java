package org.example.Agents;

import jade.core.Agent;
import org.example.ProducerBehs.ProdTestBeh;

public class ProducerAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + " готов вырабатывать");

        this.addBehaviour(new ProdTestBeh());
    }
    //вырабатывает заданную мощность в соответствии со своими исходными данными
    //участвует в торгах ЭЭ для продажи доступной мощности агентам-поставщикам
}
