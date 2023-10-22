package com.resa.fitness.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@Getter
@Setter
@Entity(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "duration_in_minutes")
    private int durationInMinutes;
    private String description;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "service_menu_id")
    private ServiceMenu serviceMenu;

}
