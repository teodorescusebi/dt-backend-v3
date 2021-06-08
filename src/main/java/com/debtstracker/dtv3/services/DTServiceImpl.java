package com.debtstracker.dtv3.services;

import com.debtstracker.dtv3.dtos.DTO;
import com.debtstracker.dtv3.exceptions.CustomConflictException;
import com.debtstracker.dtv3.exceptions.CustomNotFoundException;
import com.debtstracker.dtv3.models.Debt;
import com.debtstracker.dtv3.models.User;
import com.debtstracker.dtv3.repositories.DebtRepository;
import com.debtstracker.dtv3.repositories.UserRepository;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DTServiceImpl implements DTService {

    private final UserRepository userRepository;
    private final DebtRepository debtRepository;

    @Autowired
    public DTServiceImpl(UserRepository userRepository, DebtRepository debtRepository) {
        this.userRepository = userRepository;
        this.debtRepository = debtRepository;
    }

    @SneakyThrows
    @Override
    public DTO.SingleValue<String> authenticate(DTO.Authentication authenticationDto) {
        Optional<User> optionalUser = userRepository.findByUsername(authenticationDto.username());
        User user;
        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(authenticationDto.password()))
            throw new CustomConflictException(CustomConflictException.Type.BAD_CREDENTIALS);
        else
            user = optionalUser.get();

        user.setSessionIdentifier(UUID.randomUUID().toString());
        userRepository.save(user);

        return new DTO.SingleValue<>(user.getSessionIdentifier());
    }

    @Override
    public void logout(String sessionId) {
        Optional<User> optionalUser = userRepository.findBySessionIdentifier(sessionId);
        optionalUser.ifPresent(user -> {
            user.setSessionIdentifier(null);
            userRepository.save(user);
        });
    }

    @SneakyThrows
    @Override
    public DTO.Profile getProfile(String sessionId) {
        User user = userRepository.findBySessionIdentifier(sessionId).orElseThrow(() -> new CustomNotFoundException(User.class));

        return new DTO.Profile(user.getName(), user.getProfilePictureUrl());
    }

    @SneakyThrows
    @Override
    public DTO.UserTotals getUserTotals(String sessionId) {
        User user = userRepository.findBySessionIdentifier(sessionId).orElseThrow(() -> new CustomNotFoundException(User.class));

        Optional<Double> totalBorrowed = debtRepository.getTotalBorrowedByUser(user);
        Optional<Double> totalLent = debtRepository.getTotalLentByUser(user);

        return new DTO.UserTotals(
                totalBorrowed.orElse(0.0),
                totalLent.orElse(0.0)
        );
    }

    @SneakyThrows
    @Override
    public DTO.PagedContent<DTO.DebtSummary> getDebts(String sessionId, Integer page, Integer size) {
        User user = userRepository.findBySessionIdentifier(sessionId).orElseThrow(() -> new CustomNotFoundException(User.class));
        Page<Debt> debts = debtRepository.findAllByOrderByCreationDateDesc(PageRequest.of(page, size));

        List<DTO.DebtSummary> debtSummaries = new ArrayList<>();

        debts.forEach(debt -> {
            DTO.DebtSummary debtSummary = new DTO.DebtSummary(
                    debt.getId(),
                    debt.getSum(),
                    debt.getDescription(),
                    debt.getCreationDate(),
                    debt.getPaidDate(),
                    debt.getStatus(),
                    debt.getLender().getId().equals(user.getId())
            );

            debtSummaries.add(debtSummary);
        });

        return new DTO.PagedContent<>(
                debtSummaries,
                page,
                size,
                debts.getTotalElements(),
                debts.getTotalPages()
        );
    }

    @SneakyThrows
    @Override
    public void addDebt(String sessionId, DTO.NewDebt newDebtDto) {
        User currentUser = userRepository.findBySessionIdentifier(sessionId).orElseThrow(() -> new CustomNotFoundException("current_user"));
        User otherUser = userRepository.findBySessionIdentifierNot(sessionId).orElseThrow(() -> new CustomNotFoundException("other_user"));

        Debt debt = Debt.builder()
                .creationDate(newDebtDto.creationDate())
                .lender(newDebtDto.isUserLender() ? currentUser : otherUser)
                .borrower(newDebtDto.isUserLender() ? otherUser : currentUser)
                .description(newDebtDto.description())
                .sum(newDebtDto.sum())
                .status(Debt.Status.UNPAID)
                .build();

        debtRepository.save(debt);
    }

    @SneakyThrows
    @Override
    public DTO.UserTotals equateDebts(String sessionId) {
        User currentUser = userRepository.findBySessionIdentifier(sessionId).orElseThrow(() -> new CustomNotFoundException(User.class));
        User otherUser = userRepository.findBySessionIdentifierNot(sessionId).orElseThrow(() -> new CustomNotFoundException(User.class));

        double borrowed = debtRepository.getTotalBorrowedByUser(currentUser).orElse(0.0);
        double lent = debtRepository.getTotalLentByUser(currentUser).orElse(0.0);

        if (borrowed == 0.0 || lent == 0.0)
            return new DTO.UserTotals(borrowed, lent);

        debtRepository.updateAllASPaidWithDate(DateTime.now().getMillis());

        if (borrowed == lent)
            return this.getUserTotals(sessionId);

        Debt debt = Debt.builder()
                .creationDate(DateTime.now().getMillis())
                .lender(borrowed > lent ? otherUser : currentUser)
                .borrower(borrowed > lent ? currentUser : otherUser)
                .description("Remaining sum after equate")
                .sum(Math.abs(borrowed - lent))
                .status(Debt.Status.UNPAID)
                .build();

        debtRepository.save(debt);

        return this.getUserTotals(sessionId);
    }

}
