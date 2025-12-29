package com.nour.reservation_manager.adapter.in.rest;

import com.nour.reservation_manager.adapter.in.rest.dto.ReservationRequestDto;
import com.nour.reservation_manager.adapter.in.rest.dto.ReservationResponseDto;
import com.nour.reservation_manager.adapter.in.rest.mapper.ReservationRestMapper;
import com.nour.reservation_manager.application.port.in.CreateReservationUseCase;
import com.nour.reservation_manager.application.port.in.EditReservationUseCase;
import com.nour.reservation_manager.application.port.in.GetReservationByCustomerUseCase;
import com.nour.reservation_manager.application.port.in.GetReservationUseCase;
import com.nour.reservation_manager.domain.model.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.nour.reservation_manager.adapter.in.rest.mapper.ReservationRestMapper.toDomain;
import static com.nour.reservation_manager.adapter.in.rest.mapper.ReservationRestMapper.toResponse;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final GetReservationUseCase getReservationUseCase;
    private final GetReservationByCustomerUseCase getReservationByCustomerUseCase;
    private final EditReservationUseCase editReservationUseCase;

    public ReservationController(CreateReservationUseCase createReservationUseCase,
            GetReservationUseCase getReservationUseCase,
            GetReservationByCustomerUseCase getReservationByCustomerUseCase,
            EditReservationUseCase editReservationUseCase) {
        this.createReservationUseCase = createReservationUseCase;
        this.getReservationUseCase = getReservationUseCase;
        this.getReservationByCustomerUseCase = getReservationByCustomerUseCase;
        this.editReservationUseCase = editReservationUseCase;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> addReservation(@Valid @RequestBody ReservationRequestDto request) {
        Reservation saved = createReservationUseCase.addReservation(toDomain(request));
        return ResponseEntity.created(URI.create("/api/v1/reservations/" + saved.id())).body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> searchReservations(
            @RequestParam(required = false) String email, @RequestParam(required = false) String phone) {
        if (email == null && phone == null) {
         throw new IllegalArgumentException("At least one of email or phone must be provided");
        }
        List<ReservationResponseDto> result = getReservationByCustomerUseCase
                .findByCustomerEmailOrCustomerPhoneNumber(email, phone).stream().map(ReservationRestMapper::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getById(@PathVariable UUID id) {
        return getReservationUseCase.getReservationById(id)
                .map(reservation -> ResponseEntity.ok(toResponse(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(@PathVariable UUID id,
            @Valid @RequestBody ReservationRequestDto request) {
        Reservation updated = toDomain(request);

        Reservation saved = editReservationUseCase.updateReservation(id, updated);

        return ResponseEntity.ok(toResponse(saved));
    }

}
