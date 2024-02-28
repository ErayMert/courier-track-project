package com.demo.courier.repository;


import com.demo.courier.entity.CourierTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourierTrackRepository extends JpaRepository<CourierTrack, Long> {

    List<CourierTrack> findByCourierIdOrderByUpdatedDateAsc(Long courierId);
}