package com.nour.restaurant_manager.usecase;

import com.nour.restaurant_manager.application.port.in.GetRestaurantUseCase;
import com.nour.restaurant_manager.application.usecase.GetRestaurantUseCaseImpl;
import com.nour.restaurant_manager.domain.model.Restaurant;
import com.nour.restaurant_manager.domain.model.WorkingTime;
import com.nour.restaurant_manager.usecase.fake.InMemoryRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;

class GetRestaurantUseCaseTest {

    private InMemoryRestaurantRepository repository;
    private GetRestaurantUseCase getRestaurantUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRestaurantRepository();
        getRestaurantUseCase = new GetRestaurantUseCaseImpl(repository);
    }

    @Test
    void shouldReturnRestaurantWhenItExists() {
        UUID id = UUID.randomUUID();
        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));
        Restaurant restaurant = new Restaurant(id, "La Storia Di Italia", "Authentic Italian Food", workingTimes, Collections.emptyList(), 50);
        repository.save(restaurant);

        var result = getRestaurantUseCase.getRestaurantById(id);

        assertThat(result).isPresent().contains(restaurant);
    }

    @Test
    void shouldReturnEmptyWhenRestaurantDoesNotExist() {
        UUID id = UUID.randomUUID();

        var result = getRestaurantUseCase.getRestaurantById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnAllRestaurants() {
        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));
        repository.save(new Restaurant(UUID.randomUUID(), "La Storia Di Italia", "Authentic Italian Food", workingTimes, Collections.emptyList(), 50));
        repository.save(new Restaurant(UUID.randomUUID(), "Sushi Saito", "Fresh sea food", workingTimes, Collections.emptyList(), 50));

        List<Restaurant> result = getRestaurantUseCase.getAllRestaurants(PageRequest.of(0, 10)).getContent();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Restaurant::name).containsExactlyInAnyOrder("La Storia Di Italia", "Sushi Saito");
        assertThat(result).extracting(Restaurant::description).containsExactlyInAnyOrder("Authentic Italian Food", "Fresh sea food");
    }
}
