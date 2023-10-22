package com.resa.fitness.service;

import com.resa.fitness.entity.ServiceMenu;
import com.resa.fitness.model.ServiceMenuRequest;
import com.resa.fitness.repository.ServiceMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@Service
public class ServiceMenuService {

    @Autowired
    private ServiceMenuRepository serviceMenuRepository;

    @Transactional
    public void createServiceMenu(ServiceMenuRequest request) {
        ServiceMenu serviceMenu = new ServiceMenu();
        serviceMenu.setName(request.getName());
        serviceMenu.setPricePerSession(request.getPricePerSession());
        serviceMenu.setTotalSessions(request.getTotalSessions());
        serviceMenu.setDurationInMinutes(request.getDurationInMinutes());
        serviceMenu.setSchedule(request.getSchedule());
        serviceMenu.setDescription(request.getDescription());

        serviceMenuRepository.save(serviceMenu);
    }

    public List<ServiceMenu> findAll() {
        return serviceMenuRepository.findAll();
    }

    public ServiceMenu getServiceMenuById(Long serviceMenuId) {
        final Optional<ServiceMenu> serviceMenu = serviceMenuRepository.findById(serviceMenuId);
        if (serviceMenu.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Service Menu not found");
        }

        return serviceMenu.get();
    }
}
