package com.watch.shopwatchonline.Domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private int id;
    private String name;
    private String thumbnail;
    private float price;
    private float discount;
    private int quantity;    
}
