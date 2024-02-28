package com.demo.courier.entity;

import com.demo.model.courier.enums.CourierStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "couriers")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String identityNo;

    @Enumerated(EnumType.STRING)
    private CourierStatus status;
}