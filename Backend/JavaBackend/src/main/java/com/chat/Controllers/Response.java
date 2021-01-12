package com.chat.Controllers;

public class Response<T> {
  private int status;
  private String message;
  private T data;

  public Response(int status, String message, T data) {
    this.setData(data);
    this.setStatus(status);
    this.setMessage(message);
  }
  
  public int getStatus() {
    return this.status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
