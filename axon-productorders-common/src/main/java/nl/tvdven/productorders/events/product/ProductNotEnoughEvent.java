package nl.tvdven.productorders.events.product;


import nl.tvdven.productorders.domain.OrderId;

/**
 * Created by Edison Xu on 2017/3/15.
 */
public class ProductNotEnoughEvent {

    private OrderId orderId;
    private String productId;

    public ProductNotEnoughEvent() {
    }

    public ProductNotEnoughEvent(OrderId orderId, String productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }
}
