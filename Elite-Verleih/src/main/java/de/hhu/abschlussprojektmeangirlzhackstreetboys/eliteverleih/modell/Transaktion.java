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
    private String verleihenderName;
    private String ausleihenderName;

    private int transaktionBetrag;


    public Transaktion(){

    }

    /**
     * Konstruktor.
     *
     *
     * @param artikelName       Name des Artikels
     * @param verleihenderName  Name des Verleihenden
     * @param ausleihenderName  des Ausleihenden
     * @param transaktionBetrag Betrag, welcher verbucht wird
     */

    public Transaktion(String artikelName, String verleihenderName, String ausleihenderName, int transaktionBetrag) {
        this.artikelName = artikelName;
        this.verleihenderName = verleihenderName;
        this.ausleihenderName = ausleihenderName;
        this.transaktionBetrag = transaktionBetrag;
    }







}
