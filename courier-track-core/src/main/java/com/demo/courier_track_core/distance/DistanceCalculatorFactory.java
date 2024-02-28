package com.demo.courier_track_core.distance;


import com.demo.courier_track_core.enums.DistanceType;

import java.util.Map;
import java.util.Objects;

public class DistanceCalculatorFactory {
    private static volatile DistanceCalculatorFactory instance;
    private static final Map<DistanceType, DistanceStrategy> STRATEGY_MAP = Map.of(
            DistanceType.HAVERSINE, new HaversineStrategy()
    );

    private DistanceCalculatorFactory() {
    }

    public static DistanceCalculatorFactory getInstance() {
        if (instance == null) {
            synchronized (DistanceCalculatorFactory.class) {
                if (instance == null) {
                    instance = new DistanceCalculatorFactory();
                }
            }
        }
        return instance;
    }

    public DistanceStrategy getCalculator(DistanceType type) {
        DistanceStrategy strategy = STRATEGY_MAP.get(Objects.requireNonNull(type));
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown Distance Type");
        }
        return strategy;
    }
}
