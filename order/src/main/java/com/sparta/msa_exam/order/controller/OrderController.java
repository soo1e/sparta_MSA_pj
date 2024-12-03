package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.ProductAddRequest;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 추가 API
    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody Order order, HttpServletRequest request) {
        Order savedOrder = orderService.addOrder(order);

        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // Header에 Server-Port 추가
                .body(savedOrder);
    }

    // 주문 단건 조회 API
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId, HttpServletRequest request) {
        Optional<Order> order = orderService.getOrderById(orderId);

        if (order.isEmpty()) {
            return ResponseEntity.notFound()
                    .header("Server-Port", String.valueOf(request.getLocalPort())) // Header에 Server-Port 추가
                    .build();
        }

        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(request.getLocalPort())) // Header에 Server-Port 추가
                .body(order.get());
    }

    @PutMapping("/add-product")
    public ResponseEntity<String> addProductToOrder(@RequestBody ProductAddRequest request, HttpServletRequest httpServletRequest) {
        String result = orderService.addProductToOrder(request.getOrderId(), request.getProductId(), request.getQuantity());

        if (result.contains("not found") || result.contains("Failed")) {
            return ResponseEntity.badRequest()
                    .header("Server-Port", String.valueOf(httpServletRequest.getLocalPort())) // Header에 Server-Port 추가
                    .body(result);
        }

        return ResponseEntity.ok()
                .header("Server-Port", String.valueOf(httpServletRequest.getLocalPort())) // Header에 Server-Port 추가
                .body(result);
    }
}
