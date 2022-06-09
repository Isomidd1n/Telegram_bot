package com.company.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartProduct {
    private Integer id;
    private String customerId;
    private Integer productId;
    private Integer quantity;


    public CartProduct(String customerId, Integer productId, Integer quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
