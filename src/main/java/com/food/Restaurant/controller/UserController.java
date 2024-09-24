package com.food.Restaurant.controller;

import com.food.Restaurant.entity.Menu;
import com.food.Restaurant.entity.User;
import com.food.Restaurant.service.MenuService;
import com.food.Restaurant.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    //Get all Users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("NO USERS IN THE COLLECTION", HttpStatus.NOT_FOUND);
        }
    }

    //Create User
    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User userInfo) {
        try {
            userService.saveEntry(userInfo);
            return new ResponseEntity<>("ID of New User: " + userInfo.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Add Menu Item 'menuId' to User 'userId' cart
    @PostMapping("/userId/{userId}/menuId/{menuId}")
    public ResponseEntity<?> saveUserCart(@PathVariable ObjectId userId, @PathVariable ObjectId menuId) {
        User user = userService.findUserById(userId).orElse(null);
        Menu menu = menuService.getMenuItemById(menuId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("UserId not found", HttpStatus.NOT_FOUND);
        } else if (menu == null) {
            return new ResponseEntity<>("MenuId not found", HttpStatus.NOT_FOUND);
        } else {
            userService.saveUserCart(userId, menuId);
            return new ResponseEntity<>("Item Successfully Added to Cart", HttpStatus.OK);
        }
    }

    //Get User 'userId'
    @GetMapping("/userId/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable ObjectId userId) {
        User user = userService.findUserById(userId).orElse(null);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("UserId not found", HttpStatus.NOT_FOUND);
        }
    }

    //Delete User 'userId'
    @DeleteMapping("/userId/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable ObjectId userId) {
        User user = userService.findUserById(userId).orElse(null);
        if (user != null) {
            userService.removeUserById(userId);
            return new ResponseEntity<>("ID of Deleted User: " + userId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("UserId not found", HttpStatus.NOT_FOUND);
        }
    }

    //Remove Menu Item 'menuId' from User 'userId' cart
    @DeleteMapping("/userId/{userId}/menuId/{menuId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable ObjectId userId, @PathVariable ObjectId menuId) {
        User user = userService.findUserById(userId).orElse(null);
        Menu menu = menuService.getMenuItemById(menuId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("UserId not found", HttpStatus.NOT_FOUND);
        } else if (menu == null) {
            return new ResponseEntity<>("MenuId not found", HttpStatus.NOT_FOUND);
        } else if (!user.getCart().contains(menu)) {
            return new ResponseEntity<>("MenuId not in the Cart", HttpStatus.BAD_REQUEST);
        } else {
            userService.removeUserCart(userId, menuId);
            return new ResponseEntity<>("Item Successfully Removed from Cart", HttpStatus.OK);
        }
    }

    @PutMapping("/userId/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable ObjectId userId) {
        User oldUser = userService.findUserById(userId).orElse(null);
        if (oldUser != null) {
            oldUser.setUserName(newUser.getUserName().isBlank() ? oldUser.getUserName() : newUser.getUserName());
            oldUser.setPassword(newUser.getPassword().isBlank() ? oldUser.getPassword() : newUser.getPassword());
            userService.saveEntry(oldUser);
            return new ResponseEntity<>("ID of Updated User: " + userId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("UserId not found", HttpStatus.NOT_FOUND);
        }
    }
}
