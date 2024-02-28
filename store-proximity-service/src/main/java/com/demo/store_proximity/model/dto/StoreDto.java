package com.demo.store_proximity.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    private Long id;
    private String name;
    private double latitude;
    private double longitude;
}