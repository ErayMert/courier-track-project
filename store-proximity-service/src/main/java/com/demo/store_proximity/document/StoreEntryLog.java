package com.demo.store_proximity.document;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("store_entry_logs")
@CompoundIndex(name = "storeId_pickupDate_idx", def = "{'storeId' : 1, 'pickupDate' : -1}")
public class StoreEntryLog {

    @Id
    private String id;

    private Long courierId;

    private Long storeId;

    private Long orderId;

    private LocalDateTime pickupDate;

}