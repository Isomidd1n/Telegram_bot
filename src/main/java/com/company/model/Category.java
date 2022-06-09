package com.company.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer id;
    private String name;
    private Integer parentCategoryId;
    private boolean deleted = false;

    public Category(Integer id, String name, boolean deleted) {
        this.id = id;
        this.name = name;
        this.deleted = deleted;
    }
}
