package com.resa.fitness.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@Getter
@Setter
@Entity(name = "service_menu")
@ToString
public class ServiceMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "price_per_session")
    private double pricePerSession;

    @Column(name = "total_sessions")
    private int totalSessions;
    private String schedule;
    @Column(name = "duration_in_minutes")
    private int durationInMinutes;
    private String description;

    @JsonIgnoreProperties("serviceMenu")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceMenu", orphanRemoval = true)
    private List<Exercise> exercises;

}
