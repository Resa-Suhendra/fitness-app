package com.resa.fitness.service;

import com.resa.fitness.entity.Exercise;
import com.resa.fitness.entity.ServiceMenu;
import com.resa.fitness.model.ExerciseRequest;
import com.resa.fitness.repository.ExerciseRepository;
import com.resa.fitness.repository.ServiceMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ServiceMenuRepository serviceMenuRepository;

    public void createExercise(ExerciseRequest request) {
        Optional<ServiceMenu> serviceMenuOptional = serviceMenuRepository.findById(request.getServiceMenuId());
        if (serviceMenuOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ServiceMenu not found");
        }

        Exercise exercise = new Exercise();
        exercise.setServiceMenu(serviceMenuOptional.get());
        exercise.setName(request.getName());
        exercise.setDurationInMinutes(request.getDurationInMinutes());
        exercise.setDescription(request.getDescription());


        exerciseRepository.save(exercise);
    }

    public List<Exercise> getExerciseByServiceMenuId(Long id) {
        Optional<ServiceMenu> serviceMenuOptional = serviceMenuRepository.findById(id);
        if (serviceMenuOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ServiceMenu not found");
        }
        return exerciseRepository.findAllByServiceMenu(serviceMenuOptional.get());
    }
}
