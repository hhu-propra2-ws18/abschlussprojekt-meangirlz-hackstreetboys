package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountDto {

    private String account;
    private double amount;
    private List<ReservationDto> resavations;

}
