package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", url = "http://localhost:19093")
public interface ProductServiceClient {

    @GetMapping("/products/{productId}")
    ProductResponse getProductById(@PathVariable("productId") Long productId);

    @PostMapping("/products/check-stock")
    Boolean checkStock(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity);
}
