package retail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class OrderBuilder {
    private List<Product> items;
    private CreditCardDetails creditCardDetails;
    private Address billingAddress;
    private Address shippingAddress;
    private Courier courier;
    private BigDecimal discount = BigDecimal.ZERO;
    private Boolean giftWrap = false;

    public OrderBuilder setItems(List<Product> items) {
        if (this.items == null) {
            this.items = items;
        } else {
            this.items.addAll(items);
        }

        return this;
    }

    public OrderBuilder setAdditionalItem(Product item) {
        if(items == null){
            items = new ArrayList<>();
        }
        items.add(item);
        return this;
    }

    public OrderBuilder setCreditCardDetails(CreditCardDetails creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
        return this;
    }

    public OrderBuilder setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public OrderBuilder setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public OrderBuilder setCourier(Courier courier) {
        this.courier = courier;
        return this;
    }

    public OrderBuilder setDiscount(BigDecimal discount){
        this.discount = discount;
        return this;
    }

    public OrderBuilder setGiftWrap(boolean giftWrap){
        this.giftWrap = giftWrap;
        return this;
    }

    public Order createOrder() {

        if(this.shippingAddress == null){
            this.shippingAddress = this.billingAddress;
        }

        if (this.items == null ||
                this.creditCardDetails == null ||
                this.billingAddress == null ||
                this.courier == null) {
            throw new IllegalArgumentException("Not all required values given");
        }

        if(items.size()>3){
            if(giftWrap ==true ){
                throw new IllegalArgumentException("Bulk orders (size>3) cannot be gift wrapped");
            }
            return new BulkOrder(items, creditCardDetails, billingAddress, shippingAddress, courier, discount);
        }
        if(discount != BigDecimal.ZERO){
            throw new IllegalArgumentException("Discount can only be applied to bulk orders (size>3)");
        }

        return new SmallOrder(items, creditCardDetails, billingAddress, shippingAddress, courier, giftWrap);
    }
}