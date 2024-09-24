package com.food.Restaurant.service;

import com.food.Restaurant.entity.Menu;
import com.food.Restaurant.entity.User;
import com.food.Restaurant.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuService menuService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //POST & PUT(update)
    public void saveEntry(User user) {
        userRepository.save(user);
    }

    public void saveUserCart(ObjectId userId, ObjectId menuId) {
        User user = userRepository.findById(userId).orElse(null);
        Menu menu = menuService.getMenuItemById(menuId).orElse(null);
        if (user != null && menu != null) {
            user.getCart().add(menu);
            userRepository.save(user);
        }
    }

    //PathParam or GET by id
    public Optional<User> findUserById(ObjectId myID) {
        return userRepository.findById(myID);
    }

    //DELETE
    public void removeUserById(ObjectId myID) {
        userRepository.deleteById(myID);
    }

    public void removeUserCart(ObjectId userId, ObjectId menuId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        user.getCart().removeIf(x -> x.getId().equals(menuId));
        userRepository.save(user);
    }
}
