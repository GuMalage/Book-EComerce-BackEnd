package br.edu.utfpr.pb.pw44s.server.service;

import br.edu.utfpr.pb.pw44s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;
import org.springframework.web.multipart.MultipartFile;

public interface IOrderServiceWrite extends ICrudServiceWrite<Order, Long>{
    OrderDTO saveCompleteOrder(OrderDTO orderDTO);
    void updateOrder(OrderDTO entity);
    OrderDTO updateOrderReceipt(OrderDTO entity, MultipartFile file);
}
