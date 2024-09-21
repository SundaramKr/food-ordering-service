package com.food.Restaurant.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurants")
@Data
@NoArgsConstructor
public class Restaurant {
    @Id
    private ObjectId id;
    @NonNull
    private String name;
    private String imgLink;
    private String[] cuisine;
    private String location;
    private Double ratings;
}
