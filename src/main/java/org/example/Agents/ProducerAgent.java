package org.example.Agents;

import jade.core.Agent;
import org.example.HelperClasses.DfHelper;
import org.example.ProducerBehs.GetInviteBeh;
import org.example.ProducerBehs.ProdTestBeh;

public class ProducerAgent extends Agent {
    protected void setup() {
        DfHelper.register(this, "Produce");
        System.out.println(getLocalName() + " готов вырабатывать");
        this.addBehaviour(new ProdTestBeh());
        this.addBehaviour(new GetInviteBeh());
    }
    //вырабатывает заданную мощность в соответствии со своими исходными данными
    //участвует в торгах ЭЭ для продажи доступной мощности агентам-поставщикам
}