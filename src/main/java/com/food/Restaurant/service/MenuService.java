package com.food.Restaurant.service;

import com.food.Restaurant.entity.Menu;
import com.food.Restaurant.entity.Restaurant;
import com.food.Restaurant.repository.MenuRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantService restaurantService;

    //GET
    public List<Menu> getMenu() {
        return menuRepository.findAll();
    }

    //POST & PUT(update)
    public void saveMenuItem(Menu menuItem) {
        menuRepository.save(menuItem);
    }

    //PathParam or Get by id
    public Optional<Menu> getMenuItemById(ObjectId id) {
        return menuRepository.findById(id);
    }

    //DELETE
    public void deleteMenuItemById(ObjectId id) {
        menuRepository.deleteById(id);
    }

    public void saveRestaurantMenuItem(Menu menuItem, ObjectId id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id).orElse(null);
        Menu saved = menuRepository.save(menuItem);
        if (restaurant != null)
            restaurant.getItems().add(saved);
        restaurantService.saveRestaurant(restaurant);
    }

    public void removeRestaurantMenuItem(ObjectId restaurantId, ObjectId menuId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId).orElse(null);
        if (restaurant != null)
            restaurant.getItems().removeIf(x -> x.getId().equals(menuId));
        restaurantService.saveRestaurant(restaurant);
        menuRepository.deleteById(menuId);
    }
}
