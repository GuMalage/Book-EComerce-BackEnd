package br.edu.utfpr.pb.pw44s.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private LocalDateTime dateOrder;
    private Long userId;
    private List<OrderItemDTO> itemsList;
    private UserDTO user;
    private BigDecimal totalPrice;
}
