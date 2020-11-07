package application.controllers;


import application.App;
import application.models.Conversation;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class ConversationStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/conversation/" + App._userInstance.getUser().getUserName(), this);
        session.send("/service/socket-service/", App._conversationInstance.receiveNewConversation());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        //exception
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Conversation.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Conversation msg = (Conversation) payload;
        //handel frame
    }


}
