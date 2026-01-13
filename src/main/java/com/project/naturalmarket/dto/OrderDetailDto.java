package com.project.naturalmarket.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailDto {


    private Long id;
    @NotNull
    private int totalProduct;
    @NotNull
    private double totalPrice;

    private Date timestamp;

    private long orderId;

    private long productId;
}
