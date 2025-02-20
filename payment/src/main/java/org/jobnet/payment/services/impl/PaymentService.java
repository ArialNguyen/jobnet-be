package org.jobnet.payment.services.impl;

import com.jobnet.clients.user.UserClient;
import com.jobnet.clients.user.dtos.requests.UpgradeRequest;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobnet.payment.configs.PaypalConfig;
import org.jobnet.payment.dtos.request.PaymentCreateRequest;
import org.jobnet.payment.dtos.request.PaymentExecuteRequest;
import org.jobnet.payment.services.IPaymentService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements IPaymentService {

    private final PayPalHttpClient payPalHttpClient;
    private final PaypalConfig paypalConfig;
    private final UserClient userClient;

    @Override
    public Map<String, String> createPayment(PaymentCreateRequest request) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown()
                .currencyCode(request.getCurrency())
                .value(request.getTotal().toString());
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountBreakdown);
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(paypalConfig.getSuccessUrl())
                .cancelUrl(paypalConfig.getCancelUrl());
        orderRequest.applicationContext(applicationContext);
        OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            HttpResponse<Order> orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
            Order order = orderHttpResponse.result();

            String url = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .href();
            return Map.of("id", order.id(),"url", url);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create payment", e);
        }
    }

    @Override
    public Map<String, String> capturePayment(String userId, PaymentExecuteRequest request) {
        userClient.upgradeUser(new UpgradeRequest(userId, UpgradeRequest.Action.UPGRADE_USER.name()));

        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(request.getToken());
        try {
            HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            return Map.of("token", Objects.nonNull(httpResponse.result().status())
                    ? request.getToken()
                    : "");
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute payment", e);
        }
    }
}
