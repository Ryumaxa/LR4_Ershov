package org.example.HelperClasses;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;

public class TopicHelper {
    public static AID register(Agent a, String topicName) throws ServiceException {
        TopicManagementHelper tmh = (TopicManagementHelper) a.getHelper(TopicManagementHelper.SERVICE_NAME);
        AID topic = tmh.createTopic(topicName);
        try {
            tmh.register(topic);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return topic;
    }
}
