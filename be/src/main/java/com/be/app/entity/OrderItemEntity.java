package com.be.app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    @JsonProperty("order_id")
    private Long orderID;

    @Column(name = "uuid", nullable = false, length = 64)
    @JsonProperty("uuid")
    private String uuid;

    @Column(name = "product_id", nullable = false, length = 20)
    @JsonProperty("product_id")
    private Long productID;

    @Column(name = "quantity", length = 4)
    @JsonProperty("quantity")
    private int quantity;

    @Column(name = "total_price", nullable = false, length = 11)
    @JsonProperty("total_price")
    private double totalPrice;

    @Column(name = "price", nullable = false, length = 11)
    @JsonProperty("price")
    private double price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductEntity product;
}
