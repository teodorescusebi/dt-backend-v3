package com.debtstracker.dtv3.controllers;

import com.debtstracker.dtv3.dtos.DTO;
import com.debtstracker.dtv3.services.DTService;
import com.debtstracker.dtv3.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DTController {

    private final DTService dtService;

    @Autowired
    public DTController(DTService dtService) {
        this.dtService = dtService;
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<DTO.SingleValue<String>> authenticate(@RequestBody DTO.Authentication authenticationDto) {
        return new ResponseEntity<>(dtService.authenticate(authenticationDto), HttpStatus.OK);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<DTO.Message> logout(@RequestHeader Map<String, String> headers) {
        dtService.logout(Helpers.getSessionIdFromHeaders(headers));
        return new ResponseEntity<>(new DTO.Message(HttpStatus.OK.toString()), HttpStatus.OK);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<DTO.Profile> getProfile(@RequestHeader Map<String, String> header) {
        return new ResponseEntity<>(dtService.getProfile(Helpers.getSessionIdFromHeaders(header)), HttpStatus.OK);
    }

    @GetMapping("/home/totals")
    public ResponseEntity<DTO.UserTotals> getUserTotals(@RequestHeader Map<String, String> header) {
        return new ResponseEntity<>(dtService.getUserTotals(Helpers.getSessionIdFromHeaders(header)), HttpStatus.OK);
    }

    @GetMapping("/home/debts")
    public ResponseEntity<DTO.PagedContent<DTO.DebtSummary>> getDebts(
            @RequestHeader Map<String, String> header,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return new ResponseEntity<>(dtService.getDebts(Helpers.getSessionIdFromHeaders(header), page, size), HttpStatus.OK);
    }

    @PostMapping("/debts/add")
    public ResponseEntity<DTO.Message> addDebt(@RequestHeader Map<String, String> header, @RequestBody DTO.NewDebt newDebtDto) {
        dtService.addDebt(Helpers.getSessionIdFromHeaders(header), newDebtDto);
        return new ResponseEntity<>(new DTO.Message(HttpStatus.OK.toString()), HttpStatus.OK);
    }

    @PostMapping("/debts/equate")
    public ResponseEntity<DTO.UserTotals> equateDebts(@RequestHeader Map<String, String> header) {
        return new ResponseEntity<>(dtService.equateDebts(Helpers.getSessionIdFromHeaders(header)), HttpStatus.OK);
    }

}
