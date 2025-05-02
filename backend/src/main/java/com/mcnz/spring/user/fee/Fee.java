package com.mcnz.spring.fee;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.mcnz.spring.organization.Organization;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fees")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate dueDate;
    private boolean isPaid;
    
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    public Fee() {}
    
    public Fee(String description, BigDecimal amount, LocalDate dueDate, boolean isPaid, Organization organization) {
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
        this.organization = organization;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
    
    public Organization getOrganization() {
        return organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}