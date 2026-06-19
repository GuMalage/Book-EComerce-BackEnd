package br.edu.utfpr.pb.pw44s.server.service.impl;

import br.edu.utfpr.pb.pw44s.server.dto.UserDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderItemResponseDTO;
import br.edu.utfpr.pb.pw44s.server.dto.response.OrderResponseDTO;
import br.edu.utfpr.pb.pw44s.server.minio.service.MinioService;
import br.edu.utfpr.pb.pw44s.server.model.*;
import br.edu.utfpr.pb.pw44s.server.repository.OrderItemRepository;
import br.edu.utfpr.pb.pw44s.server.repository.OrderRepository;
import br.edu.utfpr.pb.pw44s.server.service.AuthService;
import br.edu.utfpr.pb.pw44s.server.service.IOrderServiceRead;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import static br.edu.utfpr.pb.pw44s.server.controller.OrderController.log;


@Service
public class OrderServiceReadImpl extends CrudServiceReadImpl<Order, Long> implements IOrderServiceRead {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;
    private final OrderItemRepository orderItemRepository;
    private final MinioService minioService;

    public OrderServiceReadImpl(OrderRepository orderRepository, ModelMapper modelMapper, AuthService authService, OrderItemRepository orderItemRepository,MinioService minioService) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
        this.orderItemRepository = orderItemRepository;
        this.minioService = minioService;
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
                                itemDTO.setUrlImage(item.getUrlImage());
                                return itemDTO;
                            })
                            .collect(Collectors.toList());

                    orderResponseDTO.setItemsList(orderItemDTOs);
                    return orderResponseDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseOrders);
    }

    @Override
      public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {

        List<Order> allOrders = orderRepository.findAll();

        List<OrderResponseDTO> responseList = allOrders.stream()
                .map(order -> {
                    OrderResponseDTO orderResponseDTO = modelMapper.map(order, OrderResponseDTO.class);
                    if (order.getUser() != null) {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setUsername(order.getUser().getUsername());
                        orderResponseDTO.setUser(userDTO);
                    }

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
                                itemDTO.setUrlImage(item.getUrlImage());
                                return itemDTO;
                            })
                            .collect(Collectors.toList());

                    orderResponseDTO.setItemsList(orderItemDTOs);

                    return orderResponseDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Override
    public void downloadFile(Long id, HttpServletResponse response) {
        Order order = this.findOne(id);

        if (order == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Pedido não encontrado"
            );
        }

        String imageName = order.getImageName();
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Este pedido não possui imagem ou anexo cadastrado"
            );
        }

        try (InputStream in = minioService.downloadObject("commons", imageName)) {

            String contentType = order.getContentType();
            if (contentType == null || contentType.trim().isEmpty()) {
                contentType = "application/octet-stream"; // fallback seguro se estiver nulo
            }
            response.setContentType(contentType);

            String disposition = "inline; filename=\"" + URLEncoder.encode(imageName, "UTF-8") + "\"";
            response.setHeader("Content-Disposition", disposition);

            response.setCharacterEncoding("UTF-8");

            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao baixar arquivo do MinIO", e
            );
        }
    }

}
