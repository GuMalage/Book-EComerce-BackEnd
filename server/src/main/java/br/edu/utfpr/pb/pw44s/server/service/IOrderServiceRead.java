package br.edu.utfpr.pb.pw44s.server.service;

import br.edu.utfpr.pb.pw44s.server.dto.response.OrderResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOrderServiceRead extends ICrudServiceRead<Order, Long>{
    ResponseEntity<List<OrderResponseDTO>> getOrdersByAuthenticatedUser();
    ResponseEntity<List<OrderResponseDTO>> getAllOrders();
    void downloadFile(Long id, HttpServletResponse response);
}
