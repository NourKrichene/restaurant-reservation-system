package com.nour.restaurant_manager.adapter.config;

import com.nour.restaurant_manager.adapter.out.id.UuidIdGeneratorAdapter;
import com.nour.restaurant_manager.adapter.out.persistence.jpa.RestaurantRepositoryAdapter;
import com.nour.restaurant_manager.adapter.out.persistence.jpa.SpringDataRestaurantRepository;
import com.nour.restaurant_manager.application.port.in.CreateRestaurantUseCase;
import com.nour.restaurant_manager.application.port.in.DeleteRestaurantUseCase;
import com.nour.restaurant_manager.application.port.in.EditRestaurantUseCase;
import com.nour.restaurant_manager.application.port.in.GetRestaurantUseCase;
import com.nour.restaurant_manager.application.port.out.IdGenerator;
import com.nour.restaurant_manager.application.port.out.RestaurantRepository;
import com.nour.restaurant_manager.application.usecase.CreateRestaurantUseCaseImpl;
import com.nour.restaurant_manager.application.usecase.DeleteRestaurantUseCaseImpl;
import com.nour.restaurant_manager.application.usecase.EditRestaurantUseCaseImpl;
import com.nour.restaurant_manager.application.usecase.GetRestaurantUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new UuidIdGeneratorAdapter();
    }

    @Bean
    public RestaurantRepository restaurantJpaRepository(SpringDataRestaurantRepository jpaRepository) {
        return new RestaurantRepositoryAdapter(jpaRepository);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new CreateRestaurantUseCaseImpl(restaurantRepository, idGenerator());
    }

    @Bean
    public GetRestaurantUseCase getRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new GetRestaurantUseCaseImpl(restaurantRepository);
    }

    @Bean
    public EditRestaurantUseCase editRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new EditRestaurantUseCaseImpl(restaurantRepository);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantRepository restaurantRepository) {
        return new DeleteRestaurantUseCaseImpl(restaurantRepository);
    }
}
