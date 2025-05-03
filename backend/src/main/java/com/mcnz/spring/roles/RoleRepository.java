package com.mcnz.spring.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM roles WHERE name = :name", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM roles WHERE name = :name LIMIT 1)", nativeQuery = true)
    Boolean existsByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO roles (name) VALUES (:name)", nativeQuery = true)
    int save(@Param("name") String name);

}