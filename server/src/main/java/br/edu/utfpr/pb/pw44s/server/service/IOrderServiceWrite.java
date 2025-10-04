package br.edu.utfpr.pb.pw44s.server.service;

import br.edu.utfpr.pb.pw44s.server.dto.OrderDTO;
import br.edu.utfpr.pb.pw44s.server.model.Order;

public interface IOrderServiceWrite extends ICrudServiceWrite<Order, Long>{
    OrderDTO saveCompleteOrder(OrderDTO orderDTO);
    void updateOrder(OrderDTO entity);
}
