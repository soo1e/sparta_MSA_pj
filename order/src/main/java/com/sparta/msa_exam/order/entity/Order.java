package com.sparta.msa_exam.order.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash("Order")
public class Order {

    @Id
    private Long orderId; // 주문 ID
    private List<Long> productIds = new ArrayList<>(); // 상품 ID 리스트
    private int quantity; // 주문 수량 (전체 상품 수량)
    private double price; // 주문 총액
}
