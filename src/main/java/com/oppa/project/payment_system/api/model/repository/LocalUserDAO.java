package com.oppa.project.payment_system.api.model.repository;

import com.oppa.project.payment_system.api.model.entity.LocalUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalUserDAO extends CrudRepository<LocalUser, Long> {


    Optional<LocalUser> findByEmailIgnoreCase(String email);
}
