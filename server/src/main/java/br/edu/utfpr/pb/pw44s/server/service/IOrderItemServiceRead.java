package br.edu.utfpr.pb.pw44s.server.service;

import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.OrderItem;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOrderItemServiceRead extends ICrudServiceRead<OrderItem, Long>{
    ResponseEntity<List<OrderItemResponseDTO>> returnOrderItemForOdersByAuthUser();
}
