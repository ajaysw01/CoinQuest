package com.ajay.service;

import com.ajay.domain.PaymentMethod;
import com.ajay.model.PaymentOrder;
import com.ajay.model.User;
import com.ajay.repository.PaymentOrderRepository;
import com.ajay.response.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripSecretKey;

    @Value("${razorpay.api.secret}")
    private String apiKey;

    @Value("${stripe.api.key}")
    private String apiSecretKey;


    @Override
    public PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod) {
        return null;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {
        return null;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) {
        return null;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLin(User user, Long amount) {
        return null;
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) {
        return null;
    }
}
