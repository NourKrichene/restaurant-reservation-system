package com.nour.restaurant_manager.application.port.out;

import java.util.UUID;

public interface IdGenerator {
    UUID generate();
}