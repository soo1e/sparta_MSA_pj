package com.sparta.msa_exam.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Product") // Redis에서 이 엔티티를 해시(Hash) 형태로 저장
public class Product implements Serializable {

    @Id
    private Long productId; // Primary Key

    private String name; // 상품 이름

    private Integer supplyPrice; // 공급 가격
}
