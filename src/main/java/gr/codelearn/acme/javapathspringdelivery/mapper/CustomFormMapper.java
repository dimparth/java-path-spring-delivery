package gr.codelearn.acme.javapathspringdelivery.mapper;

import gr.codelearn.acme.javapathspringdelivery.domain.PaymentMethod;
import gr.codelearn.acme.javapathspringdelivery.transfer.CheckoutOrderForm;
import gr.codelearn.acme.javapathspringdelivery.transfer.CreateOrderForm;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.OrderItemResource;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.OrderResource;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.ProductResource;
import gr.codelearn.acme.javapathspringdelivery.transfer.resource.UserResource;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.NoSuchElementException;

@Component
public class CustomFormMapper {
    public OrderResource mapCreateOrderFormToOrderResource(CreateOrderForm createOrderForm){
        var orderItems = new HashSet<OrderItemResource>();
        for (var product:createOrderForm.getProducts()
        ) {
            var pr = new ProductResource();
            pr.setName(product);
            pr.setStore(createOrderForm.getStoreName());
            var oir = new OrderItemResource();
            oir.setProduct(pr);
            orderItems.add(oir);
        }
        var ur = new UserResource();
        ur.setEmail(createOrderForm.getUserEmail());
        var or = new OrderResource();
        or.setUser(ur);
        or.setOrderItems(orderItems);
        or.setPaymentMethod(checkPaymentMethodValidity(createOrderForm.getPaymentMethod()));
        return or;
    }
    public OrderResource mapCheckoutOrderFormToOrderResource(CheckoutOrderForm checkoutOrderForm){
        var or = new OrderResource();
        var ur = new UserResource();
        ur.setEmail(checkoutOrderForm.getUserEmail());
        or.setUser(ur);
        return or;
    }
    private PaymentMethod checkPaymentMethodValidity(String paymentMethod){
        for (PaymentMethod value : PaymentMethod.values()) {
            if (value.name().equals(paymentMethod)) {
                return value;
            }
        }
        throw new NoSuchElementException(paymentMethod);
    }
}
