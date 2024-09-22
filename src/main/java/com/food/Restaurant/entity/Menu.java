package com.food.Restaurant.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu")
@Data
@NoArgsConstructor
public class Menu {
    @Id
    private ObjectId id;
    private String name;
    private Double price;
    private String type;
    private String tags;
    private Double ratings;
    private String imgLink;
    private String[] ingredients;
    private String description;
}
