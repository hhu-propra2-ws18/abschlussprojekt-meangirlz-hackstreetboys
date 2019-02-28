package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;



@Data
@Entity
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transaktionId;

    @OneToOne(fetch= FetchType.LAZY)
    private Artikel artikel;

    @OneToOne(fetch = FetchType.LAZY)
    private Ausleihe ausleihe;

    private int transaktionBetrag;


    public Transaktion(){

    }

    /**
     * Konstruktor.
     *
     * @param artikel           Artikel welcher ausgeliehen wird
     * @param ausleihe          Ausleihe, durch welche Transaktionen entstehen
     * @param transaktionBetrag Betrag, welcher verbucht wird
     */

    public Transaktion(Artikel artikel, Ausleihe ausleihe, int transaktionBetrag){
        this.artikel = artikel;
        this.ausleihe = ausleihe;
        this.transaktionBetrag = transaktionBetrag;
    }


}
