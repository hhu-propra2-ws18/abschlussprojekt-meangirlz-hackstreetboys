package de.hhu.abschlussprojektmeangirlzhackstreetboys.EliteVerleih.dto;

import lombok.Data;

@Data
public class AccountDTO {

    private String account;
    private double amount;
    private ReservationDTO resavations;

}
