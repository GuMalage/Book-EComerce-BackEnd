package br.edu.utfpr.pb.pw44s.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_product")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    @Column(name = "url_image", length = 4000)
    private String urlImage;

    @Column(name = "long_description", length = 4000)
    private String longDescription;

    @Column(name = "autor_name")
    private String autorName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
