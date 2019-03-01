package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;



@Data
@Entity
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transaktionId;

    private String artikelName;
    private String besitzerName;
    private String kundeName;

    private int transaktionBetrag;


    public Transaktion(){

    }

    /**
     * Konstruktor.
     *
     *
     * @param artikelName       Name des Artikels
     * @param besitzerName  Name des Verleihenden/Verkaufenden
     * @param kundeName  des Ausleihenden/Kaeufers
     * @param transaktionBetrag Betrag, welcher verbucht wird
     */

    public Transaktion(String artikelName, String besitzerName, String kundeName, int transaktionBetrag) {
        this.artikelName = artikelName;
        this.besitzerName = besitzerName;
        this.kundeName = kundeName;
        this.transaktionBetrag = transaktionBetrag;
    }







}
