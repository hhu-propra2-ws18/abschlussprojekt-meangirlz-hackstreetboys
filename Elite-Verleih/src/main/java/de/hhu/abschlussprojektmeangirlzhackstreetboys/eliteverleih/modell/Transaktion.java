package de.hhu.abschlussprojektmeangirlzhackstreetboys.eliteverleih.modell;

import lombok.Data;

import javax.persistence.*;



@Data
@Entity
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transaktionId;

    @ManyToOne(fetch= FetchType.LAZY)
    private Ausleihe ausleihe;

    private int transaktionBetrag;

    public Ausleihe getAusleihe(){
        return ausleihe;
    }


    public Transaktion(){

    }

    /**
     * Konstruktor.
     *
     *
     * @param ausleihe          Ausleihe, durch welche Transaktionen entstehen
     * @param transaktionBetrag Betrag, welcher verbucht wird
     */

    public Transaktion(Ausleihe ausleihe, int transaktionBetrag){

        this.ausleihe = ausleihe;
        this.transaktionBetrag = transaktionBetrag;
    }







}
