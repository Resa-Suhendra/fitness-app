package com.resa.fitness.repository;


import com.resa.fitness.entity.Exercise;
import com.resa.fitness.entity.ServiceMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    public List<Exercise> findAllByServiceMenu(ServiceMenu serviceMenu);
}
