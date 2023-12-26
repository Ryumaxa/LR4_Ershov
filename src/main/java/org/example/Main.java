package org.example;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import org.example.Agents.ConsumerAgent;
import org.example.Agents.ProducerAgent;
import org.example.Agents.ProviderAgent;
import org.example.HelperClasses.ProducersFunctions;
import org.example.HelperClasses.TimeTracker;

import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws StaleProxyException {
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        Properties props = new ExtendedProperties();
        props.setProperty("gui", "true");
        props.setProperty(Profile.SERVICES, "jade.core.messaging.TopicManagementService");
        ProfileImpl profile = new ProfileImpl(jade.util.leap.Properties.toLeapProperties(props));
        AgentContainer container = runtime.createMainContainer(profile);

        TimeTracker.StartTime();
        ProducersFunctions.CountPowerOfWindPP();

        AgentController agentController = container.createNewAgent("CvetMet", ConsumerAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("ObuvnayaFabrica", ConsumerAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("PishProm", ConsumerAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("ProviderOfCvetMet", ProviderAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("ProviderOfObuvnayaFabrica", ProviderAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("ProviderOfPishProm", ProviderAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("HeatPP", ProducerAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("WindPP", ProducerAgent.class.getName(), null);
        agentController.start();

        agentController = container.createNewAgent("SolarPP", ProducerAgent.class.getName(), null);
        agentController.start();



    }
}
