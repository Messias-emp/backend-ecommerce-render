package com.ecommerce.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ecommerce.products.Product;
import com.ecommerce.products.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        // 🔐 Proteção contra duplicação
        if (productRepository.count() > 0) {
            return; // Já existem produtos → não recria
        }

        // 🧱 Produtos fake (dados iniciais)
        Product p1 = new Product();
        p1.setName("Notebook Gamer");
        p1.setDescription("Notebook com RTX e alto desempenho");
        p1.setPrice(new BigDecimal("5999.90"));
        p1.setStock(10);
        p1.setImageUrl("assets/images/products/notebook-02-legion-5.jpg");
        p1.setActive(true);

        Product p2 = new Product();
        p2.setName("Mouse Sem Fio Logitech");
        p2.setDescription("Mouse ergonômico com conexão sem fio e alta precisão");
        p2.setPrice(new BigDecimal("149.90"));
        p2.setStock(50);
        p2.setImageUrl("assets/images/products/pc-07-black-rgb.jpg");
        p2.setActive(true);

        Product p3 = new Product();
        p3.setName("Teclado Mecânico RGB");
        p3.setDescription("Teclado mecânico com switches blue e iluminação RGB");
        p3.setPrice(new BigDecimal("399.90"));
        p3.setStock(30);
        p3.setImageUrl("assets/images/products/pc-02-rtx4070.jpg");
        p3.setActive(true);

        Product p4 = new Product();
        p4.setName("Headset Gamer Surround");
        p4.setDescription("Headset com som surround 7.1 e microfone removível");
        p4.setPrice(new BigDecimal("299.90"));
        p4.setStock(25);
        p4.setImageUrl("assets/images/products/pc-05-4070super.jpg");
        p4.setActive(true);

        Product p5 = new Product();
        p5.setName("Monitor Gamer 27\" 144Hz");
        p5.setDescription("Monitor Full HD com 144Hz e 1ms de resposta");
        p5.setPrice(new BigDecimal("1299.90"));
        p5.setStock(15);
        p5.setImageUrl("assets/images/products/pc-06-white-rgb.jpg");
        p5.setActive(true);



       

        // 🚀 Persistência em lote
        productRepository.saveAll(
                List.of(p1, p2, p3, p4, p5)
        );

        System.out.println("✅ Produtos fake carregados com sucesso");
    }
}
