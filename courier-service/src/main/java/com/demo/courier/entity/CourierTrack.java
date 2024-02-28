package com.demo.courier.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courier_tracks", indexes = {
        @Index(name = "idx_courier_track_on_courier_id_and_updated_date", columnList = "courierId, updatedDate")
})
public class CourierTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long courierId;

    private Double latitude;

    private Double longitude;

    private LocalDateTime updatedDate;
}