package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;
import com.sparta.msa_exam.order.client.ProductClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public Order addOrder(Order order) {
        order.setOrderId(System.currentTimeMillis());
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public String addProductToOrder(Long orderId, Long productId, int quantity) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return "Order not found";
        }

        try {
            System.out.println("Checking product existence for productId: " + productId);
            boolean isProductValid = productClient.isProductExists(productId);

            if (!isProductValid) {
                System.err.println("Product not found for productId: " + productId);
                return "Product not found in product-service";
            }

            Order order = optionalOrder.get();
            order.getProductIds().add(productId);
            order.setQuantity(order.getQuantity() + quantity);
            order.setPrice(order.getPrice() + (quantity * 1000)); // 상품 가격 1000원 가정
            orderRepository.save(order);

            System.out.println("Successfully added product to order");
            return "Product added to the order successfully";

        } catch (Exception e) {
            System.err.println("Unexpected exception during Feign call: " + e.getMessage());
            e.printStackTrace();
            return "Failed to add product to order: " + e.getMessage();
        }
    }
}
