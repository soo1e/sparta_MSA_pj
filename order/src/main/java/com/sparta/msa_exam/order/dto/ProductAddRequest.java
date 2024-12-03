package com.sparta.msa_exam.order.dto;

import lombok.Data;

@Data
public class ProductAddRequest {
    private Long orderId;
    private Long productId;
    private int quantity;
}
