package main.java.com.gestionabonnements.entites;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement{

    public AbonnementSansEngagement(String nomService, double montantMensuel, LocalDate dateDebut, LocalDate dateFin) {
        super(nomService, montantMensuel, dateDebut, dateFin);
    }

    @Override
    public String toString() {
        return "AbonnementSansEngagement{" +
                super.toString() +
                " }";
    }
}
