package com.debtstracker.dtv3.repositories;

import com.debtstracker.dtv3.models.Debt;
import com.debtstracker.dtv3.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {

    @Query("select sum(d.sum) from Debt d where d.borrower=:user and d.status='UNPAID'")
    Optional<Double> getTotalBorrowedByUser(User user);

    @Query("select sum(d.sum) from Debt d where d.lender=:user and d.status='UNPAID'")
    Optional<Double> getTotalLentByUser(User user);

    Page<Debt> findAllByOrderByCreationDateDesc(Pageable pageable);

    @Query("select  max(d.sum) from Debt d where d.lender=:user and d.status='UNPAID' and d.sum<=:sum")
    Optional<Debt> findLendingByUserWithSumLessThan(User user, Double sum);

    @Query("select  max(d.sum) from Debt d where d.borrower=:user and d.status='UNPAID' and d.sum<=:sum")
    Optional<Debt> findBorrowingByUserWithSumLessThan(User user, Double sum);

    @Query("select min(d.sum) from Debt d where d.lender=:user and d.status='UNPAID'")
    Optional<Debt> findSmallestByLender(User user);

    @Query("select min(d.sum) from Debt d where d.borrower=:user and d.status='UNPAID'")
    Optional<Debt> findSmallestByBorrower(User user);

    @Query("update Debt d set d.status='PAID', d.paidDate=:date where d.status='UNPAID'")
    void updateAllASPaidWithDate(Long date);

}
