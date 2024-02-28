package com.demo.order.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotEmpty
    private Long storeId;

    @NotEmpty
    private Double latitude;

    @NotEmpty
    private Double longitude;
}
