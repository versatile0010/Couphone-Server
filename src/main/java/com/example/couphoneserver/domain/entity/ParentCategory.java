package com.example.couphoneserver.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(name = "PARENT_CATEGORY")
public class ParentCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_category_id")
    private Long id;
    String name;

    @OneToMany(mappedBy = "parentCategory")
    private List<ChildCategory> childCategories = new ArrayList<>();

    @OneToOne(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private Brand brand;

    @Builder
    public ParentCategory(String name) {
        this.name = name;
    }
}
