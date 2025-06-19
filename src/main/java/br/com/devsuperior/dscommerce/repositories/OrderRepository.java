package br.com.devsuperior.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devsuperior.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
