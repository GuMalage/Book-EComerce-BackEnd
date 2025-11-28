package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw44s.server.dto.OrderItemDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import br.edu.utfpr.pb.pw44s.server.model.OrderItem;
import br.edu.utfpr.pb.pw44s.server.model.Product;
import br.edu.utfpr.pb.pw44s.server.repository.OrderItemRepository;
import br.edu.utfpr.pb.pw44s.server.service.IOrderItemServiceWrite;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderItemServiceWriteImpl extends CrudServiceWriteImpl<OrderItem, Long> implements IOrderItemServiceWrite {
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ProductServiceReadImpl productServiceRead;
    private final OrderServiceReadImpl orderServiceRead;
    private final OrderServiceWriteImpl orderServiceWrite;

    public OrderItemServiceWriteImpl(OrderItemRepository orderItemRepository, ModelMapper modelMapper, ProductServiceReadImpl productServiceRead, OrderServiceReadImpl orderServiceRead, OrderServiceWriteImpl orderServiceWrite) {
        this.orderItemRepository = orderItemRepository;
        this.modelMapper = modelMapper;
        this.productServiceRead = productServiceRead;
        this.orderServiceRead = orderServiceRead;
        this.orderServiceWrite = orderServiceWrite;
    }

    @Override
    protected JpaRepository<OrderItem, Long> getRepository() {
        return orderItemRepository;
    }

    public OrderItemResponseDTO saveOrderItemWhitTotalItemPrice(OrderItemDTO orderItemDTO) {
        Product product = productServiceRead.findOne(orderItemDTO.getProductId());
        Order order = orderServiceRead.findOne(orderItemDTO.getOrder().getId());

        OrderItem item;

        if (orderItemDTO.getId() != null) {
            item = orderItemRepository.findById(orderItemDTO.getId())
                    .orElseThrow(() -> new RuntimeException("OrderItem não encontrado: " + orderItemDTO.getId()));

            item.setQuantity(orderItemDTO.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setProduct(product);
            item.setUrlImage(product.getUrlImage());
            item.setOrder(order);
        } else {
            item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setUrlImage(product.getUrlImage());
            item.setQuantity(orderItemDTO.getQuantity());
        }

        BigDecimal totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()));
        item.setTotalPriceItems(totalPrice);

        OrderItem savedItem = orderItemRepository.save(item);

        orderServiceWrite.updateOrder(modelMapper.map(order, OrderDTO.class));

        return modelMapper.map(savedItem, OrderItemResponseDTO.class);
    }

}
