package com.food.Restaurant.controller;

import com.food.Restaurant.entity.Restaurant;
import com.food.Restaurant.service.RestaurantService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantService.getAllRestaurants();
    }

    @PostMapping
    public String saveRestaurant(@RequestBody Restaurant restaurantBody) {
        double ratings = restaurantBody.getRatings();
        if (ratings > 5.0 || ratings < 0.0) {
            return "Ratings should be in the range 0.0-5.0";
        } else {
            restaurantService.saveRestaurant(restaurantBody);
            return "ID of New Restaurant: " + restaurantBody.getId();
        }
    }

    @GetMapping("/id/{myID}")
    public Restaurant getRestaurantById(@PathVariable ObjectId myID) {
        return restaurantService.getRestaurantById(myID).orElse(null);
    }

    @DeleteMapping("/id/{myID}")
    public String deleteRestaurantById(@PathVariable ObjectId myID) {
        restaurantService.deleteRestaurantById(myID);
        return "ID of Deleted Restaurant: " + myID;
    }

    @PutMapping("/id/{myID}")
    public String updateRestaurantById(@PathVariable ObjectId myID, @RequestBody Restaurant newRestaurant) {
        Restaurant oldRestaurant = restaurantService.getRestaurantById(myID).orElse(null);
        if (oldRestaurant != null) {
            //IF newRestaurant's <parameter> is blank set the oldRestaurant's <parameter> as itself
            //ELSE set oldRestaurant's <parameter> as newRestaurant's updated <parameter>
            oldRestaurant.setName(newRestaurant.getName().isBlank() ? oldRestaurant.getName() : newRestaurant.getName());
            oldRestaurant.setImgLink(newRestaurant.getImgLink().isBlank() ? oldRestaurant.getImgLink() : newRestaurant.getImgLink());
            oldRestaurant.setCuisine(newRestaurant.getCuisine()[0].isBlank() ? oldRestaurant.getCuisine() : newRestaurant.getCuisine());
            oldRestaurant.setLocation(newRestaurant.getLocation().isBlank() ? oldRestaurant.getLocation() : newRestaurant.getLocation());
            oldRestaurant.setRatings(newRestaurant.getRatings() > 5.0 || newRestaurant.getRatings() < 0.0 ? oldRestaurant.getRatings() : newRestaurant.getRatings());
            restaurantService.saveRestaurant(oldRestaurant);
            return "ID of Updated Restaurant: " + myID;
        } else {
            return "ID " + myID + " not found";
        }
    }
}
