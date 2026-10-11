package com.ecommerce.order.service;

import com.ecommerce.order.dto.CheckoutRequest;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.sercurity.SecurityUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 📦 Listar pedidos do usuário logado (com itens).
     */
    public List<Order> findByUserEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }

        return orderRepository.findByUserEmailWithItems(email);
    }

    /**
     * 🧾 Criar novo pedido com itens
     * 🔥 Transacional para garantir persistência completa
     */
    @Transactional
    public Order createOrder(CheckoutRequest request) {

        // 🔐 Recupera email do usuário autenticado via JWT
        String email = SecurityUtils.getAuthenticatedEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter itens");
        }

        // 🚀 Criação do pedido
        Order order = new Order();
        order.setUserEmail(email);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // 🛒 Mapeamento dos itens
        List<OrderItem> items = request.getItems().stream().map(req -> {

            OrderItem item = new OrderItem();

            // 🔗 vínculo obrigatório com o pedido
            item.setOrder(order);

            item.setProductId(req.getProductId());
            item.setProductName(req.getProductName());
            item.setQuantity(req.getQuantity());
            item.setPrice(req.getPrice());
            item.setImageUrl(req.getImageUrl());

            return item;

        }).toList();

        // 🔗 Associa itens ao pedido
        order.setItems(items);

        // 💰 Cálculo do total estratégico
        BigDecimal total = items.stream()
                .map(item ->
                        item.getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        // 💾 Persistência completa (pedido + itens)
        return orderRepository.save(order);
    }

    /**
     * 🔍 Buscar pedido por ID
     */
    public Order findById(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id do pedido é obrigatório");
        }

        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    /**
     * 🔐 Buscar pedido por ID garantindo que pertence ao usuário (com itens).
     */
    public Order findByIdAndUserEmail(Long id, String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }

        return orderRepository
                .findByIdWithItemsAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    /**
     * 💳 Marcar pedido como pago
     */
    @Transactional
    public Order markAsPaid(Long id) {

        Order order = findById(id);

        order.setStatus(OrderStatus.PAID);

        return orderRepository.save(order);
    }
}