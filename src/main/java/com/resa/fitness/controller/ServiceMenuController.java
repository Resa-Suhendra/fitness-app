package com.resa.fitness.controller;

import com.resa.fitness.entity.ServiceMenu;
import com.resa.fitness.model.ServiceMenuRequest;
import com.resa.fitness.model.WebResponse;
import com.resa.fitness.service.ServiceMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@RestController
public class ServiceMenuController {

    @Autowired
    private ServiceMenuService serviceMenuService;

    @PostMapping(
            path = "/api/service-menu",
            consumes = "application/json",
            produces = "application/json"
    )
    public WebResponse<String> createServiceMenu(@RequestBody ServiceMenuRequest serviceMenuRequest) {
        serviceMenuService.createServiceMenu(serviceMenuRequest);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/service-menu",
            produces = "application/json"
    )
    public WebResponse<ServiceMenu> getServiceMenu(@RequestParam("id") Long id) {
        return WebResponse.<ServiceMenu>builder().data(serviceMenuService.getServiceMenuById(id)).build();
    }

    @GetMapping(
            path = "/api/service-menu/all",
            produces = "application/json"
    )
    public WebResponse<Iterable<ServiceMenu>> getServiceMenu() {
        return WebResponse.<Iterable<ServiceMenu>>builder().data(serviceMenuService.findAll()).build();
    }
}
