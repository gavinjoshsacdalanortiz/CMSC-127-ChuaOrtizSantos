package com.mcnz.spring.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM role WHERE name = CAST(:name as member_status_enum)", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM role WHERE name = CAST(:name as member_status_enum) LIMIT 1)", nativeQuery = true)
    Boolean existsByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO role (name) VALUES (CAST(:name as member_status_enum))", nativeQuery = true)
    int save(@Param("name") String name);

}