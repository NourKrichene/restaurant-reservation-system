package com.nour.restaurant_manager.usecase;

import com.nour.restaurant_manager.application.port.in.EditRestaurantUseCase;
import com.nour.restaurant_manager.application.usecase.EditRestaurantUseCaseImpl;
import com.nour.restaurant_manager.domain.exception.RestaurantNotFoundException;
import com.nour.restaurant_manager.domain.model.Restaurant;
import com.nour.restaurant_manager.domain.model.WorkingTime;
import com.nour.restaurant_manager.usecase.fake.InMemoryRestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EditRestaurantUseCaseTest {

    private InMemoryRestaurantRepository repository;
    private EditRestaurantUseCase editRestaurantUseCase;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRestaurantRepository();
        editRestaurantUseCase = new EditRestaurantUseCaseImpl(repository);
    }

    @Test
    void shouldUpdateExistingRestaurant() {
        UUID id = UUID.randomUUID();
        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));
        repository.save(new Restaurant(id, "La Storia Di Italia", "Authentic Italian Food", workingTimes, Collections.emptyList(), 10));

        Restaurant updatedRestaurant = new Restaurant(id, "La Storia", "Tasty Italian Food", workingTimes, Collections.emptyList(), 10);

        Restaurant result = editRestaurantUseCase.updateRestaurant(id, updatedRestaurant);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("La Storia");
        assertThat(result.description()).isEqualTo("Tasty Italian Food");
    }

    @Test
    void shouldThrowExceptionWhenRestaurantDoesNotExist() {
        UUID id = UUID.randomUUID();
        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));

        Restaurant updatedRestaurant = new Restaurant(id, "La Storia Di Italia", "Authentic Italian Food", workingTimes, Collections.emptyList(), 10);

        assertThatThrownBy(() -> editRestaurantUseCase.updateRestaurant(id, updatedRestaurant)).isInstanceOf(RestaurantNotFoundException.class);
    }

    @Test
    void shouldPreserveOriginalIdEvenIfUpdatedDataContainsAnotherId() {
        UUID originalId = UUID.randomUUID();
        var workingTimes = List.of(new WorkingTime(1L, MONDAY, LocalTime.parse("12:00:00"), LocalTime.parse("14:30:00")), new WorkingTime(2L, MONDAY, LocalTime.parse("19:00:00"), LocalTime.parse("22:30:00")));

        repository.save(new Restaurant(originalId, "La Storia Di Italia", "Authentic Italian Food", workingTimes, Collections.emptyList(), 10));

        Restaurant updatedData = new Restaurant(UUID.randomUUID(), "La Storia", "Tasty Italian Food", workingTimes, Collections.emptyList(), 10);

        Restaurant result = editRestaurantUseCase.updateRestaurant(originalId, updatedData);

        assertThat(result.id()).isEqualTo(originalId);
    }
}
