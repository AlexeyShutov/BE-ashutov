package com.scloud.product.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
public class Product {

    @NotNull
    private String uniqId;
    private String sku;
    private String nameTitle;
    private String description;
    private String listPrice;
    private String salePrice;
    private String category;
    private String categoryTree;
    private String averageProductRating;
    private String productUrl;
    private String productImageUrls;
    private String brand;
    private String totalNumberReviews;
    private String reviews;

}
