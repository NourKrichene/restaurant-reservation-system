package com.nour.restaurant_manager.adapter.out.id;

import com.nour.restaurant_manager.application.port.out.IdGenerator;

import java.util.UUID;

public class UuidIdGeneratorAdapter implements IdGenerator {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
