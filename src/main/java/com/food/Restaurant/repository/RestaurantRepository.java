package com.food.Restaurant.repository;

import com.food.Restaurant.entity.Restaurant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
}
