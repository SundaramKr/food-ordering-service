package com.food.Restaurant.repository;

import com.food.Restaurant.entity.Menu;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, ObjectId> {
}
