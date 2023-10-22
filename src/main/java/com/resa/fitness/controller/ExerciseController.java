package com.resa.fitness.controller;

import com.resa.fitness.entity.Exercise;
import com.resa.fitness.model.ExerciseRequest;
import com.resa.fitness.model.WebResponse;
import com.resa.fitness.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@RestController
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping(
            path = "/api/exercise",
            consumes = "application/json",
            produces = "application/json"
    )
    public WebResponse<String> createExercise(@RequestBody ExerciseRequest exercise) {
        exerciseService.createExercise(exercise);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/exercise",
            produces = "application/json"
    )
    public WebResponse<List<Exercise>> getExercise(@RequestParam("serviceMenuId") Long serviceMenuId) {
        List<Exercise> list = exerciseService.getExerciseByServiceMenuId(serviceMenuId);
        return WebResponse.<List<Exercise>>builder().data(list).build();
    }
}
