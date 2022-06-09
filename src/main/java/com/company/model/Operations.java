package com.company.model;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Operations {
    Integer id;
    Integer category_id;
    Integer product_id;
    Timestamp updated_at;
    String  operation_name;
}
