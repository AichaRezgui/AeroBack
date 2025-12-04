package com.Aero.Beauty.Services;

import com.Aero.Beauty.Entities.Order;
import com.Aero.Beauty.Entities.OrderItem;
import com.Aero.Beauty.Entities.Product;
import com.Aero.Beauty.Repositories.OrderRepository;
import com.Aero.Beauty.Repositories.ProductRepository;
import com.Aero.Beauty.dto.OrderDTO;
import com.Aero.Beauty.dto.OrderItemDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order saveOrder(Order order) {

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {

                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Produit introuvable avec l'ID : " + item.getProduct().getId()
                        ));
                System.out.println("le produit est :"+product);
                if (product.getStock() < item.getQuantity()) {
                    throw new RuntimeException("Stock insuffisant pour le produit : " + product.getName());
                }
                System.out.println("le stock de produit est :"+product.getStock());
                product.setStock(product.getStock() - item.getQuantity());
                productRepository.save(product);

                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setDate(order.getDate());
        dto.setShippingAddress(order.getShippingAddress());

        List<OrderItemDTO> itemsDto = order.getItems().stream()
                .map(i -> new OrderItemDTO(i.getProduct().getId(), i.getQuantity(), i.getPrice()))
                .collect(Collectors.toList());
        dto.setItems(itemsDto);

        return dto;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
