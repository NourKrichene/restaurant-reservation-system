package com.nour.restaurant_manager.adapter.out.persistence.jpa.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
public class RestaurantJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String description;
    private int capacity;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingTimeJpaEntity> workingTimes;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExceptionalClosingTimeJpaEntity> exceptionalClosingTimes;

    public RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(UUID id, String name, String description, List<WorkingTimeJpaEntity> workingTimes, List<ExceptionalClosingTimeJpaEntity> exceptionalClosingTimes, int capacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.workingTimes = workingTimes;
        this.exceptionalClosingTimes = exceptionalClosingTimes;
        this.capacity = capacity;

        if (workingTimes != null) {
            workingTimes.forEach(w -> w.setRestaurant(this));
        }
        if (exceptionalClosingTimes != null) {
            exceptionalClosingTimes.forEach(e -> e.setRestaurant(this));
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<WorkingTimeJpaEntity> getWorkingTimes() {
        return workingTimes;
    }

    public void setWorkingTimes(List<WorkingTimeJpaEntity> workingTimes) {
        this.workingTimes = workingTimes;
        if (workingTimes != null) {
            workingTimes.forEach(w -> w.setRestaurant(this));
        }
    }

    public List<ExceptionalClosingTimeJpaEntity> getExceptionalClosingTimes() {
        return exceptionalClosingTimes;
    }

    public void setExceptionalClosingTimes(List<ExceptionalClosingTimeJpaEntity> exceptionalClosingTimes) {
        this.exceptionalClosingTimes = exceptionalClosingTimes;
        if (exceptionalClosingTimes != null) {
            exceptionalClosingTimes.forEach(e -> e.setRestaurant(this));
        }
    }
}
