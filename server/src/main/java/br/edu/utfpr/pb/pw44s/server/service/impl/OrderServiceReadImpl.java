package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.*;
import br.edu.utfpr.pb.pw44s.server.repository.OrderItemRepository;
import br.edu.utfpr.pb.pw44s.server.repository.OrderRepository;
import br.edu.utfpr.pb.pw44s.server.service.AuthService;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceRead;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceReadImpl extends CrudServiceReadImpl<Order, Long> implements IOrderServiceRead {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceReadImpl(OrderRepository orderRepository, ModelMapper modelMapper, AuthService authService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }

    @Override
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByAuthenticatedUser() {
        User currentUser = authService.getAuthenticatedUser();

        List<Order> userOrders = orderRepository.findByUser(currentUser);

        List<OrderResponseDTO> responseOrders = userOrders.stream()
                .map(order -> {
                    OrderResponseDTO orderResponseDTO = modelMapper.map(order, OrderResponseDTO.class);

                    List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(order.getId());

                    List<OrderItemResponseDTO> orderItemDTOs = orderItems.stream()
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

                    orderResponseDTO.setItemsList(orderItemDTOs);
                    return orderResponseDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseOrders);
    }
}
