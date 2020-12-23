package application.controllers;

import application.models.User;

public class UserController {

  User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
