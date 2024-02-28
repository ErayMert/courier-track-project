package com.demo.model.courier.dto;

import com.demo.model.courier.enums.CourierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierDto {

    private Long id;
    private String firstName;
    private String lastName;
    private CourierStatus status;
}
