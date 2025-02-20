package org.jobnet.payment.services;

import org.jobnet.payment.dtos.request.PaymentCreateRequest;
import org.jobnet.payment.dtos.request.PaymentExecuteRequest;

import java.util.Map;

public interface IPaymentService {
    Map<String, String> createPayment(PaymentCreateRequest request);

    Map<String, String> capturePayment(String userId, PaymentExecuteRequest request);
}
