package tutorial.pizzeria.domain;

public enum OrderStatus {

    PENDING,
    PAYING,
    EXPIRED,
    PROCESSING_FOR_SHIPPING,
    SHIPPING,
    DELIVERED,
    CANCELLED,
    REFUNDED
}
