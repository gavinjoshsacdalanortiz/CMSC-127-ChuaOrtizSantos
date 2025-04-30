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
    private Long feeId;
    private BigDecimal amount;
    private String semester;
    private String academicYear;
    private LocalDate dueDate;
    private LocalDate datePaid;
    private Long memberId;
    
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    
    public Fee() {}
    
    public Fee(BigDecimal amount, String semester, String academicYear, 
               LocalDate dueDate, LocalDate datePaid, Long memberId, Organization organization) {
        this.amount = amount;
        this.semester = semester;
        this.academicYear = academicYear;
        this.dueDate = dueDate;
        this.datePaid = datePaid;
        this.memberId = memberId;
        this.organization = organization;
    }
    
    public Long getFeeId() {
        return feeId;
    }
    
    public void setFeeId(Long feeId) {
        this.feeId = feeId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
    
    //---correction----
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    //------------------------------------------
    
    public LocalDate getDatePaid() {
        return datePaid;
    }
    
    public void setDatePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
    }
    
    public Long getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
    public Organization getOrganization() {
        return organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}