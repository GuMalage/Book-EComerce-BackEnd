package br.edu.utfpr.pb.pw44s.server.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_orderItem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    private int quantity;

    private BigDecimal totalPriceItems;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    @Column(length = 4000)
    private String urlImage;
}
