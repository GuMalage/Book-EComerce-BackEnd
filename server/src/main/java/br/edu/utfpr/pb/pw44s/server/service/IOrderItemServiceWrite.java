package br.edu.utfpr.pb.pw44s.server.service;

import br.edu.utfpr.pb.pw44s.server.dto.OrderItemDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.OrderItem;

public interface IOrderItemServiceWrite extends ICrudServiceWrite<OrderItem, Long> {
    OrderItemResponseDTO saveOrderItemWhitTotalItemPrice(OrderItemDTO orderItemDTO);
}
