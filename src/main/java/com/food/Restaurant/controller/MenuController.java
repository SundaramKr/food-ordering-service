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
import java.util.Optional;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    //Get all Menu Items in the collection menu
    @GetMapping
    public ResponseEntity<?> getItems() {
        List<Menu> allItems = menuService.getMenu();
        if (allItems != null && !allItems.isEmpty()) {
            return new ResponseEntity<>(allItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No menu items found in the collection", HttpStatus.NOT_FOUND);
        }
    }

    //Get all the Menu Items of Restaurant 'ID'
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

    //Post the Menu Item to the Restaurant 'ID'
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

    //Get the Menu Item 'menuId'
    @GetMapping("/id/{menuId}")
    public ResponseEntity<?> getMenuItemById(@PathVariable ObjectId menuId) {
        Optional<Menu> menuItem = menuService.getMenuItemById(menuId);
        if (menuItem.isPresent()) {
            return new ResponseEntity<>(menuItem.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Menu Item ID not found", HttpStatus.NOT_FOUND);
        }
    }

    //Delete the Menu Item 'menuId' from the Restaurant 'restaurantId'
    @DeleteMapping("/restaurantId/{restaurantId}/menuId/{menuId}")
    public ResponseEntity<?> removeMenuItem(@PathVariable ObjectId restaurantId, @PathVariable ObjectId menuId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId).orElse(null);
        //Valid restaurantId check
        if (restaurant == null)
            return new ResponseEntity<>("Restaurant ID Not Found", HttpStatus.NOT_FOUND);

        Menu Item = menuService.getMenuItemById(menuId).orElse(null);
        List<Menu> menuItems = restaurant.getItems();
        //valid menuId link to restaurantId check
        if (!menuItems.contains(Item))
            return new ResponseEntity<>("Menu Item does not belong to Restaurant", HttpStatus.BAD_REQUEST);
        //valid menuId check
        if (Item != null) {
            menuService.removeRestaurantMenuItem(restaurantId, menuId);
            return new ResponseEntity<>("Deleted ID " + menuId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Menu Item ID not found", HttpStatus.NOT_FOUND);
        }
    }

    //Update the Menu Item 'menuId' from the Restaurant 'restaurantId'
    @PutMapping("/restaurantId/{restaurantId}/menuId/{menuId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable ObjectId restaurantId,
                                            @PathVariable ObjectId menuId,
                                            @RequestBody Menu newItem) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId).orElse(null);
        //Valid restaurantId check
        if (restaurant == null)
            return new ResponseEntity<>("Restaurant ID Not Found", HttpStatus.NOT_FOUND);

        Menu oldItem = menuService.getMenuItemById(menuId).orElse(null);
        List<Menu> menuItems = restaurant.getItems();
        //valid menuId link to restaurantId check
        if (!menuItems.contains(oldItem))
            return new ResponseEntity<>("Menu Item does not belong to Restaurant", HttpStatus.BAD_REQUEST);
        //valid menuId check
        if (oldItem != null) {
            oldItem.setName(newItem.getName().isBlank() ? oldItem.getName() : newItem.getName());
            menuService.saveMenuItem(oldItem);
            return new ResponseEntity<>("ID of updated menu item " + menuId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Menu Item ID not found", HttpStatus.NOT_FOUND);
        }
    }
}
