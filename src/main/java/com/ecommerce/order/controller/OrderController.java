package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CheckoutRequest;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.sercurity.SecurityUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST de pedidos (checkout + consulta).
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"http://localhost:4200", "https://frontend-angular-g2ev.vercel.app"}, allowCredentials = "true")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Cria pedido (checkout). */
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    /** Busca pedido por ID (usuário logado). */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        String email = SecurityUtils.getAuthenticatedEmail();
        Order order = orderService.findByIdAndUserEmail(id, email);
        return ResponseEntity.ok(order);
    }

    /** Simula pagamento. */
    @PutMapping("/{id}/pay")
    public ResponseEntity<Order> pay(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markAsPaid(id));
    }

    /** Lista pedidos do usuário logado. */
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        String email = SecurityUtils.getAuthenticatedEmail();
        List<Order> orders = orderService.findByUserEmail(email);
        return ResponseEntity.ok(orders);
    }
}
