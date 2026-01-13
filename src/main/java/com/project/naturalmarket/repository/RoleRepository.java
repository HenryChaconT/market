package com.project.naturalmarket.repository;

import com.project.naturalmarket.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);

    @Query(value = "SELECT r.* FROM roles r WHERE r.name IN ?1", nativeQuery = true)
    Set<Role>findAllByName(Set<String> names);
}
