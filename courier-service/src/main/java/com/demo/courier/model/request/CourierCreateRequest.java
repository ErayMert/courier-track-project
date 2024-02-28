package com.demo.courier.model.request;

import com.demo.courier.validation.IdentityNo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierCreateRequest {

    @IdentityNo
    @NotNull(message = "Identity number cannot be null")
    private String identityNo;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

}