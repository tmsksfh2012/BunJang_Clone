package com.example.demo.src.goods.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Category {
    private int categoryId;
    private String categoryName;
    private int counting;
}
