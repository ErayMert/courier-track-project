package com.demo.store_proximity.service;

import com.demo.store_proximity.document.StoreEntryLog;
import com.demo.store_proximity.repository.StoreEntryLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreEntryLogService {

    private final StoreEntryLogRepository storeEntryLogRepository;

    public Optional<StoreEntryLog> getLastEntryRecord(Long storeId) {
        return storeEntryLogRepository.findFirstByStoreIdOrderByPickupDateDesc(storeId);
    }

    public void saveStoreEntryLog(StoreEntryLog storeEntryLog) {

        log.info("Saved store log entry log");
        storeEntryLogRepository.save(storeEntryLog);
    }
}
