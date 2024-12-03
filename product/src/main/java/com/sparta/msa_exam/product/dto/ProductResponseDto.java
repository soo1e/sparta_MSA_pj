package com.sparta.msa_exam.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long productId;      // 상품 ID
    private String name;         // 상품 이름
    private Integer supplyPrice; // 공급 가격
}
