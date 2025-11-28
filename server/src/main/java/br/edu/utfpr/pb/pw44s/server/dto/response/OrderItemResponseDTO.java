package br.edu.utfpr.pb.pw44s.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private int quantity;
    private long productId;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal totalPriceItems;
    private String urlImage;
}
