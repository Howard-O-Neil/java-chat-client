package com.chat.Controllers;

import com.chat.App;
import com.chat.Models.Conversation;
import com.chat.Models.User;
import com.chat.Services.ConversationService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(App.API + "/conversation")
@RestController
public class ConversationController {
  private ConversationService conversationService;

  @Autowired
  ConversationController(ConversationService conversationnService) {
    this.conversationService = conversationnService;
  }

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  private Response<Object> AddConversation(Conversation conversation) {
    Optional<Boolean> res = conversationService.AddConversation(conversation);

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      return ResponseHandler.ok(res.get());
    }
  }

  @PostMapping("/add")
  public Response<Object> SignUpConversation(
    @RequestBody Conversation conversation
  ) {
    Optional<Boolean> res = conversationService.CheckAvailableConversation(
      conversation
    );

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      if (res.get()) {
        return AddConversation(conversation);
      } else {
        return ResponseHandler.createdConversation("");
      }
    }
  }

  @MessageMapping("/notify-conversation")
  public void SendNotifications(@Payload Conversation conversation) {
    Conversation otherConversation = new Conversation();
    otherConversation.setId(UUID.randomUUID());
    otherConversation.setReceiver(conversation.getSender());
    otherConversation.setSender(conversation.getReceiver());

    Optional<Boolean> condition1 = conversationService.CheckAvailableConversation(
      conversation
    );
    Optional<Boolean> condition2 = conversationService.CheckAvailableConversation(
      otherConversation
    );

    if (condition1.isEmpty() || condition2.isEmpty()) {
      this.simpMessagingTemplate.convertAndSend(
          String.format("/conversation/%s", conversation.getSender()),
          ResponseHandler.error("")        
        );
    } else {
      if (condition1.get() && condition2.get()) {
        this.AddConversation(conversation);
        this.AddConversation(otherConversation);

        this.simpMessagingTemplate.convertAndSend(
            String.format("/conversation/%s", conversation.getSender()),
            ResponseHandler.ok(conversation)
          );
        this.simpMessagingTemplate.convertAndSend(
            String.format("/conversation/%s", otherConversation.getSender()),
            ResponseHandler.ok(otherConversation)
          );
      }
    }
  }

  @PostMapping("/check")
  public Response<Object> CheckConversation(
    @RequestBody Conversation conversation
  ) {
    Optional<Boolean> res = conversationService.CheckAvailableConversation(
      conversation
    );

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      if (res.get()) {
        return ResponseHandler.ok(res.get());
      } else {
        return ResponseHandler.createdConversation("");
      }
    }
  }

  @GetMapping("/get")
  public Response<Object> GetConversation(
    @RequestParam(name = "username", required = true) String userName,
    @RequestParam(name = "index", required = true) int index
  ) {
    Optional<List<Conversation>> res = conversationService.GetConversation(
      new User(userName),
      index
    );

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      return ResponseHandler.ok(res.get());
    }
  }
}
