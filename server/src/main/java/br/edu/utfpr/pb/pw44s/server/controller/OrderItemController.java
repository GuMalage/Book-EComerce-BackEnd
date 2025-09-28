package br.edu.utfpr.pb.pw44s.server.controller;

import br.edu.utfpr.pb.pw44s.server.dto.OrderItemDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.OrderItem;
import br.edu.utfpr.pb.pw44s.server.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orderItem")
public class OrderItemController extends CrudController <OrderItem, OrderItemDTO, OrderItemResponseDTO, Long>{
    private final IOrderItemServiceWrite orderItemServiceWrite;
    private final IOrderItemServiceRead orderItemServiceRead;
    private final ModelMapper modelMapper;

    public OrderItemController(IOrderItemServiceWrite orderItemServiceWrite, IOrderItemServiceRead orderItemServiceRead, ModelMapper modelMapper){
        super(OrderItem.class, OrderItemDTO.class, OrderItemResponseDTO.class);
        this.orderItemServiceWrite = orderItemServiceWrite;
        this.orderItemServiceRead = orderItemServiceRead;
        this.modelMapper = modelMapper;
    }

    @Override
    protected ICrudServiceWrite<OrderItem, Long> getWriteService() {
        return this.orderItemServiceWrite;
    }

    @Override
    protected ICrudServiceRead<OrderItem, Long> getReadService() {
        return this.orderItemServiceRead;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @Override
    public ResponseEntity<List<OrderItemResponseDTO>> findAll() {
        return orderItemServiceRead.returnOrderItemForOdersByAuthUser();
    }

    @Override
    public ResponseEntity<OrderItemResponseDTO> create(OrderItemDTO entity) {
        OrderItemResponseDTO savedOrderItemDTO = orderItemServiceWrite.saveOrderItemWhitTotalItemPrice(entity);

        return ResponseEntity.ok(savedOrderItemDTO);
    }

}
