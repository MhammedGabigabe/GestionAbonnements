package main.java.com.gestionabonnements.entites;

import java.time.LocalDate;
import java.util.UUID;

public class Paiement {
    private final String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private String typePaiement;
    private StatutPaiement statut;

    public Paiement(String idAbonnement, LocalDate dateEcheance, LocalDate datePaiement, String typePaiement, StatutPaiement statut) {
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
        this.idPaiement = UUID.randomUUID().toString();
    }

    public String getIdPaiement() {
        return idPaiement;
    }

    public String getIdAbonnement() {
        return idAbonnement;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setIdAbonnement(String idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement='" + idPaiement + '\'' +
                ", idAbonnement='" + idAbonnement + '\'' +
                ", dateEcheance=" + dateEcheance +
                ", datePaiement=" + datePaiement +
                ", typePaiement='" + typePaiement + '\'' +
                ", statut=" + statut +
                '}';
    }
}
