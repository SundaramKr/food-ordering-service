package com.food.Restaurant.service;

import com.food.Restaurant.entity.Restaurant;
import com.food.Restaurant.repository.RestaurantRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    //GET
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    //POST & PUT(update)
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    //PathParam or Get by id
    public Optional<Restaurant> getRestaurantById(ObjectId id) {
        return restaurantRepository.findById(id);
    }

    //DELETE
    public void deleteRestaurantById(ObjectId id) {
        restaurantRepository.deleteById(id);
    }
}
