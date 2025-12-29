package com.nour.reservation_manager.adapter.out.id;

import com.fasterxml.uuid.Generators;
import com.nour.reservation_manager.application.port.out.IdGenerator;

import java.util.UUID;

public class UuidIdGeneratorAdapter implements IdGenerator {
    @Override
    public UUID generate() {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }
}
