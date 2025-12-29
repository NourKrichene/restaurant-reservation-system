package com.nour.reservation_manager.adapter.config;

import com.nour.reservation_manager.adapter.out.id.UuidIdGeneratorAdapter;
import com.nour.reservation_manager.adapter.out.persistence.jpa.ReservationRepositoryAdapter;
import com.nour.reservation_manager.adapter.out.persistence.jpa.SpringDataReservationRepository;
import com.nour.reservation_manager.adapter.out.rest.RestaurantRestProvider;
import com.nour.reservation_manager.application.port.in.CreateReservationUseCase;
import com.nour.reservation_manager.application.port.in.EditReservationUseCase;
import com.nour.reservation_manager.application.port.in.GetReservationByCustomerUseCase;
import com.nour.reservation_manager.application.port.in.GetReservationUseCase;
import com.nour.reservation_manager.application.port.out.AvailabilityService;
import com.nour.reservation_manager.application.port.out.IdGenerator;
import com.nour.reservation_manager.application.port.out.ReservationRepository;
import com.nour.reservation_manager.application.port.out.RestaurantProvider;
import com.nour.reservation_manager.application.usecase.CreateReservationUseCaseImpl;
import com.nour.reservation_manager.application.usecase.EditReservationUseCaseImpl;
import com.nour.reservation_manager.application.usecase.GetReservationByCustomerUseCaseImpl;
import com.nour.reservation_manager.application.usecase.GetReservationUseCaseImpl;
import com.nour.reservation_manager.domain.service.AvailabilityServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public IdGenerator idGenerator() {
        return new UuidIdGeneratorAdapter();
    }

    @Bean
    public ReservationRepository reservationRepository(SpringDataReservationRepository jpaRepository) {
        return new ReservationRepositoryAdapter(jpaRepository);
    }

    @Bean
    public RestaurantProvider restaurantProvider(RestClient.Builder restClientBuilder, @Value("${restaurant.service.url}") String restaurantServiceUrl) {
        return new RestaurantRestProvider(restClientBuilder, restaurantServiceUrl);
    }

    @Bean
    public AvailabilityService availabilityService(RestaurantProvider restaurantProvider, ReservationRepository reservationRepository) {
        return new AvailabilityServiceImpl(restaurantProvider, reservationRepository);
    }

    @Bean
    public CreateReservationUseCase createReservationUseCase(ReservationRepository reservationRepository, IdGenerator idGenerator, AvailabilityService availabilityService) {
        return new CreateReservationUseCaseImpl(reservationRepository, idGenerator, availabilityService);
    }

    @Bean
    public GetReservationUseCase getReservationUseCase(ReservationRepository reservationRepository) {
        return new GetReservationUseCaseImpl(reservationRepository);
    }

    @Bean
    public GetReservationByCustomerUseCase getReservationByCustomerUseCase(ReservationRepository reservationRepository) {
        return new GetReservationByCustomerUseCaseImpl(reservationRepository);
    }

    @Bean
    public EditReservationUseCase editReservationUseCase(ReservationRepository reservationRepository) {
        return new EditReservationUseCaseImpl(reservationRepository);
    }
}
