package com.demo.store_proximity.repository;



import com.demo.store_proximity.document.StoreEntryLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoreEntryLogRepository extends MongoRepository<StoreEntryLog, Long> {
    Optional<StoreEntryLog> findFirstByStoreIdOrderByPickupDateDesc(Long storeId);

}
