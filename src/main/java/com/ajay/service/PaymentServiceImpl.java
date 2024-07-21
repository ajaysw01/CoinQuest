package com.ajay.service;

import com.ajay.domain.PaymentMethod;
import com.ajay.domain.PaymentOrderStatus;
import com.ajay.model.PaymentOrder;
import com.ajay.model.User;
import com.ajay.repository.PaymentOrderRepository;
import com.ajay.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${stripe.api.secret}")
    private String apiSecretKey;


    @Override
    public PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(() -> new Exception("Payment order not found"));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getStatus()==null){
            paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        }

       if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
           if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
               RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecretKey);
               Payment payment = razorpay.payments.fetch(paymentId);

               Integer amount = payment.get("amount");
               String status = payment.get("status");

               if(status.equals("captured")){
                   paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                   paymentOrderRepository.save(paymentOrder); // Save payment order status

                   return true;
               }
               paymentOrder.setStatus(PaymentOrderStatus.FAILED);
               paymentOrderRepository.save(paymentOrder);

               return  false;
           }
           paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
           paymentOrderRepository.save(paymentOrder);
             return  true;
       }

        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) {

        Long Amount = amount*100;

        //logic for razorpay
        try {
            //Instantiate a razorpay client wiht your key id and ssecret
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecretKey);

            //create a json object with  the payment link request parameters
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");

            //create json object with cutomer details
            JSONObject customer = new JSONObject();
            customer.put("name",user.getFullname());

            customer.put("email",user.getEmail());
            paymentLinkRequest.put("customer",customer);

            //set reminder settings
            paymentLinkRequest.put("reminder_enable",true);

            //set the callback url and method
            paymentLinkRequest.put("callback_url","http://localhost:8080/wallet?order_id="+orderId);
            paymentLinkRequest.put("callback_method","get");

            //create the payment link using the paymentLink.create() method
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId =payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = new PaymentResponse();
            res.setPayment_url(paymentLinkUrl);

            return res;
        } catch (RazorpayException e) {
            System.out.println("Error creating payment link" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {

        Stripe.apiKey = stripSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:8080/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(amount*100)
                                        .setProductData(SessionCreateParams
                                                .LineItem
                                                .PriceData
                                                .ProductData
                                                .builder()
                                                .setName("Top up wallet")
                                                .build()
                                        ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);

        System.out.println("session ....."+ session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());


        return res;
    }
}
