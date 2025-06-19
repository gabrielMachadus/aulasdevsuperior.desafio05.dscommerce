package br.com.devsuperior.dscommerce.entities;

import br.com.devsuperior.dscommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_order")
@EqualsAndHashCode
public class Order {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant moment;

    @Setter
    @Getter
    private OrderStatus status;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @Setter
    @Getter
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @Getter
    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();

    public List<Product> getProducts(){
        return items.stream().map(OrderItem::getProduct).toList();
    }

    public Order() {
    }

    public Order(Long id, Instant moment, OrderStatus status, User client, Payment payment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
