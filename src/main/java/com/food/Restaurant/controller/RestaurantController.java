package com.food.Restaurant.controller;

import com.food.Restaurant.entity.Restaurant;
import com.food.Restaurant.service.RestaurantService;
import com.food.Restaurant.utils.ResponseConstants;
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

    //GET
    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Restaurant> allEntries = restaurantService.getAllRestaurants();
        if (allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseConstants.NO_RESTAURANTS, HttpStatus.NOT_FOUND);
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> saveRestaurant(@RequestBody Restaurant restaurantBody) {
        try {
            double ratings = restaurantBody.getRatings();
            if (ratings > 5.0 || ratings < 0.0) {
                return new ResponseEntity<>(ResponseConstants.RATING_RANGE, HttpStatus.BAD_REQUEST);
            } else {
                restaurantService.saveRestaurant(restaurantBody);
                return new ResponseEntity<>("ID of New Restaurant: " + restaurantBody.getId(), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //GET by ID (Path Param)
    @GetMapping("/id/{myID}")
    public ResponseEntity<?> getRestaurantById(@PathVariable ObjectId myID) {
        Restaurant restaurant = restaurantService.getRestaurantById(myID).orElse(null);
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    //DELETE
    @DeleteMapping("/id/{myID}")
    public ResponseEntity<?> deleteRestaurantById(@PathVariable ObjectId myID) {
        Restaurant restaurant = restaurantService.getRestaurantById(myID).orElse(null);
        if (restaurant != null) {
            restaurantService.deleteRestaurantById(myID);
            return new ResponseEntity<>("ID of Deleted Restaurant: " + myID, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    //PUT(Update)
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
            return new ResponseEntity<>(ResponseConstants.ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
