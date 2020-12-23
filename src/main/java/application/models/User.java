package application.models;

import java.util.UUID;

public class User {

  private UUID id;
  private String userName;
  private String passWord;

  public User() {}

  public User(String userName) {
    this.userName = userName;
  }

  public User(String userName, String passWord) {
    this.userName = userName;
    this.passWord = passWord;
  }

  public User(UUID id, String userName, String passWord) {
    this.id = id;
    this.userName = userName;
    this.passWord = passWord;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassWord() {
    return this.passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }
}
