package com.ajay.controller;

import com.ajay.domain.PaymentMethod;
import com.ajay.model.PaymentOrder;
import com.ajay.model.User;
import com.ajay.response.PaymentResponse;
import com.ajay.service.PaymentService;
import com.ajay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long amount,
            @PathVariable PaymentMethod paymentMethod
            ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentResponse paymentResponse;

        PaymentOrder order = paymentService.createPaymentOrder(user, amount, paymentMethod);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponse = paymentService.createRazorpayPaymentLink(user, amount,order.getId());

        }else {
            paymentResponse= paymentService.createStripePaymentLink(user, amount,order.getId());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }




}
