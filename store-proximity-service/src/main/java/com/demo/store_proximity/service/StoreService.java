package com.demo.store_proximity.service;

import com.demo.store_proximity.model.dto.StoreDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class StoreService {

    private final ResourceLoader resourceLoader;

    private List<StoreDto> storeDtoList;


    public StoreService(@Qualifier("webApplicationContext") ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void loadStoreData() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        var resource = resourceLoader.getResource("classpath:stores.json");
        storeDtoList = mapper.readValue(resource.getInputStream(), new TypeReference<>(){});
    }

    public List<StoreDto> getAllStores(){
        return storeDtoList;
    }
}