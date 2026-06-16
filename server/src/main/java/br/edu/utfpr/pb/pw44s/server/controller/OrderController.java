package br.edu.utfpr.pb.pw44s.server.controller;

import br.edu.utfpr.pb.pw44s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceRead;
import br.edu.utfpr.pb.pw44s.server.service.ICrudServiceWrite;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceRead;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceWrite;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    public static final Logger log =
            LoggerFactory.getLogger(ProductController.class);

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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return orderServiceRead.getAllOrders();
    }

    @Override
    public ResponseEntity<OrderResponseDTO> create(OrderDTO entity) {
        OrderDTO savedOrderDTO = orderServiceWrite.saveCompleteOrder(entity);
        OrderResponseDTO responseOrderResponseDTO = modelMapper.map(savedOrderDTO, OrderResponseDTO.class);

        return ResponseEntity.ok(responseOrderResponseDTO);
    }

    @Override
    @PutMapping("{id}")
    public ResponseEntity<OrderDTO> update(
            @PathVariable Long id,
            @RequestBody OrderDTO entity
    ) {

        log.info("Endpoint GET /products foi chamado");
        entity.setId(id);

        orderServiceWrite.updateOrder(entity);

        return ResponseEntity.ok(entity);
    }

    @PutMapping(value = "reciptUpload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public OrderDTO saveOrderRecipt(@RequestPart("order") @Valid OrderDTO entity, @RequestPart("image") @Valid MultipartFile file) {
        return orderServiceWrite.updateOrderReceipt(entity, file);
    }

    @GetMapping(value = "download/{id}")
    public  void downloadFile(@PathVariable("id") Long id, HttpServletResponse response) {
        orderServiceRead.downloadFile(id, response);
    }

}
