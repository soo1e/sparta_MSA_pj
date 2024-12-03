package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.client.ProductClient;
import com.sparta.msa_exam.order.dto.ProductResponseDto;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;
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

    public String addOrderWithFallback(Order order) {
        try {
            // 상품 유효성 체크 로직
            for (Long productId : order.getProductIds()) {
                boolean isProductValid = productClient.isProductExists(productId);
                if (!isProductValid) {
                    return "상품 서비스에서 상품을 찾을 수 없습니다. 상품 ID: " + productId;
                }
            }
            order.setOrderId(System.currentTimeMillis());
            orderRepository.save(order);
            return "주문이 성공적으로 추가되었습니다.";
        } catch (FeignException e) {
            // 상품 서비스 호출 실패 시 fallback 처리
            return "상품 API 호출에 실패했습니다. 잠시 후 다시 시도해주세요.";
        } catch (Exception e) {
            return "예상치 못한 오류가 발생했습니다: " + e.getMessage();
        }
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public String addProductToOrder(Long orderId, Long productId, int quantity) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return "주문을 찾을 수 없습니다.";
        }

        try {
            System.out.println("Checking product existence for productId: " + productId);

            // 상품 정보 조회
            ProductResponseDto product = productClient.getProductById(productId);
            if (product == null) {
                System.err.println("Product not found for productId: " + productId);
                return "상품 서비스에서 상품을 찾을 수 없습니다. 상품 ID: " + productId;
            }

            // 상품 가격 가져오기
            int productPrice = product.getPrice();

            Order order = optionalOrder.get();
            order.getProductIds().add(productId);
            order.setQuantity(order.getQuantity() + quantity);
            order.setPrice(order.getPrice() + (quantity * productPrice));
            orderRepository.save(order);

            System.out.println("Successfully added product to order");
            return "상품이 주문에 성공적으로 추가되었습니다.";

        } catch (FeignException e) {
            System.err.println("Unexpected exception during Feign call: " + e.getMessage());
            e.printStackTrace();
            return "상품 API 호출 중 오류가 발생했습니다: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            return "상품을 주문에 추가하는 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
