package com.demo.courier.repository;

import com.demo.courier.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepository extends JpaRepository<Courier, Long> {

    boolean existsByIdentityNo(String identityNo);
}