package com.ajay.service;

import com.ajay.domain.PaymentMethod;
import com.ajay.model.PaymentOrder;
import com.ajay.model.User;
import com.ajay.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user,
                                    Long amount,
                                    PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId);

    PaymentResponse createStripePaymentLink(User user, Long amount,Long orderId) throws StripeException;





}


