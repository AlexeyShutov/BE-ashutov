package com.scloud.product.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ProductAvailability {

    @NotNull
    private String uniqId;
    @Setter
    private boolean available;

}
