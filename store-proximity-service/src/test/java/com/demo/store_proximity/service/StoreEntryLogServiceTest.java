package com.demo.store_proximity.service;

import com.demo.store_proximity.document.StoreEntryLog;
import com.demo.store_proximity.repository.StoreEntryLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreEntryLogServiceTest {

    @Mock
    private StoreEntryLogRepository storeEntryLogRepository;

    @InjectMocks
    private StoreEntryLogService storeEntryLogService;

    @Test
    void getLastEntryRecord_WhenRecordExists() {

        Long storeId = 1L;
        StoreEntryLog expectedLog = new StoreEntryLog();
        when(storeEntryLogRepository.findFirstByStoreIdOrderByPickupDateDesc(storeId))
            .thenReturn(Optional.of(expectedLog));

        Optional<StoreEntryLog> result = storeEntryLogService.getLastEntryRecord(storeId);

        assertTrue(result.isPresent());
        assertEquals(expectedLog, result.get());
        verify(storeEntryLogRepository).findFirstByStoreIdOrderByPickupDateDesc(storeId);
    }

    @Test
    void getLastEntryRecord_WhenRecordDoesNotExist() {
        Long storeId = 1L;
        when(storeEntryLogRepository.findFirstByStoreIdOrderByPickupDateDesc(storeId))
            .thenReturn(Optional.empty());

        Optional<StoreEntryLog> result = storeEntryLogService.getLastEntryRecord(storeId);

        assertFalse(result.isPresent());
        verify(storeEntryLogRepository).findFirstByStoreIdOrderByPickupDateDesc(storeId);
    }

    @Test
    void saveStoreEntryLog_Successful() {
        StoreEntryLog log = new StoreEntryLog();
        storeEntryLogService.saveStoreEntryLog(log);
        verify(storeEntryLogRepository).save(log);
    }
}