package com.sparta.msa_exam.order.client;

import com.sparta.msa_exam.order.dto.ProductResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductResponseDto getProductById(Long productId) {
        System.err.println("Failed to fetch product with ID: " + productId);
        return null; // 기본 응답 반환
    }

    @Override
    public boolean isProductExists(Long productId) {
        System.err.println("Failed to check product existence for ID: " + productId);
        return false; // 기본 응답 반환
    }
}
