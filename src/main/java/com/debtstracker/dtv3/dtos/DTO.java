package com.debtstracker.dtv3.dtos;

import com.debtstracker.dtv3.models.Debt;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class DTO {

    public static record Authentication(String username, String password) {
    }

    public static record SingleValue<T>(T value) {
    }

    public static record Message(String message) {
    }

    public record UserTotals(Double totalBorrowed, Double totalLent) {
    }

    @Getter
    public static class PagedContent<T> {
        private final List<T> content;

        private final Integer currentPage;

        private final Integer pageSize;

        private final Integer totalPages;

        private final Long totalResults;

        public PagedContent(@NonNull List<T> content, int currentPage, int pageSize, long totalResults, int totalPages) {
            if (currentPage < 0 || pageSize < 1 || totalResults < 0)
                throw new IllegalArgumentException();

            this.content = Collections.unmodifiableList(content);
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalResults = totalResults;
            this.totalPages = totalPages;
        }
    }

    public static record Profile(String name, String profilePictureUrl) {
    }

    public static record DebtSummary(Long id, Double sum, String description, Long creationDate, Long paidDate,
                                     Debt.Status status, Boolean isUserLender) {
    }

    public record NewDebt(Double sum, String description, Long creationDate, Boolean isUserLender) {
    }
}
