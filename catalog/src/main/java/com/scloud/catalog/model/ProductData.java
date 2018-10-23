package com.scloud.catalog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Objects;

@RedisHash("ProductData")
public class ProductData {

    @Id
    private String uniqId;
    @Indexed
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

    private ProductData() {}

    private ProductData(Builder builder) {
        uniqId = builder.uniqId;
        sku = builder.sku;
        nameTitle = builder.nameTitle;
        description = builder.description;
        listPrice = builder.listPrice;
        salePrice = builder.salePrice;
        category = builder.category;
        categoryTree = builder.categoryTree;
        averageProductRating = builder.averageProductRating;
        productUrl = builder.productUrl;
        productImageUrls = builder.productImageUrls;
        brand = builder.brand;
        totalNumberReviews = builder.totalNumberReviews;
        reviews = builder.reviews;
    }

    public static class Builder {

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

        public Builder(String uniqId, String sku) {
            this.uniqId = uniqId;
            this.sku = sku;
        }

        public Builder title(String title) {
            this.nameTitle = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder prices(String listPrice, String salePrice) {
            this.listPrice = listPrice;
            this.salePrice = salePrice;
            return this;
        }

        public Builder category(String category, String categoryTree) {
            this.category = category;
            this.categoryTree = categoryTree;
            return this;
        }

        public Builder averageRating(String averageRating) {
            this.averageProductRating = averageRating;
            return this;
        }

        public Builder urls(String productUrl, String imageUrls) {
            this.productUrl = productUrl;
            this.productImageUrls = imageUrls;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder reviews(String reviews, String number) {
            this.reviews = reviews;
            this.totalNumberReviews = number;
            return this;
        }

        public ProductData build() {
            return new ProductData(this);
        }
    }

    public String getUniqId() {
        return uniqId;
    }

    public String getSku() {
        return sku;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getListPrice() {
        return listPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryTree() {
        return categoryTree;
    }

    public String getAverageProductRating() {
        return averageProductRating;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public String getProductImageUrls() {
        return productImageUrls;
    }

    public String getBrand() {
        return brand;
    }

    public String getTotalNumberReviews() {
        return totalNumberReviews;
    }

    public String getReviews() {
        return reviews;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (that == null || getClass() != that.getClass())
            return false;

        ProductData productData = (ProductData) that;
        return Objects.equals(uniqId, productData.uniqId) &&
                Objects.equals(sku, productData.sku) &&
                Objects.equals(nameTitle, productData.nameTitle) &&
                Objects.equals(description, productData.description) &&
                Objects.equals(listPrice, productData.listPrice) &&
                Objects.equals(salePrice, productData.salePrice) &&
                Objects.equals(category, productData.category) &&
                Objects.equals(categoryTree, productData.categoryTree) &&
                Objects.equals(averageProductRating, productData.averageProductRating) &&
                Objects.equals(productUrl, productData.productUrl) &&
                Objects.equals(productImageUrls, productData.productImageUrls) &&
                Objects.equals(brand, productData.brand) &&
                Objects.equals(totalNumberReviews, productData.totalNumberReviews) &&
                Objects.equals(reviews, productData.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqId, sku, nameTitle, description, listPrice, salePrice, category, categoryTree,
                averageProductRating, productUrl, productImageUrls, brand, totalNumberReviews, reviews);
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "uniqId='" + uniqId + '\'' +
                ", sku='" + sku + '\'' +
                ", nameTitle='" + nameTitle + '\'' +
                ", description='" + description + '\'' +
                ", listPrice=" + listPrice +
                ", salePrice=" + salePrice +
                ", category='" + category + '\'' +
                ", categoryTree='" + categoryTree + '\'' +
                ", averageProductRating='" + averageProductRating + '\'' +
                ", productUrl='" + productUrl + '\'' +
                ", productImageUrls='" + productImageUrls + '\'' +
                ", brand='" + brand + '\'' +
                ", totalNumberReviews=" + totalNumberReviews +
                ", reviews=" + reviews +
                '}';
    }
}
