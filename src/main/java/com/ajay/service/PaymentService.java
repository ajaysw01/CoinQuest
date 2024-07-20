package com.ajay.service;

import com.ajay.domain.PaymentMethod;
import com.ajay.model.PaymentOrder;
import com.ajay.model.User;
import com.ajay.response.PaymentResponse;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user,
                                    Long amount,
                                    PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id);

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId);

    PaymentResponse createRazorpayPaymentLin(User user, Long amount);

    PaymentResponse createStripePaymentLink(User user, Long amount,Long orderId);





}


