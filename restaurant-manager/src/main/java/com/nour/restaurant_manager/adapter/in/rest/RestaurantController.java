package com.nour.restaurant_manager.adapter.in.rest;

import com.nour.restaurant_manager.adapter.in.rest.dto.RestaurantRequestDto;
import com.nour.restaurant_manager.adapter.in.rest.dto.RestaurantResponseDto;
import com.nour.restaurant_manager.adapter.in.rest.mapper.RestaurantRestMapper;
import com.nour.restaurant_manager.application.port.in.CreateRestaurantUseCase;
import com.nour.restaurant_manager.application.port.in.DeleteRestaurantUseCase;
import com.nour.restaurant_manager.application.port.in.EditRestaurantUseCase;
import com.nour.restaurant_manager.application.port.in.GetRestaurantUseCase;
import com.nour.restaurant_manager.domain.model.Restaurant;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static com.nour.restaurant_manager.adapter.in.rest.mapper.RestaurantRestMapper.toDomain;
import static com.nour.restaurant_manager.adapter.in.rest.mapper.RestaurantRestMapper.toResponse;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantUseCase getRestaurantUseCase;
    private final EditRestaurantUseCase editRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase, GetRestaurantUseCase getRestaurantUseCase, EditRestaurantUseCase editRestaurantUseCase, DeleteRestaurantUseCase deleteRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.getRestaurantUseCase = getRestaurantUseCase;
        this.editRestaurantUseCase = editRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDto> addRestaurant(@Valid @RequestBody RestaurantRequestDto request) {
        Restaurant saved = createRestaurantUseCase.addRestaurant(toDomain(request));
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + saved.id())).body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> getAll(Pageable pageable) {
        Page<RestaurantResponseDto> result = getRestaurantUseCase.getAllRestaurants(pageable).map(RestaurantRestMapper::toResponse);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getById(@PathVariable UUID id) {
        return getRestaurantUseCase.getRestaurantById(id).map(restaurant -> ResponseEntity.ok(toResponse(restaurant))).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> update(@PathVariable UUID id, @Valid @RequestBody RestaurantRequestDto request) {
        Restaurant updated = toDomain(request);

        Restaurant saved = editRestaurantUseCase.updateRestaurant(id, updated);

        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = deleteRestaurantUseCase.deleteRestaurant(id);

        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
