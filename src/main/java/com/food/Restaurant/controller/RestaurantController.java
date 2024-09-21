package com.food.Restaurant.controller;

import com.food.Restaurant.entity.Restaurant;
import com.food.Restaurant.service.RestaurantService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Restaurant> allEntries = restaurantService.getAllRestaurants();
        if (allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No restaurants to show in the collection", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveRestaurant(@RequestBody Restaurant restaurantBody) {
        try {
            double ratings = restaurantBody.getRatings();
            if (ratings > 5.0 || ratings < 0.0) {
                return new ResponseEntity<>("Ratings should be in the range 0.0-5.0", HttpStatus.BAD_REQUEST);
            } else {
                restaurantService.saveRestaurant(restaurantBody);
                return new ResponseEntity<>("ID of New Restaurant: " + restaurantBody.getId(), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myID}")
    public ResponseEntity<?> getRestaurantById(@PathVariable ObjectId myID) {
        Restaurant restaurant = restaurantService.getRestaurantById(myID).orElse(null);
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ID not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/id/{myID}")
    public ResponseEntity<?> deleteRestaurantById(@PathVariable ObjectId myID) {
        Restaurant restaurant = restaurantService.getRestaurantById(myID).orElse(null);
        if (restaurant != null) {
            restaurantService.deleteRestaurantById(myID);
            return new ResponseEntity<>("ID of Deleted Restaurant: " + myID, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ID not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{myID}")
    public ResponseEntity<?> updateRestaurantById(@PathVariable ObjectId myID, @RequestBody Restaurant newRestaurant) {
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
            return new ResponseEntity<>("ID of Updated Restaurant: " + myID, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ID not found", HttpStatus.NOT_FOUND);
        }
    }
}
