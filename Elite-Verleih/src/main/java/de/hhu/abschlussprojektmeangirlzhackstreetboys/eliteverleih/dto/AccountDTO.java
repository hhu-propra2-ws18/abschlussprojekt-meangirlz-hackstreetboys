package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountDTO {

    private String account;
    private double amount;
    private List<ReservationDTO> resavations;

}
