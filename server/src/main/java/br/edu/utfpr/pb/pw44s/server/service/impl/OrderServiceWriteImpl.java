package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw44s.server.dto.OrderItemDTO;
import br.edu.utfpr.pb.pw44s.server.enums.OrderStatus;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import br.edu.utfpr.pb.pw44s.server.model.OrderItem;
import br.edu.utfpr.pb.pw44s.server.model.Product;
import br.edu.utfpr.pb.pw44s.server.repository.OrderItemRepository;
import br.edu.utfpr.pb.pw44s.server.repository.OrderRepository;
import br.edu.utfpr.pb.pw44s.server.service.AuthService;
import br.edu.utfpr.pb.pw44s.server.service.EmailService;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceWrite;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceWriteImpl extends CrudServiceWriteImpl<Order, Long> implements IOrderServiceWrite {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductServiceReadImpl productServiceRead;
    private final AuthService authService;
    @Autowired
    private EmailService emailService;
    private BigDecimal totalOrderPrice=BigDecimal.ZERO;

    public OrderServiceWriteImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, AuthService authService, ProductServiceReadImpl productServiceRead, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productServiceRead = productServiceRead;
        this.authService = authService;
        this.emailService = emailService;
    }

    @Override
    protected JpaRepository<Order, Long> getRepository() {
        return orderRepository;
    }


    @Override
    public OrderDTO saveCompleteOrder(OrderDTO entity) {
        Order order;
        if (entity.getId() != null) {
            order = orderRepository.findById(entity.getId())
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + entity.getId()));
        } else {
            order = new Order();
            order.setDateOrder(LocalDateTime.now());
            order.setUser(authService.getAuthenticatedUser());
            order.setShippingType(entity.getShippingType());
            order.setPaymentType(entity.getPaymentType());
            order.setAddressId(entity.getAddressId());
            order = orderRepository.save(order); // salva para gerar ID
        }

        BigDecimal totalOrderPrice = BigDecimal.ZERO;

        for (OrderItemDTO dtoItem : entity.getItemsList()) {
            Product product = productServiceRead.findOne(dtoItem.getProductId());

            OrderItem item = orderItemRepository.findByOrderIdAndProductId(order.getId(), product.getId())
                    .orElse(new OrderItem());

            item.setOrder(order);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setQuantity(dtoItem.getQuantity());
            item.setUrlImage(product.getUrlImage());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(dtoItem.getQuantity()));
            item.setTotalPriceItems(itemTotal);

            totalOrderPrice = totalOrderPrice.add(itemTotal);

            orderItemRepository.save(item);
        }

        order.setTotalPrice(totalOrderPrice);
        order.setOrderStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        entity.setId(order.getId());
        return entity;
    }


    @Override
    @Transactional
    public void updateOrder(OrderDTO entity) {

        Order order = orderRepository.findById(entity.getId())
                .orElseThrow(() -> new RuntimeException("Order não encontrado: " + entity.getId()));

        String oldStatus = (order.getOrderStatus() != null) ? order.getOrderStatus().getDescription() : "Não definido";

        if (entity.getOrderStatus() != null) {
            order.setOrderStatus(entity.getOrderStatus());
        }

        orderRepository.save(order);

        if (order.getUser() != null) {
            String username = order.getUser().getUsername();
            String email = order.getUser().getEmail();
            String newStatusName = (order.getOrderStatus() != null) ? order.getOrderStatus().getDescription() : "N/A";

            System.out.println("Enviando e-mail para o usuário: " + username);

            emailService.sendOrderStatusEmail(
                    email,
                    username,
                    newStatusName,
                    oldStatus,
                    email
            );
        } else {
            System.out.println("Aviso: O pedido ID " + order.getId() + " não possui um usuário vinculado. E-mail não enviado.");
        }
    }

}
