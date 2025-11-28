package br.edu.utfpr.pb.pw44s.server.dto.response;

import br.edu.utfpr.pb.pw44s.server.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime dateOrder;
    private UserDTO user;
    private List<OrderItemResponseDTO> itemsList = new ArrayList<>();
    private BigDecimal totalPrice;
    private String paymentType;
    private String shippingType;
    private Long addressId;
    private BigDecimal shipping;
    private String urlImage;
}
