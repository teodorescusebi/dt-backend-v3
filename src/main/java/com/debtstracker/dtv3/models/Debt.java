package com.debtstracker.dtv3.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DEBTS")
public class Debt {
    @Id
    @GeneratedValue
    private Long id;

    private Long creationDate;

    private Long paidDate;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private User lender;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @Column(length = 500)
    private String description;

    private Double sum;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PAID,
        UNPAID,
        DELETED
    }
}
