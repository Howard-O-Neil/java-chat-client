package com.chat.Models;

import java.util.Objects;
import java.util.UUID;

public class Message {
  // properties

  private UUID id;
  private String sender;
  private String receiver;
  private String content;
  private String fileType;
  private String fileContent;
  private String unixTime;

  public Message() {
  }

  public Message(UUID id, String sender, String receiver, String content, String fileType, String fileContent, String unixTime) {
    this.id = id;
    this.sender = sender;
    this.receiver = receiver;
    this.content = content;
    this.fileType = fileType;
    this.fileContent = fileContent;
    this.unixTime = unixTime;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getSender() {
    return this.sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getReceiver() {
    return this.receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getFileType() {
    return this.fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileContent() {
    return this.fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

  public String getUnixTime() {
    return this.unixTime;
  }

  public void setUnixTime(String unixTime) {
    this.unixTime = unixTime;
  }

  public Message id(UUID id) {
    this.id = id;
    return this;
  }

  public Message sender(String sender) {
    this.sender = sender;
    return this;
  }

  public Message receiver(String receiver) {
    this.receiver = receiver;
    return this;
  }

  public Message content(String content) {
    this.content = content;
    return this;
  }

  public Message fileType(String fileType) {
    this.fileType = fileType;
    return this;
  }

  public Message fileContent(String fileContent) {
    this.fileContent = fileContent;
    return this;
  }

  public Message unixTime(String unixTime) {
    this.unixTime = unixTime;
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Message)) {
            return false;
        }
        Message message = (Message) o;
        return Objects.equals(id, message.id) && Objects.equals(sender, message.sender) && Objects.equals(receiver, message.receiver) && Objects.equals(content, message.content) && Objects.equals(fileType, message.fileType) && Objects.equals(fileContent, message.fileContent) && Objects.equals(unixTime, message.unixTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sender, receiver, content, fileType, fileContent, unixTime);
  }

  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
      ", sender='" + getSender() + "'" +
      ", receiver='" + getReceiver() + "'" +
      ", content='" + getContent() + "'" +
      ", fileType='" + getFileType() + "'" +
      ", fileContent='" + getFileContent() + "'" +
      ", unixTime='" + getUnixTime() + "'" +
      "}";
  }
  
}
