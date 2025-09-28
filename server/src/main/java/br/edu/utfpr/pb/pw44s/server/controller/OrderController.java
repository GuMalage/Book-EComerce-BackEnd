package br.edu.utfpr.pb.pw44s.server.controller;

import br.edu.utfpr.pb.pw44s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceRead;
import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceWrite;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceRead;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceWrite;
import org.modelmapper.ModelMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController extends CrudController<Order, OrderDTO, OrderResponseDTO, Long>{
    private final IOrderServiceWrite orderServiceWrite;
    private final IOrderServiceRead orderServiceRead;
    private final ModelMapper modelMapper;

    public OrderController(IOrderServiceWrite orderServiceWrite, IOrderServiceRead orderServiceRead, ModelMapper modelMapper) {
        super(Order.class, OrderDTO.class, OrderResponseDTO.class);
        this.orderServiceWrite = orderServiceWrite;
        this.orderServiceRead = orderServiceRead;
        this.modelMapper = modelMapper;
    }

    @Override
    protected ICrudServiceWrite<Order, Long> getWriteService() {
        return this.orderServiceWrite;
    }

    @Override
    protected ICrudServiceRead<Order, Long> getReadService() {
        return this.orderServiceRead;
    }

    @Override
    protected ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    @Override
    public ResponseEntity<List<OrderResponseDTO>> findAll() {
        return orderServiceRead.getOrdersByAuthenticatedUser();
    }

    @Override
    public ResponseEntity<OrderResponseDTO> create(OrderDTO entity) {
        OrderDTO savedOrderDTO = orderServiceWrite.SaveCompleteOrder(entity);
        OrderResponseDTO responseOrderResponseDTO = modelMapper.map(savedOrderDTO, OrderResponseDTO.class);

        return ResponseEntity.ok(responseOrderResponseDTO);
    }

}
