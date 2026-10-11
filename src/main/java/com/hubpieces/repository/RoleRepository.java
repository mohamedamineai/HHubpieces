package com.hubpieces.repository;

import com.hubpieces.model.Role;
import com.hubpieces.model.RoleNom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNom(RoleNom nom);
}
