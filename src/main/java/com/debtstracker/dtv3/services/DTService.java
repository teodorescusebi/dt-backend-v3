package com.debtstracker.dtv3.services;

import com.debtstracker.dtv3.dtos.DTO;

public interface DTService {

    DTO.SingleValue<String> authenticate(DTO.Authentication authenticationDto);

    void logout(String sessionId);

    DTO.Profile getProfile(String sessionId);

    DTO.UserTotals getUserTotals(String sessionId);

    DTO.PagedContent<DTO.DebtSummary> getDebts(String sessionId, Integer page, Integer size);

    void addDebt(String sessionId, DTO.NewDebt newDebtDto);

    DTO.UserTotals equateDebts(String sessionId);
}
