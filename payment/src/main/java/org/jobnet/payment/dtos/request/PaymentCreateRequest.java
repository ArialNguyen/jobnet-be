package org.jobnet.payment.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentCreateRequest {

    @NotNull(message = "Total is required.")
    private Double total;

    @NotBlank(message = "Current must not be blank.")
    private String currency;

    @NotBlank(message = "Description must not be blank.")
    private String description;
}
