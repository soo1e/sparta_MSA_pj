package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.ProductResponseDto;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. 상품 추가 API
    @PostMapping
    @CacheEvict(value = "products", allEntries = true) // 캐시 초기화
    public ResponseEntity<Product> addProduct(@RequestBody Product product, HttpServletRequest request) {
        Product addedProduct = productService.addProduct(product);
        System.out.println("[LOG] 상품 추가됨. 캐시 초기화 완료.");
        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // 서버 포트 헤더 추가
                .body(addedProduct);
    }


    // 2. 상품 목록 조회 API
    @GetMapping
    @Cacheable(value = "products", key = "'allProducts'") // 캐시 저장
    public ResponseEntity<List<Product>> getAllProducts(HttpServletRequest request) {
        System.out.println("[LOG] 상품 목록 조회 요청. 캐시 확인.");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // 서버 포트 헤더 추가
                .body(products);
    }

    // 상품 ID로 상품 조회 API
    @GetMapping("/{productId}")
    @Cacheable(value = "products", key = "#productId") // 캐시 저장
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId, HttpServletRequest request) {
        ProductResponseDto product = productService.getProductById(productId);
        System.out.println("[LOG] 상품 ID " + productId + " 조회 요청. 캐시 확인.");
        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // 헤더에 Server-Port 추가
                .body(product);
    }
}
