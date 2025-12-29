package com.nour.reservation_manager.adapter.out.rest;

import com.nour.reservation_manager.adapter.out.rest.dto.RestaurantDto;
import com.nour.reservation_manager.adapter.out.rest.mapper.RestaurantRestMapper;
import com.nour.reservation_manager.application.port.out.RestaurantProvider;
import com.nour.reservation_manager.domain.model.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class RestaurantRestProvider implements RestaurantProvider {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantRestProvider.class);

    private final RestClient restClient;

    public RestaurantRestProvider(RestClient.Builder restClientBuilder, @Value("${restaurant.service.url}") String restaurantServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(restaurantServiceUrl).build();
    }

    @Override
    public Restaurant getRestaurantById(UUID restaurantId) {
        try {
            RestaurantDto dto = restClient.get().uri("/api/v1/restaurants/{id}", restaurantId).retrieve().onStatus(status -> status == HttpStatus.NOT_FOUND, (request, response) -> {
                logger.error("Restaurant with id {} not found - 404", restaurantId);
                throw new RestaurantNotFoundException("Restaurant with id " + restaurantId + " not found");
            }).body(RestaurantDto.class);

            if (dto == null) {
                logger.error("Restaurant with id {} not found - response body is null", restaurantId);
                throw new RestaurantNotFoundException("Restaurant with id " + restaurantId + " not found");
            }

            logger.info("Successfully fetched restaurant: {}", dto.id());
            return RestaurantRestMapper.toDomain(dto);

        } catch (RestaurantNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching restaurant with id {}: {}", restaurantId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch restaurant: " + e.getMessage(), e);
        }
    }
}
