package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import br.edu.utfpr.pb.pw44s.server.model.OrderItem;
import br.edu.utfpr.pb.pw44s.server.model.User;
import br.edu.utfpr.pb.pw44s.server.repository.OrderItemRepository;
import br.edu.utfpr.pb.pw44s.server.repository.OrderRepository;
import br.edu.utfpr.pb.pw44s.server.service.AuthService;
import br.edu.utfpr.pb.pw44s.server.service.IOrderItemServiceRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceReadImpl extends CrudServiceReadImpl<OrderItem, Long> implements IOrderItemServiceRead {
    private final OrderItemRepository orderItemRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    public OrderItemServiceReadImpl(OrderItemRepository orderItemRepository, AuthService authService, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.authService = authService;
        this.orderRepository = orderRepository;
    }

    @Override
    protected JpaRepository<OrderItem, Long> getRepository() {return orderItemRepository;}

    @Override
    public ResponseEntity<List<OrderItemResponseDTO>> returnOrderItemForOdersByAuthUser(){
        User currentUser = authService.getAuthenticatedUser();

        List<Order> userOrders = orderRepository.findByUser(currentUser);

        if (userOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<OrderItem> orderItems = userOrders.stream()
                .flatMap(order -> orderItemRepository.findByOrder_Id(order.getId()).stream())
                .collect(Collectors.toList());

        List<OrderItemResponseDTO> responseOrderItems = orderItems.stream()
                .map(item -> {
                    OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                    itemDTO.setId(item.getId());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductPrice(item.getUnitPrice());
                    itemDTO.setTotalPriceItems(item.getTotalPriceItems());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseOrderItems);
    }

}
