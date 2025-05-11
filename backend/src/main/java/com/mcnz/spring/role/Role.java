package com.mcnz.spring.role;

import jakarta.persistence.*;

@Entity
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer roleId;

  @Enumerated(EnumType.STRING)
  @Column(length = 20, unique = true, nullable = false)
  private ERole name;

  public Role() {

  }

  public Role(ERole name) {
    this.name = name;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setId(Integer roleId) {
    this.roleId = roleId;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole name) {
    this.name = name;
  }
}