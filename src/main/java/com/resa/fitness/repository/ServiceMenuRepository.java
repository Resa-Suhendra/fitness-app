package com.resa.fitness.repository;

import com.resa.fitness.entity.ServiceMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceMenuRepository extends JpaRepository<ServiceMenu, Long> {
}
