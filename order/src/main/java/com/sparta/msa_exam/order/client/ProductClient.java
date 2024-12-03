package com.sparta.msa_exam.order.client;

import com.sparta.msa_exam.order.config.FeignClientConfig;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product", configuration = FeignClientConfig.class)
public interface ProductClient {
    @GetMapping("/products/{productId}")
    ProductResponseDto getProductById(@PathVariable("productId") Long productId);

    @GetMapping("/products/exists")
    boolean isProductExists(@RequestParam("product_id") Long productId);
}





