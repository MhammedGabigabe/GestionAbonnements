package main.java.com.gestionabonnements.entites;

import java.time.LocalDate;

public class AbonnementAvecEngagement extends  Abonnement{
    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin, int dureeEngagementMois){
        super(nomService, montantMensuel, dateDebut, dateFin);
        this.dureeEngagementMois = dureeEngagementMois;
    }

    public int getDureeEngagementMois() {
        return dureeEngagementMois;
    }

    public void setDureeEngagementMois(int dureeEngagementMois) {
        this.dureeEngagementMois = dureeEngagementMois;
    }

    @Override
    public String toString() {
        return "AbonnementAvecEngagement{" + super.toString() +
                ", dureeEngagementMois=" + dureeEngagementMois +
                " }";
    }
}
