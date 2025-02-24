package com.chat.Controllers;

import com.chat.App;
import com.chat.Models.User;
import com.chat.Services.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(App.API + "/user")
@RestController
public class UserController {
  private UserService userService;

  @Autowired
  UserController(UserService userService) {
    this.userService = userService;
  }

  private Response<Object> AddUser(User user) {
    Optional<Boolean> res = userService.AddUser(user);

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      return ResponseHandler.ok(res.get());
    }
  }

  @PostMapping(path = "/add")
  public Response<Object> SignUpUser(@RequestBody User user) {
    Optional<Boolean> isUserNameAvailable = userService.CheckAvailableUserName(
      user.getUserName()
    );

    if (isUserNameAvailable.isEmpty()) {
      return ResponseHandler.error("");
      //
    } else {
      if (isUserNameAvailable.get()) {
        return AddUser(user);
      } else {
        return ResponseHandler.usernameNotAvailable("");
      }
    }
  }

  @GetMapping(path = "/checkusername")
  public Response<Object> CheckUserName(
    @RequestParam(name = "username", required = true) String userName
  ) {
    Optional<Boolean> res = userService.CheckAvailableUserName(userName);

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      if (res.get()) {
        return ResponseHandler.ok("available");
      } else {
        return ResponseHandler.usernameNotAvailable("");
      }
    }
  }

  @GetMapping(path = "/find")
  public Response<Object> FindUser(
    @RequestParam(name = "searchkey", required = true) String searchKey
  ) {
    Optional<List<String>> res = userService.FindUser(searchKey);

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      return ResponseHandler.ok(res.get());
    }
  }

  @PostMapping(path = "/checklogin")
  public Response<Object> CheckLogin(@RequestBody User user) {
    Optional<User> res = userService.GetUser(user.getUserName());
    Optional<Boolean> check = userService.CheckAvailableUserName(
      user.getUserName()
    );

    if (check.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      if (!check.get()) {
        if (!user.getPassWord().equals(res.get().getPassWord())) {
          return ResponseHandler.loginFailed("");
          //
        } else return ResponseHandler.ok(res.get());
        //
      } else {
        return ResponseHandler.loginFailed("");
      }
    }
  }

  @GetMapping(path = "/get")
  public Response<Object> GetUser(
    @RequestParam(name = "username", required = true) String userName
  ) {
    Optional<User> res = userService.GetUser(userName);

    if (res.isEmpty()) {
      return ResponseHandler.error("");
    } else {
      return ResponseHandler.ok(res.get());
    }
  }
}
