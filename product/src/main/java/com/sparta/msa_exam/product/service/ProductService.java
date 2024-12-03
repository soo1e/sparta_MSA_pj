package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 추가
    public Product addProduct(Product product) {
        product.setProductId(System.currentTimeMillis()); // ID 생성 (예: 현재 시간)
        return productRepository.save(product); // 상품 저장
    }

    // 모든 상품 조회
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    // 상품 ID로 상품 조회
    @Cacheable(value = "products", key = "#productId") // 캐시 처리
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return new ProductResponseDto(
                product.getProductId(),
                product.getName(),
                product.getSupplyPrice()
        );
    }

    // 상품 존재 여부 확인
    public boolean isProductExists(Long productId) {
        return productRepository.existsById(productId);
    }
}
