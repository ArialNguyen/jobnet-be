package org.jobnet.payment.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jobnet.payment.dtos.request.PaymentExecuteRequest;
import org.jobnet.payment.dtos.request.PaymentCreateRequest;
import org.jobnet.payment.services.IPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createPayment(@RequestBody @Valid PaymentCreateRequest request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("capture")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> capturePayment(@RequestHeader String userId, @RequestBody @Valid PaymentExecuteRequest request) {
        return paymentService.capturePayment(userId, request);
    }
}
