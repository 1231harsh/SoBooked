package com.hvrc.bookStore.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public PaymentIntent createPaymentIntent(Double amount, String currency) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.longValue())
                .setCurrency(currency)
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                .build();
        return PaymentIntent.create(params);
    }

    public void confirmPayment(String paymentId) throws Exception {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);
        if ("requires_capture".equals(paymentIntent.getStatus())) {
            paymentIntent.capture();
        } else if ("succeeded".equals(paymentIntent.getStatus())) {
            System.out.println("Payment already succeeded. No capture needed.");
        } else {
            throw new Exception("Payment is in an unexpected state: " + paymentIntent.getStatus());
        }
    }
}
