package com.ecommerce.order.repository;

import com.ecommerce.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de pedidos.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Pedidos de um usuário COM itens (evita LazyInitialization).
     */
    @Query("""
           SELECT DISTINCT o
           FROM Order o
           LEFT JOIN FETCH o.items
           WHERE o.userEmail = :email
           """)
    List<Order> findByUserEmailWithItems(@Param("email") String email);

    /**
     * Buscar todos os pedidos de um usuário.
     */
    List<Order> findByUserEmail(String userEmail);

    /**
     * Buscar pedido por id garantindo que pertence ao usuário.
     */
    Optional<Order> findByIdAndUserEmail(Long id, String userEmail);

    /**
     * Buscar pedido com seus itens.
     */
    @Query("""
           SELECT o
           FROM Order o
           LEFT JOIN FETCH o.items
           WHERE o.id = :id
           """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    /**
     * Buscar pedido por id + email já carregando itens.
     */
    @Query("""
           SELECT o
           FROM Order o
           LEFT JOIN FETCH o.items
           WHERE o.id = :id
           AND o.userEmail = :email
           """)
    Optional<Order> findByIdWithItemsAndUserEmail(@Param("id") Long id,
                                                  @Param("email") String email);

    /**
     * Receita total da loja.
     */
    @Query("SELECT COALESCE(SUM(o.total),0) FROM Order o")
    BigDecimal getTotalRevenue();
}
