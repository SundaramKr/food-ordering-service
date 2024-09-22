package com.food.Restaurant.controller;

import com.food.Restaurant.entity.Menu;
import com.food.Restaurant.entity.Restaurant;
import com.food.Restaurant.service.MenuService;
import com.food.Restaurant.service.RestaurantService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<?> getItems() {
        List<Menu> allItems = menuService.getMenu();
        if (allItems != null && !allItems.isEmpty()) {
            return new ResponseEntity<>(allItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No menu items found in the collection", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{ID}")
    public ResponseEntity<?> getAllMenuItems(@PathVariable ObjectId ID) {
        Restaurant restaurant = restaurantService.getRestaurantById(ID).orElse(null);
        if (restaurant == null)
            return new ResponseEntity<>("Restaurant ID Not Found", HttpStatus.NOT_FOUND);
        List<Menu> menuItems = restaurant.getItems();
        if (menuItems != null && !menuItems.isEmpty()) {
            return new ResponseEntity<>(menuItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No items found in the menu", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("{ID}")
    public ResponseEntity<?> saveMenuItem(@PathVariable ObjectId ID, @RequestBody Menu menuItem) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(ID).orElse(null);
            if (restaurant == null)
                return new ResponseEntity<>("Restaurant ID Not Found", HttpStatus.NOT_FOUND);
            menuService.saveRestaurantMenuItem(menuItem, ID);
            return new ResponseEntity<>("ID of Menu Item " + menuItem.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
